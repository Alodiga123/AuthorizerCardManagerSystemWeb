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
import com.cms.commons.models.PermissionGroup;
import com.cms.commons.models.PermissionGroupData;
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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Toolbarbutton;

public class AdminPermissionGroupDataController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private UtilsEJB utilsEJB = null;
    private PermissionGroupData permissionGroupDataParam;
    private Textbox txtDescription;
    private Textbox txtAlias;
    private Combobox cmbPermiGroup;
    private Combobox cmbLanguage;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Sessions.getCurrent();
        permissionGroupDataParam = (Sessions.getCurrent().getAttribute("object") != null) ? (PermissionGroupData) Sessions.getCurrent().getAttribute("object") : null;
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
           permissionGroupDataParam = null;                    
       } else {
           permissionGroupDataParam = (PermissionGroupData) Sessions.getCurrent().getAttribute("object");            
       }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_EDIT:   
                tbbTitle.setLabel(Labels.getLabel("cms.crud.permission.group.data.edit"));
                break;
            case WebConstants.EVENT_VIEW:  
                tbbTitle.setLabel(Labels.getLabel("cms.crud.permission.group.data.view"));
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
    
    private void loadFields(PermissionGroupData permissionGroupData) {
        try {
            txtDescription.setText(permissionGroupData.getDescription());
            txtAlias.setText(permissionGroupData.getAlias().toString());
        } catch (Exception ex) {
            showError(ex);
        }
        
    }

    public void blockFields() {
        txtDescription.setReadonly(true);
        txtAlias.setReadonly(true);
        cmbPermiGroup.setReadonly(true);
        cmbLanguage.setReadonly(true);
        btnSave.setVisible(false);
    }

    private void savePermissionGroupData(PermissionGroupData _permissionGroupData) throws RegisterNotFoundException, NullParameterException, GeneralException {
        try {
            PermissionGroupData permissionGroupData = null;
            
            if (_permissionGroupData != null) {
                permissionGroupData = _permissionGroupData;
            } else {//New PermissionGroupData
                permissionGroupData = new PermissionGroupData();
            }

            //Guardar PermissionGroupData
            permissionGroupData.setPermissionGroupId((PermissionGroup) cmbPermiGroup.getSelectedItem().getValue());
            permissionGroupData.setLanguageId((Language) cmbLanguage.getSelectedItem().getValue());
            permissionGroupData.setAlias(txtAlias.getText());
            permissionGroupData.setDescription(txtDescription.getText());
            permissionGroupData = utilsEJB.savePermissionGroupData(permissionGroupData);
            permissionGroupDataParam = permissionGroupData;
            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }
            
        public Boolean validateEmpty() {
        if (txtDescription.getText().isEmpty()) {
            txtDescription.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtAlias.getText().isEmpty()) {
            txtAlias.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);    
        } else {
            return true;
        }
        return false;
    }
    
    public void onClick$btnSave() throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    savePermissionGroupData(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    savePermissionGroupData(permissionGroupDataParam);
                    break;
                default:
                    break;
            }
        }
    }
     
    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(permissionGroupDataParam);
                loadCmbPermiGroup(eventType);
                loadCmbLanguage(eventType);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(permissionGroupDataParam);
                txtDescription.setReadonly(true);
                txtAlias.setReadonly(true);
                loadCmbPermiGroup(eventType);
                loadCmbLanguage(eventType);
                blockFields();
                loadCmbPermiGroup(eventType);
                loadCmbLanguage(eventType);
                break;
            case WebConstants.EVENT_ADD:
                loadCmbPermiGroup(eventType);
                loadCmbLanguage(eventType);
                break;
            default:
                break;
        }
    }

    private void loadCmbPermiGroup(Integer eventType) {
        EJBRequest request1 = new EJBRequest();
        List<PermissionGroup> permissionGroup;
        try {
            permissionGroup = utilsEJB.getPermissionGroup(request1);
            loadGenericCombobox(permissionGroup,cmbPermiGroup, "name",eventType,Long.valueOf(permissionGroupDataParam != null? permissionGroupDataParam.getPermissionGroupId().getId(): 0) );            
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
            loadGenericCombobox(languageList,cmbLanguage,"description",eventType,Long.valueOf(permissionGroupDataParam != null? permissionGroupDataParam.getLanguageId().getId(): 0) );            
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
