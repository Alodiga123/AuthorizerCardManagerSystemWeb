package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.commons.exception.RegisterNotFoundException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Country;
import com.cms.commons.models.DocumentsPersonType;
import com.cms.commons.models.PhoneType;
import com.cms.commons.models.PhonePerson;
import com.cms.commons.models.Person;
import com.cms.commons.models.Employee;
import com.cms.commons.util.Constants;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import com.cms.commons.util.QueryConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Radio;

public class AdminEmployeePhoneController extends GenericAbstractAdminController {

            
    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtPhone;
    private Label txtCodeCountry;
    private Textbox txtAreaCode;
    private Textbox txtPhoneExtension;
    private PersonEJB personEJB = null;
    private UtilsEJB utilsEJB = null;
    private Combobox cmbCountry; 
    private Combobox cmbPhoneType;
    private Radio rIsPrincipalNumberYes;
    private Radio rIsPrincipalNumberNo;
    private PhonePerson phonePersonParam;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;
    public Window winAdminPhoneEmployee;
    List<PhonePerson> phonePersonList = new ArrayList<PhonePerson>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);        
        eventType = (Integer) Sessions.getCurrent().getAttribute( WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
           phonePersonParam = null;                    
       } else {
           phonePersonParam = (PhonePerson) Sessions.getCurrent().getAttribute("object");            
       }
        initialize();
    }
    
    @Override
    public void initialize() {
        super.initialize();
        try {
            personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
            utilsEJB  = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onChange$cmbCountry() {
        this.clearMessage();        
        txtCodeCountry.setVisible(true);
        txtCodeCountry.setValue("");        
        Country country = (Country) cmbCountry.getSelectedItem().getValue();
        txtCodeCountry.setValue(country.getCode());
    }
    

    public void clearFields() {
        txtPhone.setRawValue(null);;

    }

    private void loadFields(PhonePerson phonePerson) {
        try {
            txtPhone.setText(phonePerson.getNumberPhone());
            txtCodeCountry.setValue(phonePerson.getCountryCode());
            txtAreaCode.setText(phonePerson.getAreaCode());
            txtPhoneExtension.setText(phonePerson.getExtensionPhoneNumber()); 
            if (phonePerson.getIndMainPhone() == true) {
                rIsPrincipalNumberYes.setChecked(true);
            } else {
                rIsPrincipalNumberNo.setChecked(true);
            }
            btnSave.setVisible(true);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void blockFields() {
        txtPhone.setReadonly(true);
        txtAreaCode.setReadonly(true);
        txtPhoneExtension.setReadonly(true);
        rIsPrincipalNumberYes.setDisabled(true);
        rIsPrincipalNumberNo.setDisabled(true);
        btnSave.setVisible(false);
    }
    
    public void onClick$btnBack() {
        winAdminPhoneEmployee.detach();
    }

    public Boolean validateEmpty() {
        if (cmbCountry.getSelectedItem()  == null) {
            cmbCountry.setFocus(true);
            this.showMessage("cms.error.country.notSelected", true, null);     
        } else if (txtAreaCode.getText().isEmpty()) {
            txtAreaCode.setFocus(true);
            this.showMessage("cms.error.employee.areaCode", true, null);
        } else if (txtPhone.getText().isEmpty()) {
            txtPhone.setFocus(true);
            this.showMessage("cms.error.field.phoneNumber", true, null);
        } else if (txtPhoneExtension.getText().isEmpty()) {
            txtPhoneExtension.setFocus(true);
            this.showMessage("cms.error.employee.extensionPhone", true, null);
        } else if (cmbPhoneType.getSelectedItem() == null) {
            cmbPhoneType.setFocus(true);
            this.showMessage("cms.error.phoneType.notSelected", true, null);
        }  else {
            return true;
        }
        return false;

    }
    
    public boolean validatePhone() {
        Employee employee = null;
        try {    
            //Empleado Principal
            AdminEmployeeController adminEmployee = new AdminEmployeeController();
            if (adminEmployee.getEmployeeParent().getPersonId() != null) {
                employee = adminEmployee.getEmployeeParent();
            }
            EJBRequest request = new EJBRequest();
            Map params = new HashMap();
            params.put(Constants.PERSON_KEY, employee.getPersonId().getId());
            request.setParams(params);
            phonePersonList = personEJB.getValidateMainPhone(request);
            if ((phonePersonList != null) && (rIsPrincipalNumberYes.isChecked())){
                this.showMessage("cms.error.employee.PhoneMainYes", true, null);
                txtPhone.setFocus(true);
                return false;
            }
        } catch (Exception ex) {
            showError(ex);
        }        
        return true;
    }


    private void savePhone(PhonePerson _phonePerson) {
        Employee employee = null;
        boolean indPrincipalPhone  = true;
        try {
            PhonePerson phonePerson = null;

            if (_phonePerson != null) {

           phonePerson = _phonePerson;
            } else {
                phonePerson = new PhonePerson();
            }
            
            if (rIsPrincipalNumberYes.isChecked()) {
                indPrincipalPhone = true;
            } else {
                indPrincipalPhone = false;
            }
            //Obtener Person
             AdminEmployeeController adminEmployee = new AdminEmployeeController();
            if (adminEmployee.getEmployeeParent().getPersonId().getId() != null) {
                employee = adminEmployee.getEmployeeParent();
            }
            
            //Guardar telefono
            phonePerson.setPersonId(employee.getPersonId());
            phonePerson.setCountryId((Country) cmbCountry.getSelectedItem().getValue());
            phonePerson.setCountryCode(txtCodeCountry.getValue());
            phonePerson.setAreaCode(txtAreaCode.getText());
            phonePerson.setNumberPhone(txtPhone.getText());
            phonePerson.setExtensionPhoneNumber(txtPhoneExtension.getText());
            phonePerson.setIndMainPhone(indPrincipalPhone);
            phonePerson.setPhoneTypeId((PhoneType) cmbPhoneType.getSelectedItem().getValue());
            
            phonePerson = personEJB.savePhonePerson(phonePerson);
            this.showMessage("sp.common.save.success", false, null);
            EventQueues.lookup("updatePhonePerson", EventQueues.APPLICATION, true).publish(new Event(""));
            btnSave.setVisible(false);
            
            } catch (Exception ex) {
                 showError(ex);
            }

    }

    public void onClick$btnSave()throws RegisterNotFoundException, NullParameterException, GeneralException {
        this.clearMessage();
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                   if (validatePhone()){
                        savePhone(null);   
                   }                    
                    break;
                case WebConstants.EVENT_EDIT:
                    savePhone(phonePersonParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(phonePersonParam);
                loadcmbPhoneType(eventType);
                loadCmbCountry(eventType);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(phonePersonParam);
                txtPhone.setReadonly(true);
                blockFields();
                loadCmbCountry(eventType);
                loadcmbPhoneType(eventType);
                break;
            case WebConstants.EVENT_ADD:
                loadcmbPhoneType(eventType);
                loadCmbCountry(eventType);
                onChange$cmbCountry();
                break;
            default:
                break;
        }
    }
    
    private void loadcmbPhoneType(Integer evenInteger) {
        EJBRequest request1 = new EJBRequest();
        List<PhoneType> phoneTypes;
        try {
            phoneTypes = personEJB.getPhoneType(request1);
            loadGenericCombobox(phoneTypes,cmbPhoneType, "description",evenInteger,Long.valueOf(phonePersonParam != null? phonePersonParam.getPhoneTypeId().getId() : 0) );
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
    
    private void loadCmbCountry(Integer evenInteger) {
        EJBRequest request1 = new EJBRequest();
        List<Country> countryList;
        try {
            countryList = utilsEJB.getCountries(request1);
            loadGenericCombobox(countryList, cmbCountry, "name", evenInteger, Long.valueOf(phonePersonParam != null ? phonePersonParam.getCountryId().getId() : 0));
        } catch (EmptyListException ex) {
            showError(ex);
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        }
    }     
}

    
 

