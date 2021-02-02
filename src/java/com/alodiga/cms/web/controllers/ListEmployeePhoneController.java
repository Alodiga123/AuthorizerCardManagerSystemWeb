package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractListController;
import com.alodiga.cms.web.utils.Utils;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Employee;
import com.cms.commons.models.PhonePerson;
import com.cms.commons.models.Person;
import com.cms.commons.util.Constants;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import java.util.ArrayList;
import org.zkoss.zul.Tab;

public class ListEmployeePhoneController extends GenericAbstractListController<PhonePerson> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtName;
    private UtilsEJB utilsEJB = null;
    private PersonEJB personEJB = null;
    private List<PhonePerson> phonePersonList = null;
    private Person person = null;
    private Tab tabEmployeePhone;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
        startListener();
    }
    
    public void onSelect$tabEmployeePhone() {
        try {
            doAfterCompose(self);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void startListener() {
        EventQueue que = EventQueues.lookup("updatePhonePerson", EventQueues.APPLICATION, true);
        que.subscribe(new EventListener() {
            public void onEvent(Event evt) {
                getData();
                loadDataList(phonePersonList);
            }
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            //Evaluar Permisos
            permissionEdit = true;
            permissionAdd = true;
            permissionRead = true;
            adminPage = "adminEmployeePhone.zul";
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
            startListener();
            getData();            
            if (phonePersonList != null) {
               loadDataList(phonePersonList);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }


    public void onClick$btnAdd() throws InterruptedException {
        try {
            Sessions.getCurrent().setAttribute(WebConstants.EVENTYPE, WebConstants.EVENT_ADD);
            Map<String, Object> paramsPass = new HashMap<String, Object>();
            paramsPass.put("object", phonePersonList);
            final Window window = (Window) Executions.createComponents(adminPage, null, paramsPass);
            window.doModal();
        } catch (Exception ex) {
            this.showMessage("sp.error.general", true, ex);
        }
    }
     
    public void loadDataList(List<PhonePerson> list) {
        String indMainPhone = null;
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (PhonePerson phonePerson : list) {
                    item = new Listitem();
                    item.setValue(phonePerson);
                    item.appendChild(new Listcell(phonePerson.getCountryCode()));
                    item.appendChild(new Listcell(phonePerson.getAreaCode()));
                    item.appendChild(new Listcell(phonePerson.getNumberPhone()));
                    item.appendChild(new Listcell(phonePerson.getExtensionPhoneNumber()));
                    if (phonePerson.getIndMainPhone() != null) {
                        if (phonePerson.getIndMainPhone() == true) {
                            indMainPhone = "Si";
                        } else {
                            indMainPhone = "No";
                        }
                        item.appendChild(new Listcell(indMainPhone));
                    } else {
                        item.appendChild(new Listcell("No"));
                    }                    
                    item.appendChild(new Listcell(phonePerson.getPhoneTypeId().getDescription()));
                    item.appendChild(createButtonEditModal(phonePerson));
                    item.appendChild(createButtonViewModal(phonePerson));
                    item.setParent(lbxRecords);
                }
            } else {
                btnDownload.setVisible(true);
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    public Listcell createButtonEditModal(final Object obg) {
        Listcell listcellEditModal = new Listcell();
        try {
            Button button = new Button();
            button.setImage("/images/icon-edit.png");
            button.setTooltiptext(Labels.getLabel("sp.common.actions.edit"));
            button.setClass("open orange");
            button.addEventListener("onClick", new EventListener() {
                @Override
                public void onEvent(Event arg0) throws Exception {
                    Sessions.getCurrent().setAttribute("object", obg);
                    Sessions.getCurrent().setAttribute(WebConstants.EVENTYPE, WebConstants.EVENT_EDIT);
                    Map<String, Object> paramsPass = new HashMap<String, Object>();
                    paramsPass.put("object", obg);
                    final Window window = (Window) Executions.createComponents(adminPage, null, paramsPass);
                    window.doModal();
                }

            });
            button.setParent(listcellEditModal);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listcellEditModal;
    }

    public Listcell createButtonViewModal(final Object obg) {
        Listcell listcellViewModal = new Listcell();
        try {
            Button button = new Button();
            button.setImage("/images/icon-invoice.png");
            button.setTooltiptext(Labels.getLabel("sp.common.actions.view"));
            button.setClass("open orange");
            button.addEventListener("onClick", new EventListener() {
                @Override
                public void onEvent(Event arg0) throws Exception {
                    Sessions.getCurrent().setAttribute("object", obg);
                    Sessions.getCurrent().setAttribute(WebConstants.EVENTYPE, WebConstants.EVENT_VIEW);
                    Map<String, Object> paramsPass = new HashMap<String, Object>();
                    paramsPass.put("object", obg);
                    final Window window = (Window) Executions.createComponents(adminPage, null, paramsPass);
                    window.doModal();
                }

            });
            button.setParent(listcellViewModal);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listcellViewModal;
    }

    public void getData() {
        Employee employee = null;
        phonePersonList = new ArrayList<PhonePerson>();
        try {
             //Empleado Principal
            AdminEmployeeController adminEmployee = new AdminEmployeeController();
            if (adminEmployee.getEmployeeParent().getPersonId() != null) {
                employee = adminEmployee.getEmployeeParent();
            }
            EJBRequest request = new EJBRequest();
            Map params = new HashMap();
            params.put(Constants.PERSON_KEY, employee.getPersonId().getId());
            request.setParams(params);
            phonePersonList = personEJB.getPhoneByPerson(request);            
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            showEmptyList();
        } catch (GeneralException ex) {
            showError(ex);
        }
    }

    private void showEmptyList()  {
        Listitem item = new Listitem();
        item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
        item.appendChild(new Listcell());
        item.appendChild(new Listcell());
        item.appendChild(new Listcell());
        item.appendChild(new Listcell());
        item.setParent(lbxRecords);        
    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("cms.common.network.list"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnClear() throws InterruptedException {
        txtName.setText("");
    }

    public List<PhonePerson> getFilterList(String filter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
