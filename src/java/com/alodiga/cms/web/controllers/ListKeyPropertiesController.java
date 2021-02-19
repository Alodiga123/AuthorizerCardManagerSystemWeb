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
import com.cms.commons.models.User;
import com.cms.commons.models.KeyProperties;
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

public class ListKeyPropertiesController extends GenericAbstractListController<KeyProperties> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private PersonEJB personEJB = null;
    private UtilsEJB utilsEJB = null;
    private List<KeyProperties> keyPropertiesList = null;
    private User currentUser;
    private Textbox txtProduct;
    private Textbox txtChannel;
    private int optionFilter = 0;

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
            adminPage = "adminKeyProperties.zul";
            personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            getData();
            loadDataList(keyPropertiesList);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
   public void getData() {
    keyPropertiesList = new ArrayList<KeyProperties>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            keyPropertiesList = utilsEJB.getKeyProperties(request);
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
            StringBuilder file = new StringBuilder(Labels.getLabel("authorize.crud.keyProperties.list"));
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

    public void loadDataList(List<KeyProperties> list) {
        String indEnabled = null;
        Listitem item = null;
        try {
            lbxRecords.getItems().clear();
            if (list != null && !list.isEmpty()) {
                btnDownload.setVisible(true);
                for (KeyProperties keyProperties : list) {
                    item = new Listitem();
                    item.setValue(keyProperties);
                    item.appendChild(new Listcell(keyProperties.getProductId().getCountryId().getName()));
                    item.appendChild(new Listcell(keyProperties.getProductId().getName()));
                    item.appendChild(new Listcell(keyProperties.getChannelId().getName()));
                    item.appendChild(new Listcell(keyProperties.getKeyLength().toString()));
                    item.appendChild(new Listcell(keyProperties.getExpirationDays().toString()));
                    item.appendChild(new Listcell(keyProperties.getTotalPreviousKeys().toString()));
                    item.appendChild(new ListcellEditButton(adminPage, keyProperties));
                    item.appendChild(new ListcellViewButton(adminPage, keyProperties,true));
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

    
    public List<KeyProperties> getFilterList(String filter) {
       List<KeyProperties> keyProperties = new ArrayList<KeyProperties>();
       KeyProperties keyPropertties = null;
       List<KeyProperties> bankList = null;
        try {
            if (filter != null && !filter.equals("")) {
                if (optionFilter == 1) {
                   keyProperties = utilsEJB.getSearchKeyPropertiesByProduct(filter);
                   keyProperties.add(keyPropertties);
                } else {
                    bankList = utilsEJB.getSearchKeyPropertiesByChannel(filter);
                    for (KeyProperties b: bankList) {
                        keyProperties.add(b);
                    }
                }
            } else {
                return keyPropertiesList;
            }
        } catch (Exception ex) {
            showError(ex);
        }
        return keyProperties;
    }
    
    public void onClick$btnClear() throws InterruptedException {
        txtChannel.setText("");
        txtProduct.setText("");
    }

    public void onClick$btnSearch() throws InterruptedException {
        String txtFilter = "";
        try {
            if (txtProduct.getText() != "") {
                txtFilter = txtProduct.getText();
                optionFilter = 1;
            }
            else if (txtChannel.getText() != "") {
                txtFilter = txtChannel.getText();
                optionFilter = 2;
            }   
            loadDataList(getFilterList(txtFilter));
        } catch (Exception ex) {
            showError(ex);
        }
    }

}
