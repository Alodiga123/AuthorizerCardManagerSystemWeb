package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Country;
import com.cms.commons.models.PhonePerson;
import com.cms.commons.models.PhoneType;
import com.cms.commons.models.Employee;
import com.cms.commons.models.Person;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class AdminPhoneEmployeeController extends GenericAbstractAdminController {

            
    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtPhone;
    private UtilsEJB utilsEJB = null;
    private PersonEJB personEJB = null;
    private Combobox cmbPhoneType;
    private PhonePerson phonePersonParam;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;
    public Window winAdminPhoneEmployee;

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
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.economicActivity.edit"));
                break;
            case WebConstants.EVENT_ADD:
                tbbTitle.setLabel(Labels.getLabel("cms.crud.economicActivity.add"));
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
        txtPhone.setRawValue(null);
    }

    private void loadFields(PhonePerson phonePerson) {
        try {
            txtPhone.setText(phonePerson.getNumberPhone());
            btnSave.setVisible(true);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void blockFields() {
        txtPhone.setReadonly(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
            return true;
    }


    private void savePhonePerson(PhonePerson _phonePerson) {
        try {
            PhonePerson phonePerson = null;

            if (_phonePerson != null) {

            phonePerson = _phonePerson;
            } else {
                phonePerson = new PhonePerson();
            }
            

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

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    savePhonePerson(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    savePhonePerson(phonePersonParam);
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
                loadCmbPhoneType(eventType);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(phonePersonParam);
                blockFields();
                loadCmbPhoneType(eventType);
                break;
            case WebConstants.EVENT_ADD:
                loadCmbPhoneType(eventType);
                break;
            default:
                break;
        }
    }
    
    private void loadCmbPhoneType(Integer evenInteger) {
        //cmbPhoneType 
        EJBRequest request1 = new EJBRequest();
        List<PhoneType> phoneTypes;
        try {
            phoneTypes = personEJB.getPhoneType(request1);
            loadGenericCombobox(phoneTypes, cmbPhoneType, "description", evenInteger, Long.valueOf(phonePersonParam != null ? phonePersonParam.getPhoneTypeId().getId() : 0));
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


}


        
