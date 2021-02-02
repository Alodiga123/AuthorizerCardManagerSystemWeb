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
import com.cms.commons.models.Person;
import com.cms.commons.models.User;
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

public class ListUserController extends GenericAbstractListController<User> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private PersonEJB personEJB = null;
    private List<User> userList = null;
    private User currentUser;
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
            currentUser = (User) session.getAttribute(Constants.USER_OBJ_SESSION);
            adminPage = "adminUser.zul";
            personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
            getData();
            loadDataList(userList);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
   public void getData() {
    userList = new ArrayList<User>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            userList = personEJB.getUser(request);
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
        Executions.getCurrent().sendRedirect(adminPage);
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

    public void loadDataList(List<User> list) {
        String indEnabled = null;
        Listitem item = null;
        try {
            lbxRecords.getItems().clear();
            if (list != null && !list.isEmpty()) {
                btnDownload.setVisible(true);
                for (User user : list) {
                    item = new Listitem();
                    item.setValue(user);
                    item.appendChild(new Listcell(user.getLogin()));
                    item.appendChild(new Listcell(user.getIdentificationNumber().toString()));
                    StringBuilder userName = new StringBuilder(user.getFirstNames());
                    userName.append(" ");
                    userName.append(user.getLastNames());
                    item.appendChild(new Listcell(userName.toString()));
                    item.appendChild(new Listcell(user.getComercialAgencyId().getName()));
                    if (user.getEnabled() == true) {
                        indEnabled = "Yes";
                    } else {
                        indEnabled = "No";
                    }
                    item.appendChild(new Listcell(indEnabled));
                    item.appendChild(new ListcellEditButton(adminPage, user));
                    item.appendChild(new ListcellViewButton(adminPage, user,true));
                    item.setParent(lbxRecords);
                }
            } else {
                btnDownload.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
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

    
    public List<User> getFilterList(String filter) {
        List<User> userList_ = new ArrayList<User>();
        try {
            if (filter != null && !filter.equals("")) {
                EJBRequest request1 = new EJBRequest();
                Map params = new HashMap();
                params.put(Constants.PARAM_USER, filter);
                request1.setParams(params);
                userList_ = personEJB.searchUser(request1);
            } else {
                return userList;
            }
        } catch (Exception ex) {
            showError(ex);
        }
        return userList_;  
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
