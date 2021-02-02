package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.PersonEJB;
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
import org.zkoss.zul.Textbox;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.models.ComercialAgency;
import com.cms.commons.models.DocumentsPersonType;
import com.cms.commons.models.Employee;
import com.cms.commons.models.PasswordChangeRequest;
import com.cms.commons.models.Person;
import com.cms.commons.models.PersonClassification;
import com.cms.commons.models.PhonePerson;
import com.cms.commons.models.Sequences;
import com.cms.commons.models.User;
import com.cms.commons.util.Constants;
import com.cms.commons.util.QueryConstants;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Toolbarbutton;

public class AdminPasswordChangeRequestController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtCurrentPassword;
    private Textbox txtNewPassword;
    private Textbox txtRepeatNewPassword;
    private Label lblRequestNumber;
    private Label lblRequestDate;
    private Label lblIdentificationNumber;
    private Label lblUser;
    private Label lblComercialAgency;
    private Radio rApprovedYes;
    private Radio rApprovedNo;
    private PersonEJB personEJB = null;
    private UtilsEJB utilsEJB = null;
    private PasswordChangeRequest passwordChangeRequestParam;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;
    private User user;
    private String numberRequest = "";
    private int attempts = 0;
    
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        passwordChangeRequestParam = (Sessions.getCurrent().getAttribute("object") != null) ? (PasswordChangeRequest) Sessions.getCurrent().getAttribute("object") : null;
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
           passwordChangeRequestParam = null;                    
       } else {
           passwordChangeRequestParam = (PasswordChangeRequest) Sessions.getCurrent().getAttribute("object");            
       }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_VIEW:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.password.change.request.view"));
                break;
            case WebConstants.EVENT_ADD:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.password.change.request.add"));
                rApprovedYes.setVisible(false);
                rApprovedNo.setVisible(false);
                break;
            default:
                break;
        }
        try {
            user = (User) session.getAttribute(Constants.USER_OBJ_SESSION);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$imgEye() {
        if (txtCurrentPassword.getType().equals("password")) {
            txtCurrentPassword.setType("text");
        } else {
            txtCurrentPassword.setType("password");
        }              
    }
    
    public void onClick$imgEye1() {
        if (txtNewPassword.getType().equals("password")) {
            txtNewPassword.setType("text");
        } else {
            txtNewPassword.setType("password");
        }              
    }
    
    public void onClick$imgEye2() {
        if (txtRepeatNewPassword.getType().equals("password")) {
            txtRepeatNewPassword.setType("text");
        } else {
            txtRepeatNewPassword.setType("password");
        }              
    }
    
    public void clearFields() {
        txtCurrentPassword.setRawValue(null);
        txtNewPassword.setRawValue(null);
        txtRepeatNewPassword.setRawValue(null);
        lblRequestNumber.setValue(null);
        lblRequestDate.setValue(null);
        lblIdentificationNumber.setValue(null);
        lblUser.setValue(null);
        lblComercialAgency.setValue(null);
    } 
    
    private void loadFields(PasswordChangeRequest passwordChangeRequest) {
        try {        
            lblRequestNumber.setValue(passwordChangeRequest.getRequestNumber().toString());
            lblRequestDate.setValue(passwordChangeRequest.getRequestDate().toString());
            lblIdentificationNumber.setValue(passwordChangeRequest.getUserId().getIdentificationNumber());
            lblUser.setValue(passwordChangeRequest.getUserId().getFirstNames());
            lblComercialAgency.setValue(passwordChangeRequest.getUserId().getComercialAgencyId().getName());
            txtCurrentPassword.setText(passwordChangeRequest.getCurrentPassword());
            txtNewPassword.setText(passwordChangeRequest.getNewPassword());
            txtRepeatNewPassword.setText(passwordChangeRequest.getNewPassword());
                  
              if (passwordChangeRequest.getCurrentPassword() != null ) {
                txtCurrentPassword.setValue(passwordChangeRequest.getCurrentPassword());
              }
              
              if (passwordChangeRequest.getCurrentPassword() == null) {
                txtCurrentPassword.setValue(passwordChangeRequest.getNewPassword());
              } 
                
            if (passwordChangeRequest.getIndApproved() == true) {
                rApprovedYes.setChecked(true);
             } else {
                rApprovedNo.setChecked(true);
            }
            btnSave.setVisible(true);
        
        } catch (Exception ex) {
            showError(ex);
        }
    }     

    public void blockFields() {
        txtCurrentPassword.setReadonly(true);
        txtNewPassword.setReadonly(true);
        txtRepeatNewPassword.setReadonly(true);
        btnSave.setVisible(false);
    
    }
    
    public Boolean validateEmpty() {
        if (txtCurrentPassword.getText().isEmpty()) {
            txtCurrentPassword.setFocus(true);
            this.showMessage("cms.error.field.cannotNull.txtCurrentPassword", true, null);
        } else if (txtNewPassword.getText().isEmpty()) {
            txtNewPassword.setFocus(true);
            this.showMessage("cms.error.field.cannotNull.txtNewPassword", true, null);
        } else if (txtRepeatNewPassword.getText().isEmpty()) {
            txtRepeatNewPassword.setFocus(true);
            this.showMessage("cms.error.field.cannotNull.txtRepeatNewPassword", true, null);
        } else {
            return true;
        }
        return false;
    }  
    
    public boolean validatePasswordChange() {
        //Valida que la confirmación de la nueva contraseña coincida con la nueva contraseña
        if (!txtRepeatNewPassword.getValue().equals(txtNewPassword.getValue())) {
            txtRepeatNewPassword.setValue("");
            txtRepeatNewPassword.setFocus(true);
            this.showMessage("cms.msj.fieldsPasswordNotEquals", true, null);
        } else if (txtNewPassword.getValue().equals(txtCurrentPassword.getValue())) {
            txtNewPassword.setValue("");
            txtRepeatNewPassword.setValue("");
            txtNewPassword.setFocus(true);
            this.showMessage("cms.msj.fieldsCurrentPasswordEqualsNewPassword", true, null);
        } else {
            return true;
        }
        return false;
    }
    
    private void savePasswordChangeRequest(PasswordChangeRequest _passwordChangeRequest) throws RegisterNotFoundException, NullParameterException, GeneralException, EmptyListException {
        boolean indApproved = true;
        EJBRequest request1 = new EJBRequest();
        Date dateRequest = null;
        List<User> userList = null;
        PasswordChangeRequest passwordChangeRequest = null;
        
        try {
            if (_passwordChangeRequest != null) {
                passwordChangeRequest = _passwordChangeRequest;
                dateRequest = passwordChangeRequest.getRequestDate();
            } else {
                passwordChangeRequest = new PasswordChangeRequest();
            }

            if (rApprovedYes.isChecked()) {
                indApproved = true;
            } else {
                indApproved = false;
            }
            
            //Valida si la contraseña actual es correcta
            Map params = new HashMap();
            params.put(Constants.CURRENT_PASSWORD, txtCurrentPassword.getValue());
            params.put(Constants.USER_KEY,user.getId());
            request1.setParams(params);
            userList = personEJB.validatePassword(request1); 
            
            if (userList.size() > 0) {
                //Obtiene el numero de secuencia para Solicitud de Cambio de Contraseña
                numberRequest = generateNumberSequence();
                dateRequest = new Date();
                lblRequestNumber.setValue(numberRequest);
                lblRequestDate.setValue(dateRequest.toString());
                
                //Se aprueba la solicitud automaticamente
                indApproved = true;                
                //Se crea el objeto passwordChangeRequest
                createPasswordChangeRequest(passwordChangeRequest, numberRequest, dateRequest, indApproved); 
                
                //Guardar la solicitud de cambio de contraseña en la BD
                passwordChangeRequest = personEJB.savePasswordChangeRequest(passwordChangeRequest);
                passwordChangeRequestParam = passwordChangeRequest;

                //Actualizar la contraseña del usuario en la BD
                user.setPassword(txtNewPassword.getText());
                user = personEJB.saveUser(user);
                
                rApprovedYes.setVisible(true);
                rApprovedNo.setVisible(true);                
                if (passwordChangeRequest.getIndApproved() == true) {
                    rApprovedYes.setChecked(true);
                } else {
                    rApprovedNo.setChecked(true);
                }                
                rApprovedYes.setDisabled(true);
                rApprovedNo.setDisabled(true);               
                
                this.showMessage("cms.msj.passwordChangedRequestApproved", false, null);
                btnSave.setVisible(false);
            }             
        } catch (WrongValueException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            showError(ex);
        } finally {
            if (userList == null) {
                attempts++;
                if (attempts != 3) {
                    this.showMessage("cms.msj.errorCurrentPasswordNotMatchInBD", true, null);
                    txtCurrentPassword.setText("");
                    txtNewPassword.setText("");
                    txtRepeatNewPassword.setText("");
                }
            }
            if (attempts == 3) {
                //Obtiene el numero de secuencia para Solicitud de Cambio de Contraseña
                numberRequest = generateNumberSequence();
                dateRequest = new Date();
                lblRequestNumber.setValue(numberRequest);
                lblRequestDate.setValue(dateRequest.toString());
                
                //Se rechaza la solicitud automaticamente
                indApproved = false;
                //Se crea el objeto passwordChangeRequest
                createPasswordChangeRequest(passwordChangeRequest, numberRequest, dateRequest, indApproved); 

                //Guardar la solicitud de cambio de contraseña en la BD
                passwordChangeRequest = personEJB.savePasswordChangeRequest(passwordChangeRequest);
                passwordChangeRequestParam = passwordChangeRequest;                
                
                rApprovedYes.setVisible(true);
                rApprovedNo.setVisible(true);                
                if (passwordChangeRequest.getIndApproved() == true) {
                    rApprovedYes.setChecked(true);
                } else {
                    rApprovedNo.setChecked(true);
                }                
                rApprovedYes.setDisabled(true);
                rApprovedNo.setDisabled(true);

                this.showMessage("cms.msj.passwordChangedRequestRejected", true, null);
                btnSave.setVisible(false);
            }
                    
        }
    } 
    
    public String generateNumberSequence() {
        try {
            EJBRequest request1 = new EJBRequest();
            Map params = new HashMap();
            params.put(Constants.DOCUMENT_TYPE_KEY, Constants.DOCUMENT_TYPE_CHANGE_PASSWORD_REQUEST);
            request1.setParams(params);
            List<Sequences> sequence = utilsEJB.getSequencesByDocumentType(request1);
            numberRequest = utilsEJB.generateNumberSequence(sequence, Constants.ORIGIN_APPLICATION_CMS_ID);
        } catch (EmptyListException ex) {
            showError(ex);
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (RegisterNotFoundException ex) {
            showError(ex);
        }        
        return numberRequest;
    }
    
    public void createPasswordChangeRequest(PasswordChangeRequest passwordChangeRequest, String numberRequest, Date requestDate, boolean indApproved) {
        passwordChangeRequest.setRequestNumber(numberRequest);
        passwordChangeRequest.setRequestDate(requestDate);
        passwordChangeRequest.setUserId(user);
        passwordChangeRequest.setCurrentPassword(txtCurrentPassword.getText());
        passwordChangeRequest.setNewPassword(txtNewPassword.getText());
        passwordChangeRequest.setNewPassword(txtRepeatNewPassword.getText());
        passwordChangeRequest.setIndApproved(indApproved);
        passwordChangeRequest.setCreateDate(new Timestamp(new Date().getTime()));
    }
    
    public void onClick$btnSave() throws RegisterNotFoundException, NullParameterException, GeneralException, EmptyListException {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    if (validatePasswordChange()) {
                        savePasswordChangeRequest(null);
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    public void onclick$btnBack() {
        Executions.getCurrent().sendRedirect("listPasswordChangeRequest.zul");
    }
    
    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_VIEW:
                loadFields(passwordChangeRequestParam);
                txtCurrentPassword.setReadonly(true);
                txtNewPassword.setReadonly(true);
                txtRepeatNewPassword.setDisabled(false);
                blockFields();
                rApprovedYes.setDisabled(true);
                rApprovedNo.setDisabled(true);
                break;
            case WebConstants.EVENT_ADD:
                lblComercialAgency.setValue(user.getComercialAgencyId().getName());
                lblUser.setValue(user.getFirstNames() + " " + user.getLastNames());
                lblIdentificationNumber.setValue(user.getIdentificationNumber());
                break;
            default:
                break;
        }

      }
     
    private Object getSelectedItem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  }
