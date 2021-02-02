package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.AccessControlEJB;
import com.alodiga.cms.commons.exception.DisabledUserException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.InvalidPasswordException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.commons.exception.RegisterNotFoundException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractController;
import com.cms.commons.models.User;
import com.cms.commons.util.Constants;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Textbox;

public class IndexAdminController_ extends GenericAbstractController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtLogin;
    private Textbox txtRecoverLogin;
    private Textbox txtPassword;
    private Groupbox gbxLogin;
    private Groupbox gbxRecoverPass;
    private AccessControlEJB accessControlEJB = null;
    private String adminHome = "home-admin.zul";

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void clearFields() {
        txtLogin.setRawValue(null);
        txtPassword.setRawValue(null);
    }

    public Boolean validateEmpty() {
        if (txtLogin.getText().isEmpty()) {
            txtLogin.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPassword.getText().isEmpty()) {
            txtPassword.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            return true;
        }
        return false;

    }

    public boolean validateRecoverLogin() {

        if (txtRecoverLogin.getText().isEmpty()) {
            this.showMessage("sp.error.field.cannotNull", true, null);
            txtRecoverLogin.setFocus(true);
        } else {
            return true;
        }
        return false;
    }

    public boolean validate() {
        return true;
    }

    public void onClick$btnLogin() throws InterruptedException {
        this.clearMessage();
        if (validate()) {
            try {
                accessControlEJB = (AccessControlEJB) EJBServiceLocator.getInstance().get(EjbConstants.ACCESS_CONTROL_EJB);
            } catch (Exception ex) {
                ex.printStackTrace();
                this.showMessage("error.general", true, null);
            }
            try {
                User user = new User();
                user = accessControlEJB.validateUser(txtLogin.getText(), txtPassword.getText());
                session.setAttribute(Constants.USER_OBJ_SESSION, user);
                Executions.sendRedirect(Constants.HOME_ADMIN);
            } catch (RegisterNotFoundException ex) {
                this.showMessage("login.cms.user.not.found", true, null);
            } catch (DisabledUserException ex) {
                this.showMessage("login.cms.user.disabled", true, null);
            } catch (NullParameterException ex) {
                this.showMessage("cms.msj.error.general", true, null);
            } catch (GeneralException ex) {
                this.showMessage("cms.msj.error.general", true, null);
            } catch (InvalidPasswordException ex) {
                this.showMessage("login.cms.user.invalid.password", true, null);
            }
        }
    }

    public void onOK$txtLogin() throws InterruptedException {
        this.clearMessage();
        if (validate()) {
            onClick$btnLogin();
        }
    }

    public void onOK$txtPassword() throws InterruptedException {
        this.clearMessage();
        onClick$btnLogin();
    }

    public void onClick$btnRecoverPassword() throws InterruptedException {
        this.clearMessage();
        gbxLogin.setVisible(false);
        gbxRecoverPass.setVisible(true);
    }

    public void onClick$btnCancelRecoverPassword() throws InterruptedException {
        this.clearMessage();
        gbxLogin.setVisible(true);
        gbxRecoverPass.setVisible(false);
    }

    public void onClick$btnRecoverPass() throws InterruptedException {

    }

}
