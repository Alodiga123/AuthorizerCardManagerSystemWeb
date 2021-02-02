package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.web.custom.components.ListcellEditButton;
import com.alodiga.cms.web.custom.components.ListcellViewButton;
import com.alodiga.cms.web.generic.controllers.GenericAbstractListController;
import com.alodiga.cms.web.utils.Utils;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Employee;
import com.cms.commons.models.Person;
import com.cms.commons.models.User;
import com.cms.commons.models.PhonePerson;
import com.cms.commons.util.Constants;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ListEmployeeController extends GenericAbstractListController<Employee> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private PersonEJB personEJB = null;
    private List<Employee> employeeList = null;
    private Textbox txtName;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            //Evaluar Permisos
            permissionEdit = true;
            permissionAdd = true;
            permissionRead = true;
            adminPage = "TabEmployee.zul";
            personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
            getData();
            loadDataList(employeeList);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
   public void getData() {
    employeeList = new ArrayList<Employee>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            employeeList = personEJB.getEmployee(request);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
        } catch (GeneralException ex) {
            showError(ex);
        }
    }



    public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Sessions.getCurrent().removeAttribute("object");
        Executions.getCurrent().sendRedirect("TabEmployee.zul");
    }
    
       
   public void onClick$btnDownload() throws InterruptedException {
       try {
            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            StringBuilder file = new StringBuilder(Labels.getLabel("cms.menu.user.list"));
            file.append("_");
            file.append(date);
            Utils.exportExcel(lbxRecords, file.toString());
        } catch (Exception ex) {
            showError(ex);
        }
        
    } 

    public void startListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadDataList(List<Employee> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {             
                for (Employee employee : list) {
                    item = new Listitem();
                    item.setValue(employee);
                    StringBuilder employeeName = new StringBuilder(employee.getFirstNames());
                    employeeName.append(" ");
                    employeeName.append(employee.getLastNames());
                    item.appendChild(new Listcell(employeeName.toString()));
                    if (String.valueOf(employee.getIdentificationNumber()) !=  null){
                        item.appendChild(new Listcell(String.valueOf(employee.getIdentificationNumber())));
                    } else{
                        item.appendChild(new Listcell(""));
                    }
                    
                    if (employee.getEmployedPositionId() != null) {
                        item.appendChild(new Listcell(employee.getEmployedPositionId().getName()));
                    } else {
                        item.appendChild(new Listcell(""));
                    }
                    
                    if(employee.getComercialAgencyId() != null){
                        item.appendChild(new Listcell(employee.getComercialAgencyId().getName()));
                    }else{
                        item.appendChild(new Listcell(""));
                    }
                    if (employee.getPersonId().getPhonePerson() != null){
                        item.appendChild(new Listcell(employee.getPersonId().getPhonePerson().getNumberPhone()));
                    } else {
                        item.appendChild(new Listcell(""));
                    }
                                
                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, employee) : new Listcell());
                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, employee) : new Listcell());
                    item.setParent(lbxRecords);
                }
            } else {
                btnDownload.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    private void showEmptyList(){
                Listitem item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);  
    }
    
    public List<Employee> getFilterList(String filter) {
        List<Employee> employeeList_ = new ArrayList<Employee>();
        try {
            if (filter != null && !filter.equals("")) {
                employeeList_ = personEJB.getSearchEmployee(filter);
            } else {
                return employeeList;
            }
        } catch (Exception ex) {
            showError(ex);
        }
        return employeeList_;  
    }

    
    public void onClick$btnClear() throws InterruptedException {
        txtName.setText("");
    }

     public void onClick$btnSearch() throws InterruptedException {
        try {
            loadDataList(getFilterList(txtName.getText()));
        } catch (Exception ex) {
            showError(ex);
        }
    }
}
