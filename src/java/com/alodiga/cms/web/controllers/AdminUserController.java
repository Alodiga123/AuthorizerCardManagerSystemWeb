package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.commons.ejb.UserEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.commons.exception.RegisterNotFoundException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.models.ComercialAgency;
import com.cms.commons.models.Country;
import com.cms.commons.models.DocumentsPersonType;
import com.cms.commons.models.Employee;
import com.cms.commons.models.Person;
import com.cms.commons.models.PersonClassification;
import com.cms.commons.models.PhonePerson;
import com.cms.commons.models.User;
import com.cms.commons.util.Constants;
import com.cms.commons.util.QueryConstants;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Toolbarbutton;

public class AdminUserController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEmployee;
    private Label lblPosition;
    private Label lblUserExtAlodiga;
    private Label lblCountry;
    private Label lblIdentificationType;    
    private Label lblIdentificationNumber;
    private Label lblComercialAgency;
    private Label lblCityEmployee;
    private Label lblEmailEmployee;
    private Combobox cmbAuthorizeEmployee;
    private Label lblAuthorizeExtAlodiga;
    private Textbox txtLogin;
    private Textbox txtPassword;
    private Radio rEnabledYes;
    private Radio rEnabledNo;
    private PersonEJB personEJB = null;
    private UtilsEJB utilsEJB = null;
    private UserEJB userEJB = null;
    private User userParam;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;
    private List<PhonePerson> phonePersonUserList = null;
    Employee employee = null;
    List<User> userEmployeeList = new ArrayList<User>();
    List<User> userLoginList = new ArrayList<User>();
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        userParam = (Sessions.getCurrent().getAttribute("object") != null) ? (User) Sessions.getCurrent().getAttribute("object") : null;
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
            userParam = null;
        } else {
            userParam = (User) Sessions.getCurrent().getAttribute("object");
        }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.user.edit"));
                break;
            case WebConstants.EVENT_VIEW:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.user.view"));
                break;
            default:
                break;
        }
        try {
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {

    }

    private void loadFields(User user) {
        PhonePerson phonePersonUser = null;
        List<PhonePerson> phonePersonEmployeeAuthorizeList = null;
        PhonePerson phonePersonEmployeeAuthorize = null;
        try {
            txtLogin.setText(user.getLogin());
            txtPassword.setText(user.getPassword());
            lblPosition.setValue(user.getEmployeeId().getEmployedPositionId().getName());
            lblPosition.setValue(user.getEmployeeId().getEmployedPositionId().getName());
            lblIdentificationType.setValue(user.getEmployeeId().getDocumentsPersonTypeId().getDescription());
            lblIdentificationNumber.setValue(String.valueOf(user.getEmployeeId().getIdentificationNumber()));
            lblCountry.setValue(user.getEmployeeId().getPersonId().getCountryId().getName());
            if (user.getEmployeeId().getComercialAgencyId().getCityId() != null) {
                lblCityEmployee.setValue(user.getEmployeeId().getComercialAgencyId().getCityId().getName());
            } else {
                EJBRequest request = new EJBRequest();
                request.setParam(user.getEmployeeId().getComercialAgencyId().getId());
                ComercialAgency comercialAgency = userEJB.loadComercialAgency(request);
                user.getEmployeeId().getComercialAgencyId().setCityId(comercialAgency.getCityId());
                lblCityEmployee.setValue(user.getEmployeeId().getComercialAgencyId().getCityId().getName());
            }            
            lblEmailEmployee.setValue(user.getEmployeeId().getPersonId().getEmail());
            if (user.getEmployeeId() != null) {
                EJBRequest request = new EJBRequest();
                HashMap params = new HashMap();
                params.put(Constants.PERSON_KEY, user.getEmployeeId().getPersonId().getId());
                request.setParams(params);
                phonePersonUserList = personEJB.getPhoneByPerson(request);
                for (PhonePerson phoneUser : phonePersonUserList) {
                    phonePersonUser = phoneUser;
                }
                lblUserExtAlodiga.setValue(phonePersonUser.getExtensionPhoneNumber());
            }
            if (user.getAuthorizedEmployeeId() != null) {
                EJBRequest request = new EJBRequest();
                HashMap params = new HashMap();
                params.put(Constants.PERSON_KEY, user.getAuthorizedEmployeeId().getPersonId().getId());
                request.setParams(params);
                phonePersonEmployeeAuthorizeList = personEJB.getPhoneByPerson(request);
                for (PhonePerson phoneEmployeeAuthorize : phonePersonEmployeeAuthorizeList) {
                    phonePersonEmployeeAuthorize = phoneEmployeeAuthorize;
                }
                lblAuthorizeExtAlodiga.setValue(phonePersonEmployeeAuthorize.getExtensionPhoneNumber());
            }
            if (user.getEnabled() == true) {
                rEnabledYes.setChecked(true);
            } else {
                rEnabledNo.setChecked(true);
            }
            btnSave.setVisible(true);
            cmbEmployee.setDisabled(true);
            cmbAuthorizeEmployee.setDisabled(true);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void blockFields() {
        cmbEmployee.setDisabled(true);
        cmbAuthorizeEmployee.setDisabled(true);
        txtLogin.setReadonly(true);
        txtPassword.setReadonly(true);
        rEnabledYes.setDisabled(true);
        rEnabledNo.setDisabled(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (cmbEmployee.getSelectedItem() == null) {
            cmbEmployee.setFocus(true);
            this.showMessage("cms.error.employee.noSelected", true, null);
        } else if (cmbAuthorizeEmployee.getSelectedItem() == null) {
            cmbAuthorizeEmployee.setFocus(true);
            this.showMessage("cms.error.authorizeEmployee.noSelected", true, null);
        } else if (txtLogin.getText().isEmpty()) {
            txtLogin.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPassword.getText().isEmpty()) {
            txtPassword.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if ((!rEnabledYes.isChecked()) && (!rEnabledNo.isChecked())) {
            this.showMessage("cms.error.field.enabled", true, null);
        } else {
            return true;
        }
        return false;
    }
    
    public boolean validateUser() {
        userEmployeeList.clear();
        userLoginList.clear();
        try {
            //Valida si el empleado ya tiene usuario
            EJBRequest request1 = new EJBRequest();
            Map params = new HashMap();
            params.put(Constants.PARAM_EMPLOYEE, employee.getId());
            request1.setParams(params);
            userEmployeeList = personEJB.getValidateEmployee(request1);
        } catch (Exception ex) {
            showError(ex);
        } finally {
            if (userEmployeeList.size() > 0) {
                this.showMessage("cms.error.field.employeeExistInBD", true, null);
                cmbEmployee.setFocus(true);
                return false;
            }
            try {
                //Valida si el login ingresado ya existe en BD
                EJBRequest request1 = new EJBRequest();
                Map params = new HashMap();
                params.put(Constants.PARAM_USER, txtLogin.getValue());
                request1.setParams(params);
                userLoginList = personEJB.getUserByLogin(request1);
            } catch (Exception ex) {
                showError(ex);
            } finally {
                if (userLoginList.size() > 0) {
                    this.showMessage("cms.error.field.loginExistInBD", true, null);
                    txtLogin.setFocus(true);
                    return false;
                }
            }
        }       
        return true;
    }
      
    public void onChange$cmbEmployee() {
        PhonePerson phonePersonEmployee = null;
        try {
            employee = (Employee) cmbEmployee.getSelectedItem().getValue();
            lblPosition.setValue(employee.getEmployedPositionId().getName());
            lblIdentificationType.setValue(employee.getDocumentsPersonTypeId().getDescription());
            lblIdentificationNumber.setValue(String.valueOf(employee.getIdentificationNumber()));
            lblCountry.setValue(employee.getPersonId().getCountryId().getName());
            lblComercialAgency.setValue(employee.getComercialAgencyId().getName());
            lblCityEmployee.setValue(employee.getComercialAgencyId().getCityId().getName());
            lblEmailEmployee.setValue(employee.getPersonId().getEmail());
            if (employee.getPersonId().getPhonePerson() != null) {
                lblUserExtAlodiga.setValue(employee.getPersonId().getPhonePerson().getExtensionPhoneNumber());
            } else {
                EJBRequest request = new EJBRequest();
                HashMap params = new HashMap();
                params.put(Constants.PERSON_KEY, employee.getPersonId().getId());
                request.setParams(params);
                phonePersonUserList = personEJB.getPhoneByPerson(request);
                for (PhonePerson phoneUser : phonePersonUserList) {
                    phonePersonEmployee = phoneUser;
                }
                lblUserExtAlodiga.setValue(phonePersonEmployee.getExtensionPhoneNumber());
            }
        } catch (Exception ex) {
            showError(ex);
        }
        
    }

    public void onChange$cmbAuthorizeEmployee() {
        this.clearMessage();
        Employee employeeAuthorize = (Employee) cmbAuthorizeEmployee.getSelectedItem().getValue();
        String nameEmployeeAuthorize = employeeAuthorize.getFirstNames() + " " + employeeAuthorize.getLastNames();
        String nameEmployee = employee.getFirstNames() + " " + employee.getLastNames();
        if (nameEmployeeAuthorize.equals(nameEmployee)) {
            cmbAuthorizeEmployee.setValue("");
            lblAuthorizeExtAlodiga.setValue("");
            this.showMessage("cms.msj.error.EmployeeAuthorizeNotEqualToEmployeeUser", true, null);
            cmbAuthorizeEmployee.setFocus(true);           
        } else {
            if (employeeAuthorize.getPersonId().getPhonePerson() != null) {
                lblAuthorizeExtAlodiga.setValue(employeeAuthorize.getPersonId().getPhonePerson().getNumberPhone());
            } else {
                lblAuthorizeExtAlodiga.setValue("");
            }
        }        
    }

    private void saveUser(User _user) throws RegisterNotFoundException, NullParameterException, GeneralException {
        boolean indEnabled = true;
        try {
            User user = null;
            Person person = null;

            if (_user != null) {
                user = _user;
                person = user.getPersonId();
            } else {
                user = new User();
                person = new Person();
            }

            if (rEnabledYes.isChecked()) {
                indEnabled = true;
            } else {
                indEnabled = false;
            }

            //Obtener la clasificacion del Empleado / Usuario
            EJBRequest request1 = new EJBRequest();
            request1.setParam(Constants.CLASSIFICATION_PERSON_USER);
            PersonClassification personClassification = utilsEJB.loadPersonClassification(request1);

            //Guardar la persona
            person.setCountryId(employee.getPersonId().getCountryId());
            person.setPersonTypeId(employee.getDocumentsPersonTypeId().getPersonTypeId());
            person.setEmail(lblEmailEmployee.getValue().toString());
            person.setCreateDate(new Timestamp(new Date().getTime()));
            person.setPersonClassificationId(personClassification);
            person = personEJB.savePerson(person);

            //Guarda el Usuario
            user.setLogin(txtLogin.getText());
            user.setPassword(txtPassword.getText());
            user.setPersonId(person);
            user.setDocumentsPersonTypeId(employee.getDocumentsPersonTypeId());
            user.setIdentificationNumber(lblIdentificationNumber.getValue());
            user.setCode(lblIdentificationNumber.getValue());
            user.setFirstNames(employee.getFirstNames());
            user.setLastNames(employee.getLastNames());
            user.setEmployeeId((Employee) cmbEmployee.getSelectedItem().getValue());
            user.setComercialAgencyId(employee.getComercialAgencyId());
            user.setAuthorizedEmployeeId((Employee) cmbAuthorizeEmployee.getSelectedItem().getValue());
            user.setEnabled(indEnabled);
            user = personEJB.saveUser(user);
            userParam = user;
            this.showMessage("sp.common.save.success", false, null);
            btnSave.setVisible(false);
        } catch (WrongValueException ex) {
            showError(ex);
        }
    }

    public void onClick$btnSave() throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    if (validateUser()) {
                        saveUser(null);
                    } 
                break;
                case WebConstants.EVENT_EDIT:
                    saveUser(userParam);                  
                break;
            }
        }
    }

    public void onclick$btnBack() {
        Executions.getCurrent().sendRedirect("listUser.zul");
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(userParam);
                txtLogin.setReadonly(true);
                txtPassword.setReadonly(true);
                loadCmbEmployee(eventType);
                loadCmbAuthorizeEmployee(eventType);
                onChange$cmbEmployee();
                onChange$cmbAuthorizeEmployee();
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(userParam);
                loadCmbEmployee(eventType);
                loadCmbAuthorizeEmployee(eventType);
                blockFields();
                onChange$cmbEmployee();
                onChange$cmbAuthorizeEmployee();
                break;
            case WebConstants.EVENT_ADD:
                loadCmbEmployee(eventType);
                loadCmbAuthorizeEmployee(eventType);
                break;
            default:
                break;
        }
    }

    private void loadCmbEmployee(Integer evenInteger) {
        EJBRequest request1 = new EJBRequest();
        List<Employee> employeeList;
        String nameEmployee = "";
        try {
            employeeList = personEJB.getEmployee(request1);
            for (int i = 0; i < employeeList.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(employeeList.get(i));
                nameEmployee = employeeList.get(i).getFirstNames() + " " + employeeList.get(i).getLastNames();
                item.setLabel(nameEmployee);
                item.setParent(cmbEmployee);
                if (eventType != 1) {
                    if (employeeList.get(i).getId().equals(userParam.getEmployeeId().getId())) {
                        cmbEmployee.setSelectedItem(item);
                    }
                }
            }
        } catch (EmptyListException ex) {
            showError(ex);
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        }
    }

    private void loadCmbAuthorizeEmployee(Integer evenInteger) {
        EJBRequest request1 = new EJBRequest();
        List<Employee> employeeList;
        String nameEmployee = "";
        try {
            employeeList = personEJB.getEmployee(request1);
            for (int i = 0; i < employeeList.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(employeeList.get(i));
                nameEmployee = employeeList.get(i).getFirstNames() + " " + employeeList.get(i).getLastNames();
                item.setLabel(nameEmployee);
                item.setParent(cmbAuthorizeEmployee);
                if (eventType != 1) {
                    if (employeeList.get(i).getId().equals(userParam.getAuthorizedEmployeeId().getId())) {
                        cmbAuthorizeEmployee.setSelectedItem(item);
                    }
                }
            }
        } catch (EmptyListException ex) {
            showError(ex);
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        }
    }

    private Object getSelectedItem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
