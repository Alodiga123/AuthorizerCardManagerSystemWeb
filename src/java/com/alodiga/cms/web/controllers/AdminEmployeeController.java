package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.ejb.UserEJB;
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
import com.cms.commons.models.EmployedPosition;
import com.cms.commons.models.Employee;
import com.cms.commons.models.Person;
import com.cms.commons.models.PersonClassification;
import com.cms.commons.models.PersonType;
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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Toolbarbutton;

public class AdminEmployeeController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbCountry;
    private Combobox cmbDocumentPersonType;
    private Intbox indIdentification;
    private Tab tabEmployeePhone;
    private Textbox txtName;
    private Textbox txtLastName;
    private Textbox txtEmail;
    private Combobox cmbPositionEnterprise;
    private Combobox cmbComercialAgency;
    private PersonEJB personEJB = null;
    private UtilsEJB utilsEJB = null;
    private UserEJB userEJB = null;
    private Employee employeeParam = null;
    private Button btnSave;
    private Integer eventType;
    public static Employee employeeParent = null;
    private Toolbarbutton tbbTitle;
    private List<PhonePerson> phonePersonUserList = null;
    List<Employee> employeeList = new ArrayList<Employee>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        employeeParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Employee) Sessions.getCurrent().getAttribute("object") : null;
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
            employeeParam = null;
        } else {
            employeeParam = (Employee) Sessions.getCurrent().getAttribute("object");
        }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.employee.edit"));
                break;
            case WebConstants.EVENT_VIEW:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.employee.view"));
                break;
            case WebConstants.EVENT_ADD:
                 tabEmployeePhone.setDisabled(true);
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
    public void onChange$cmbCountry() {
        this.clearMessage();
        cmbDocumentPersonType.setVisible(true);
        cmbDocumentPersonType.setValue("");
        Country country = (Country) cmbCountry.getSelectedItem().getValue();
        loadCmbDocumentsPersonType(eventType, country.getId());
    }
    
    public void clearFields() {

    }
    
    public Employee getEmployeeParent() {
        return this.employeeParent;
    }

    private void loadFields(Employee employee) {
        
        try {
            indIdentification.setValue(employee.getIdentificationNumber());
            txtName.setText(employee.getFirstNames());
            txtLastName.setText(employee.getLastNames());
            txtEmail.setText(employee.getPersonId().getEmail());
            btnSave.setVisible(true);
        } catch (Exception ex) {
            showError(ex);
        }
 
    }

    public void blockFields() {
       indIdentification.setRawValue(null); 
       txtName.setRawValue(null);
       txtLastName.setRawValue(null);
       txtEmail.setRawValue(null);
       btnSave.setVisible(false);
       cmbCountry.setDisabled(true);
       cmbPositionEnterprise.setDisabled(true);
       cmbComercialAgency.setDisabled(true);
       cmbDocumentPersonType.setDisabled(true);
    }

    public Boolean validateEmpty() {
        if (cmbCountry.getSelectedItem()  == null) {
            cmbCountry.setFocus(true);
            this.showMessage("cms.error.country.notSelected", true, null);     
        } else if (cmbDocumentPersonType.getSelectedItem() == null) {
            cmbDocumentPersonType.setFocus(true);
            this.showMessage("cms.error.documentType.notSelected", true, null);
        } else if(indIdentification.getText().isEmpty()){
             indIdentification.setFocus(true);
             this.showMessage("cms.error.employee.identification",true, null);
        } else if(txtName.getText().isEmpty()){
            txtName.setFocus(true);
            this.showMessage("cms.error.field.fullName",true, null);
        } else if(txtLastName.getText().isEmpty()){
            txtLastName.setFocus(true);
            this.showMessage("cms.error.field.lastName",true, null);
        } else if(txtEmail.getText().isEmpty()){
            txtEmail.setFocus(true);
            this.showMessage("cms.error.field.email",true, null);
        } else if (cmbPositionEnterprise.getSelectedItem() == null) {
            cmbPositionEnterprise.setFocus(true);
            this.showMessage("cms.error.employee.positionEnterprise", true, null);
        } else if (cmbComercialAgency.getSelectedItem() == null) {
            cmbComercialAgency.setFocus(true);
            this.showMessage("cms.error.comercialAgency.noSelected", true, null);
        } else {
            return true;
        }
        return false;

    }
   
    private void saveEmployee(Employee _employee) throws RegisterNotFoundException, NullParameterException, GeneralException {
       
        try {
            Employee employee = null;
            Person person = null;

            if (_employee != null) {
                employee = _employee;
                person = employee.getPersonId();
            } else {
                employee = new Employee();
                person = new Person();
            }
            
            //Obtener la clasificacion del Empleado
            EJBRequest request1 = new EJBRequest();
            request1.setParam(Constants.CLASSIFICATION_PERSON_EMPLOYEE);
            PersonClassification personClassification = utilsEJB.loadPersonClassification(request1);
            
            //Guardar la persona
            person.setCountryId((Country) cmbCountry.getSelectedItem().getValue());
            person.setPersonTypeId(((DocumentsPersonType) cmbDocumentPersonType.getSelectedItem().getValue()).getPersonTypeId());
            person.setEmail(txtEmail.getText());
            person.setPersonClassificationId(personClassification);
            person.setCreateDate(new Timestamp(new Date().getTime()));
            person = personEJB.savePerson(person);
            
            //Guardar el empleado
            employee.setPersonId(person);
            employee.setIdentificationNumber(indIdentification.getValue());
            employee.setFirstNames(txtName.getText());
            employee.setLastNames(txtLastName.getText());
            employee.setDocumentsPersonTypeId((DocumentsPersonType) cmbDocumentPersonType.getSelectedItem().getValue());
            employee.setComercialAgencyId((ComercialAgency) cmbComercialAgency.getSelectedItem().getValue());
            employee.setEmployedPositionId((EmployedPosition) cmbPositionEnterprise.getSelectedItem().getValue()) ;
            employee = personEJB.saveEmployee(employee);
            employeeParent = employee;
            employeeParam = employee;
            tabEmployeePhone.setDisabled(false);
            this.showMessage("sp.common.save.success", false, null);
            
        } catch (WrongValueException ex) {
            showError(ex);
        }
    }
 

    public void onClick$btnSave() throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveEmployee(null);
                break;
                case WebConstants.EVENT_EDIT:
                    saveEmployee(employeeParam);
                break;
                default:
                break;
            }
        }
    }

    public void onclick$btnBack() {
        Executions.getCurrent().sendRedirect("listEmployee.zul");
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                employeeParent = employeeParam;
                loadFields(employeeParam);
                loadCmbContryId(eventType);
                onChange$cmbCountry();
                loadCmbComercialAgency(eventType);
                loadCmbPositionEnterprise(eventType);
                
                break;
            case WebConstants.EVENT_VIEW:
                employeeParent = employeeParam;
                loadFields(employeeParam);
                loadCmbContryId(eventType);
                onChange$cmbCountry();
                loadCmbComercialAgency(eventType);
                loadCmbPositionEnterprise(eventType);
                blockFields();
                
                break;
            case WebConstants.EVENT_ADD:
                loadCmbContryId(eventType);
                loadCmbComercialAgency(eventType);
                loadCmbPositionEnterprise(eventType);
                
                break;
            default:
                break;
        }
    }
    
    private void loadCmbContryId(Integer evenInteger) {
        //cmbCountry
        EJBRequest request1 = new EJBRequest();
        List<Country> countries;
        try {
            countries = utilsEJB.getCountries(request1);
            loadGenericCombobox(countries, cmbCountry, "name", evenInteger, Long.valueOf(employeeParam != null ? employeeParam.getPersonId().getCountryId().getId() : 0));
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
        
    private void loadCmbDocumentsPersonType(Integer evenInteger, int countryId) {
        EJBRequest request1 = new EJBRequest();
        cmbDocumentPersonType.getItems().clear();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_COUNTRY_ID, countryId);
        params.put(QueryConstants.PARAM_IND_NATURAL_PERSON, WebConstants.IND_NATURAL_PERSON);
        params.put(QueryConstants.PARAM_ORIGIN_APPLICATION_ID, Constants.ORIGIN_APPLICATION_CMS_ID);
        request1.setParams(params);
        List<DocumentsPersonType> documentsPersonType = null;
        try {
            documentsPersonType = utilsEJB.getDocumentsPersonByCountry(request1);
            loadGenericCombobox(documentsPersonType, cmbDocumentPersonType, "description", evenInteger, Long.valueOf(employeeParam != null ? employeeParam.getDocumentsPersonTypeId().getId() : 0));
        } catch (EmptyListException ex) {
            showError(ex);
            ex.printStackTrace();
        } catch (GeneralException ex) {
            showError(ex);
            ex.printStackTrace();
        } catch (NullParameterException ex) {
            showError(ex);
        } finally {
            if (documentsPersonType == null) {
                this.showMessage("cms.msj.DocumentsPersonTypeNull", false, null);
            }
        }
    }
    
    private void loadCmbComercialAgency(Integer evenInteger) {
        //Cargos de Nomina analista 1, analista 2, atencion al cliente 1 ,atencion al cliente 2, especialista seguridad 1 y 2
        //cmbComercialAgency
        
        EJBRequest request1 = new EJBRequest();
        List<ComercialAgency> comcercialAgency;
        try {
            comcercialAgency = userEJB.getComercialAgency(request1);
            loadGenericCombobox(comcercialAgency, cmbComercialAgency, "name", evenInteger, Long.valueOf(employeeParam != null ? employeeParam.getComercialAgencyId().getId() : 0));
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
    
    private void loadCmbPositionEnterprise(Integer evenInteger) {
        //Cargos de Nomina analista 1, analista 2, atencion al cliente 1 ,atencion al cliente 2, especialista seguridad 1 y 2
        //cmbPositionEnterprise
        //position en personejb
        EJBRequest request1 = new EJBRequest();
        List<EmployedPosition> employedPosition;
        try {
            employedPosition = personEJB.getEmployedPosition(request1);
            loadGenericCombobox(employedPosition, cmbPositionEnterprise, "name", evenInteger, Long.valueOf(employeeParam != null ? employeeParam.getEmployedPositionId().getId() : 0));
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
    
    private Object getSelectedItem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
