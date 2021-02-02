package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.PermissionGroup;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class AdminPermissionGroupController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private UtilsEJB utilsEJB = null;
    private Textbox txtNamePermissionGroup;
    private Radio rEnabledYes;
    private Radio rEnabledNo;
    private PermissionGroup permissionGroupParam;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;
        
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Sessions.getCurrent();
        permissionGroupParam = (Sessions.getCurrent().getAttribute("object") != null) ? (PermissionGroup) Sessions.getCurrent().getAttribute("object") : null;
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
           permissionGroupParam = null;                    
       } else {
           permissionGroupParam = (PermissionGroup) Sessions.getCurrent().getAttribute("object");            
       }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.permission.group.edit"));
                break;
            case WebConstants.EVENT_VIEW:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.permission.group.view"));
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
        txtNamePermissionGroup.setRawValue(null);
    }
    
    private void loadFields(PermissionGroup permissionGroup) {
        try {
            txtNamePermissionGroup.setText(permissionGroup.getName().toString());
            if (permissionGroup.getEnabled() == true) {
                rEnabledYes.setChecked(true);
            } else {
                rEnabledNo.setChecked(false);
            }
  
         } catch (Exception ex) {
            showError(ex);
        }    
       
    }

    public void blockFields() {
        txtNamePermissionGroup.setReadonly(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (txtNamePermissionGroup.getText().isEmpty()) {
            txtNamePermissionGroup.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            return true;
        }
        return false;

    }

    private void savePermissionGroup(PermissionGroup _permissionGroup) {
        Boolean indEnabled = true;
        try {
            PermissionGroup permissionGroup = null;
            if (_permissionGroup != null) {
               permissionGroup = _permissionGroup;
            } else {
                permissionGroup = new PermissionGroup();
            }
            
            if (rEnabledYes.isChecked()) {
                indEnabled = true;
            } else {
                indEnabled = false;
            }
            
            permissionGroup.setName(txtNamePermissionGroup.getText());
            permissionGroup.setEnabled(indEnabled);
            permissionGroup = utilsEJB.savePermissionGroup(permissionGroup);
            permissionGroupParam = permissionGroup;
            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    savePermissionGroup(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    savePermissionGroup(permissionGroupParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(permissionGroupParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(permissionGroupParam);
                txtNamePermissionGroup.setReadonly(true);
                blockFields();
                rEnabledYes.setDisabled(true);
                rEnabledNo.setDisabled(true);
                break;
            case WebConstants.EVENT_ADD:
                loadFields(permissionGroupParam);
                break;
            default:
                break;
        }
    }
}
