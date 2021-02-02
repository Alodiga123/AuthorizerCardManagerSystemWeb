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
import com.cms.commons.models.Profile;
import com.cms.commons.models.ProfileData;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class AdminProfileDataController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private UtilsEJB utilsEJB = null;
    private ProfileData profileDataParam;
    private Textbox txtDescription;
    private Textbox txtAlias;
    private Combobox cmbProfile;
    private Combobox cmbLanguage;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Sessions.getCurrent();
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
            profileDataParam = null;
        } else {
            profileDataParam = (ProfileData) Sessions.getCurrent().getAttribute("object");
        }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.profile.data.edit"));
                break;
            case WebConstants.EVENT_VIEW:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.profile.data.view"));
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
        txtAlias.setRawValue(null);
        txtDescription.setRawValue(null);
    }

    private void loadFields(ProfileData profileData) {
        try {
            txtDescription.setText(profileData.getDescription());
            txtAlias.setText(profileData.getAlias().toString());
        } catch (Exception ex) {
            showError(ex);
        }

    }

    public void blockFields() {
        cmbProfile.setReadonly(true);
        cmbLanguage.setReadonly(true);
        txtDescription.setReadonly(true);
        txtAlias.setReadonly(true);
        
        btnSave.setVisible(false);
    }

    private void saveProfileData(ProfileData _profileData) throws RegisterNotFoundException, NullParameterException, GeneralException {
        try {
            ProfileData profileData = null;

            if (_profileData != null) {
                profileData = _profileData;
            } else {//New ProfileData
                profileData = new ProfileData();
            }

            //Guardar ProfileData
            profileData.setProfileId((Profile) cmbProfile.getSelectedItem().getValue());
            profileData.setLanguageId((Language) cmbLanguage.getSelectedItem().getValue());
            profileData.setAlias(txtAlias.getText());
            profileData.setDescription(txtDescription.getText());
            profileData = utilsEJB.saveProfileData(profileData);
            
            profileDataParam = profileData;
            this.showMessage("sp.common.save.success", false, null);
            
            if (eventType == WebConstants.EVENT_ADD) {
                btnSave.setVisible(false);
            } else {
                btnSave.setVisible(true);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public Boolean validateEmpty() {
        if (cmbProfile.getSelectedItem() == null) {
            cmbProfile.setFocus(true);
            this.showMessage("cms.error.profileId.name", true, null);
        } else if (cmbLanguage.getSelectedItem() == null) {
            cmbLanguage.setFocus(true);
            this.showMessage("cms.error.language.id", true, null);
        } else if (txtAlias.getText().isEmpty()) {
            txtAlias.setFocus(true);
            this.showMessage("cms.error.alias.profile", true, null);
        } else if (txtDescription.getText().isEmpty()) {
            txtDescription.setFocus(true);
            this.showMessage("cms.error.description", true, null);
        } else {
            return true;
        }
        return false;
    }

    public void onClick$btnSave() throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveProfileData(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveProfileData(profileDataParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(profileDataParam);
                loadCmbProfile(eventType);
                loadCmbLanguage(eventType);
                break;
            case WebConstants.EVENT_VIEW:
                blockFields();
                loadFields(profileDataParam);
                loadCmbProfile(eventType);
                loadCmbLanguage(eventType);
                break;
            case WebConstants.EVENT_ADD:
                loadCmbProfile(eventType);
                loadCmbLanguage(eventType);
                break;
            default:
                break;
        }
    }

    private void loadCmbProfile(Integer eventType) {
        EJBRequest request1 = new EJBRequest();
        List<Profile> profile;
        try {
            profile = utilsEJB.getProfile(request1);
            loadGenericCombobox(profile, cmbProfile, "name", eventType, Long.valueOf(profileDataParam != null ? profileDataParam.getProfileId().getId() : 0));
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
            loadGenericCombobox(languageList, cmbLanguage, "description", eventType, Long.valueOf(profileDataParam != null ? profileDataParam.getLanguageId().getId() : 0));
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
