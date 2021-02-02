package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.commons.exception.RegisterNotFoundException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Language;
import com.cms.commons.models.Permission;
import com.cms.commons.models.PermissionData;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import com.cms.commons.util.QueryConstants;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Toolbarbutton;

public class AdminPermissionDataController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private UtilsEJB utilsEJB = null;
    private PermissionData permissionDataParam;
    private Textbox txtDescription;
    private Textbox txtAlias;
    private Combobox cmbPermission;
    private Combobox cmbLanguage;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Sessions.getCurrent();
        permissionDataParam = (Sessions.getCurrent().getAttribute("object") != null) ? (PermissionData) Sessions.getCurrent().getAttribute("object") : null;
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
           permissionDataParam = null;                    
       } else {
           permissionDataParam = (PermissionData) Sessions.getCurrent().getAttribute("object");            
       }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_EDIT:   
                tbbTitle.setLabel(Labels.getLabel("cms.crud.permission.data.edit"));
                break;
            case WebConstants.EVENT_VIEW:  
                tbbTitle.setLabel(Labels.getLabel("cms.crud.permission.data.view"));
                break;
            default:
                break;
        }
        try {
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void clearFields() {
        txtDescription.setRawValue(null);
    }
    
    private void loadFields(PermissionData permissionData) {
        try {
            txtDescription.setText(permissionData.getDescription());
            txtAlias.setText(permissionData.getAlias().toString());
        } catch (Exception ex) {
            showError(ex);
        }
        
    }

    public void blockFields() {
        txtDescription.setReadonly(true);
        txtAlias.setReadonly(true);
        cmbPermission.setReadonly(true);
        cmbLanguage.setReadonly(true);
        btnSave.setVisible(false);
    }

    private void savePermissionData(PermissionData _permissionData) throws RegisterNotFoundException, NullParameterException, GeneralException {
        try {
            PermissionData permissionData = null;
            
            if (_permissionData != null) {
                permissionData = _permissionData;
            } else {//New PermissionData
                permissionData = new PermissionData();
            }

            //Guardar PermissionData
            permissionData.setPermissionId((Permission) cmbPermission.getSelectedItem().getValue());
            permissionData.setLanguageId((Language) cmbLanguage.getSelectedItem().getValue());
            permissionData.setAlias(txtAlias.getText());
            permissionData.setDescription(txtDescription.getText());
            permissionData = utilsEJB.savePermissionData(permissionData);
            permissionDataParam = permissionData;
            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }
            
        public Boolean validateEmpty() {
        if (cmbPermission.getSelectedItem() == null) {
            cmbPermission.setFocus(true);
            this.showMessage("cms.error.documentType.notSelected", true, null);
        } else if (cmbLanguage.getSelectedItem() == null) {
            cmbLanguage.setFocus(true);
            this.showMessage("sp.error.language.notSelected", true, null);
        }  else if (txtDescription.getText().isEmpty()) {
            txtDescription.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtAlias.getText().isEmpty()) {
            txtAlias.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);    
        }  else {
            return true;
        }
        return false;
    }
    
    public void onClick$btnSave() throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    savePermissionData(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    savePermissionData(permissionDataParam);
                    break;
                default:
                    break;
            }
        }
    }
    
    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(permissionDataParam);
                loadCmbPermission(eventType);
                loadCmbLanguage(eventType);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(permissionDataParam);
                txtDescription.setReadonly(true);
                txtAlias.setReadonly(true);
                loadCmbPermission(eventType);
                loadCmbLanguage(eventType);
                blockFields();
                loadCmbPermission(eventType);
                loadCmbLanguage(eventType);
                break;
            case WebConstants.EVENT_ADD:
                loadCmbPermission(eventType);
                loadCmbLanguage(eventType);
                break;
            default:
                break;
        }
    }

    private void loadCmbPermission(Integer eventType) {
        EJBRequest request1 = new EJBRequest();
        List<Permission> permission;
        try {
            permission = utilsEJB.getPermission(request1);
            loadGenericCombobox(permission,cmbPermission, "name",eventType,Long.valueOf(permissionDataParam != null? permissionDataParam.getPermissionId().getId() : 0) );            
        } catch (EmptyListException ex) {
            showError(ex);
            ex.printStackTrace();
        } catch (GeneralException ex) {
            showError(ex);
            ex.printStackTrace();
        } catch (NullParameterException ex) {
            showError(ex);
            ex.printStackTrace();
        }
    }
    
    private void loadCmbLanguage(Integer eventType) {
        EJBRequest request1 = new EJBRequest();
        List<Language> languageList;
        try {
            languageList = utilsEJB.getLanguage(request1);
            loadGenericCombobox(languageList,cmbLanguage,"description",eventType,Long.valueOf(permissionDataParam != null? permissionDataParam.getLanguageId().getId(): 0) );            
        } catch (EmptyListException ex) {
            showError(ex);
            ex.printStackTrace();
        } catch (GeneralException ex) {
            showError(ex);
            ex.printStackTrace();
        } catch (NullParameterException ex) {
            showError(ex);
            ex.printStackTrace();
        }    
    }

    private void setText(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
