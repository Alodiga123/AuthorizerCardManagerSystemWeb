package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Profile;
import com.cms.commons.models.User;
import com.cms.commons.models.UserHasProfile;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class AdminUserHasProfileController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private Textbox txtCode;
    private Textbox txtShortName;
    private Textbox txtAlternativeName1;

    private UtilsEJB utilsEJB = null;
    private PersonEJB personEJB = null;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;
    
    private UserHasProfile UserHasProfileParam;
    private Radio rEnabledYes;
    private Radio rEnabledNo;
    private Textbox textUser;
    private Textbox textRole;
    private Combobox cmbUser;
    private Combobox cmbRole;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
            UserHasProfileParam = null;
        } else {
            UserHasProfileParam = (UserHasProfile) Sessions.getCurrent().getAttribute("object");
        }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.userHasProfile.edit"));
                break;
            case WebConstants.EVENT_VIEW:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.userHasProfile.view"));
                break;
            case WebConstants.EVENT_ADD:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.userHasProfile.add"));
                break;
            default:
                break;
        }
        try {
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        textRole.setRawValue(null);
        textUser.setRawValue(null);
        
    }

    private void loadFields(UserHasProfile userHasProfile) {
        try {         
            if (userHasProfile.getEnabled() == true) {
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
        btnSave.setVisible(false);
        textUser.setReadonly(true);
        textRole.setReadonly(true);
        cmbUser.setDisabled(true);
        cmbRole.setDisabled(true);
    }
    
    public Boolean validateEmpty() {
        if (cmbUser.getSelectedItem() == null) {
            cmbUser.setFocus(true);
            this.showMessage("cms.error.field.cannotNull.cmbUser", true, null);
        } else if (cmbRole.getSelectedItem() == null) {
            cmbRole.setFocus(true);
            this.showMessage("cms.error.field.cannotNull.cmbRole", true, null);
        } else if ((!rEnabledYes.isChecked()) && (!rEnabledNo.isChecked())) {
            this.showMessage("cms.error.field.cannotNull.rEnabled", true, null);
        } else {
            return true;
        }
        return false;
    }

    public Boolean validateUserHasProfile(UserHasProfile userHasProfile) {
        List<UserHasProfile> userHasProfileList;
        try {
            userHasProfileList= (List<UserHasProfile>) utilsEJB.getUserHasProfileByUser(userHasProfile);
            boolean isEmpty = userHasProfileList.isEmpty();            
            if (isEmpty) {
                return true;
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(AdminUserHasProfileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(AdminUserHasProfileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(AdminUserHasProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void onClick$btnCodes() {
        Executions.getCurrent().sendRedirect("/docs/T-SP-E.164D-2009-PDF-S.pdf", "_blank");
    }

    public void onClick$btnShortNames() {
        Executions.getCurrent().sendRedirect("/docs/countries-abbreviation.pdf", "_blank");
    }

    private void saveUserHasProfile(UserHasProfile _userHasProfile) {
        try {
            UserHasProfile userHasProfile = null;
            boolean indEnabled = true;


            if (_userHasProfile != null) {
                userHasProfile = _userHasProfile;
                userHasProfile.setUpdateDate(new Timestamp(new Date().getTime()));
            } else {
                userHasProfile = new UserHasProfile();
                userHasProfile.setCreateDate(new Timestamp(new Date().getTime()));
                userHasProfile.setUpdateDate(new Timestamp(new Date().getTime()));
            }
            
            if (rEnabledYes.isChecked()) {
                indEnabled = true;
            } else {
                indEnabled = false;
            }
            
            userHasProfile.setUserId((User) cmbUser.getSelectedItem().getValue());
            userHasProfile.setProfileId((Profile) cmbRole.getSelectedItem().getValue());
            userHasProfile.setEnabled(indEnabled); 
            
            if (!validateUserHasProfile(userHasProfile) && (eventType == WebConstants.EVENT_ADD)) {
                this.showMessage("cms.error.userHasProfileExistInBD", true, null);
            } else {
                userHasProfile = utilsEJB.saveUserHasProfile(userHasProfile);
                UserHasProfileParam = userHasProfile;
                this.showMessage("sp.common.save.success", false, null);
                btnSave.setVisible(false);
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveUserHasProfile(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveUserHasProfile(UserHasProfileParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(UserHasProfileParam);
                loadCmbUser(eventType);
                loadCmbRole(eventType);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(UserHasProfileParam);
                loadCmbUser(eventType);
                loadCmbRole(eventType);
                blockFields();
                break;
            case WebConstants.EVENT_ADD:
                loadCmbUser(eventType);
                loadCmbRole(eventType);
                break;
            default:
                break;
        }
    }

    private void loadCmbRole(Integer evenInteger) {
        EJBRequest request1 = new EJBRequest();
        List<Profile> profile;
        try {
            profile = utilsEJB.getProfile(request1);
            loadGenericCombobox(profile, cmbRole, "name", evenInteger, Long.valueOf(UserHasProfileParam != null ? UserHasProfileParam.getProfileId().getId() : 0));
        } catch (EmptyListException ex) {
            showError(ex);
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        }
    }
    
    private void loadCmbUser(Integer evenInteger) {
        EJBRequest request1 = new EJBRequest();
        StringBuilder userName;
        List<User> users;
        try {
            users = personEJB.getUser(request1);
            for (int i = 0; i < users.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(users.get(i));
                userName = new StringBuilder(users.get(i).getFirstNames());
                userName.append(" ");
                userName.append(users.get(i).getLastNames());
                item.setLabel(userName.toString());
                item.setParent(cmbUser);
                if (UserHasProfileParam != null && users.get(i).getId().equals(UserHasProfileParam.getUserId().getId())) {
                    cmbUser.setSelectedItem(item);
                }
            }
            if (evenInteger.equals(WebConstants.EVENT_VIEW)) {
                cmbUser.setDisabled(true);
            }            
        } catch (EmptyListException ex) {
            showError(ex);
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        }
    }
}
