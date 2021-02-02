package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.commons.exception.RegisterNotFoundException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.models.Profile;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Toolbarbutton;

public class AdminProfileController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private Radio rEnabledYes;
    private Radio rEnabledNo;
    private Label txtOculto;
    private Hbox hboxOculto;
    private UtilsEJB utilsEJB = null;
    private Profile profileParam;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
            profileParam = null;
        } else {
            profileParam = (Profile) Sessions.getCurrent().getAttribute("object");
        }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.profile.edit"));
                break;
            case WebConstants.EVENT_VIEW:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.profile.view"));
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
        txtName.setRawValue(null);
    }

    private void loadFields(Profile profile) {
        try {
            txtName.setText(profile.getName());
            if (profile.getEnabled() == true) {
                rEnabledYes.setChecked(true);
            } else {
                rEnabledNo.setChecked(true);
            }
            btnSave.setVisible(true);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void blockFields() {
        txtName.setReadonly(true);
        rEnabledYes.setDisabled(true);
        rEnabledNo.setDisabled(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("cms.error.profile.name", true, null);
        } else {
            return true;
        }
        return false;
    }

    private void saveProfile(Profile _profile) throws RegisterNotFoundException, NullParameterException, GeneralException {
        boolean indEnabled = true;
        try {
            Profile profile = null;

            if (_profile != null) {
                profile = _profile;
            } else {
                profile = new Profile();
            }

            if (eventType == WebConstants.EVENT_EDIT) {
                if (rEnabledYes.isChecked()) {
                    indEnabled = true;
                } else {
                    indEnabled = false;
                }
            }

            //Guarda el Perfil de Sistema
            profile.setName(txtName.getText());
            profile.setEnabled(indEnabled);
            profile = utilsEJB.saveProfile(profile);
            profileParam = profile;

            if (eventType == WebConstants.EVENT_ADD) {
                btnSave.setVisible(false);
            } else {
                btnSave.setVisible(true);
            }

            this.showMessage("sp.common.save.success", false, null);
        } catch (WrongValueException ex) {
            showError(ex);
        }
    }

    public void onClick$btnSave() throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveProfile(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveProfile(profileParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void onclick$btnBack() {
        Executions.getCurrent().sendRedirect("listProfile.zul");
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(profileParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(profileParam);
                blockFields();
                break;
            case WebConstants.EVENT_ADD:
                rEnabledYes.setChecked(true);
                txtOculto.setVisible(false);
                rEnabledYes.setVisible(false);
                rEnabledNo.setVisible(false);
                hboxOculto.setVisible(false);
                
                
                break;
            default:
                break;
        }

    }

    private void setText(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
