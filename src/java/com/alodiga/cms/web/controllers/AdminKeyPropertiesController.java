package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.ProgramEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.ejb.UserEJB;
import com.alodiga.cms.commons.ejb.ProductEJB;
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
import com.cms.commons.models.Channel;
import com.cms.commons.models.Country;
import com.cms.commons.models.KeyProperties;
import com.cms.commons.models.Product;
import com.cms.commons.models.Program;

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

public class AdminKeyPropertiesController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbCountry;
    private Combobox cmbProgram;
    private Combobox cmbProduct;
    private Combobox cmbChannel;
    private Label lblIssuer;
    private Label lblBinNumber;
    private Intbox intSegment;
    private Intbox intNumberTotal;
    private Intbox intDaysExpired;
    private Intbox intPreviusKeys;
    private Radio cFollowedYes;
    private Radio cFollowedNo;
    private Radio cCharactersYes;
    private Radio cCharactersNo;
    private Radio aChannelsYes;
    private Radio aChannelsNo;
    private UtilsEJB utilsEJB = null;
    private ProgramEJB programEJB = null;
    private ProductEJB productEJB = null;
    private KeyProperties keyPropertiesParam = null;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;
    List<KeyProperties> keyPropertiesList = new ArrayList<KeyProperties>();
    List<Channel> channelList = new ArrayList<Channel>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        keyPropertiesParam = (Sessions.getCurrent().getAttribute("object") != null) ? (KeyProperties) Sessions.getCurrent().getAttribute("object") : null;
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
            keyPropertiesParam = null;
        } else {
            keyPropertiesParam = (KeyProperties) Sessions.getCurrent().getAttribute("object");
        }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                tbbTitle.setLabel(Labels.getLabel("authorize.crud.keyProperties.edit"));
                break;
            case WebConstants.EVENT_VIEW:
                tbbTitle.setLabel(Labels.getLabel("authorize.crud.keyProperties.view"));
                break;
            case WebConstants.EVENT_ADD:
                tbbTitle.setLabel(Labels.getLabel("authorize.crud.keyProperties.add"));
                break;
            default:
                break;
        }
        try {
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            programEJB = (ProgramEJB) EJBServiceLocator.getInstance().get(EjbConstants.PROGRAM_EJB);
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }
    public void onChange$cmbCountry() {
        this.clearMessage();
        cmbProgram.setVisible(true);
        cmbProgram.setValue("");
        Country country = (Country) cmbCountry.getSelectedItem().getValue();
        loadCmbProgram(eventType, country.getId());
    }
    
    public void onChange$cmbProgram(){
       this.clearMessage();
       cmbProduct.setVisible(true);
       cmbProduct.setValue("");
       Program program = (Program) cmbProgram.getSelectedItem().getValue();
       loadCmbProduct(eventType, program.getId());
       lblIssuer.setValue(program.getIssuerId().getName());
       lblBinNumber.setValue(program.getBiniinNumber().toString());
    }
    
    public void clearFields() {

    }
    
    public void onClick$aChannelsYes(){
          cmbChannel.setDisabled(true);
    }
    
    public void onClick$aChannelsNo(){
          cmbChannel.setDisabled(false);
    }

    private void loadFields(KeyProperties keyProperties) {
        try {
            intSegment.setValue(keyProperties.getKeyLength());
            intNumberTotal.setValue(keyProperties.getTotalNumericCharacters());
            intDaysExpired.setValue(keyProperties.getExpirationDays());
            intPreviusKeys.setValue(keyProperties.getTotalPreviousKeys());
            
            if(keyProperties.getIndContinuousCharacters() == true){
               cCharactersYes.setChecked(true); 
            } else {
               cCharactersNo.setChecked(true); 
            }
            if(keyProperties.getIndConsecutiveEqualCharacters() == true){
                cFollowedYes.setChecked(true);
            } else {
                cFollowedNo.setChecked(true);
            }
        } catch (Exception ex) {
            showError(ex);
        }
 
    }

    public void blockFields() {
       btnSave.setVisible(false);
       cmbCountry.setDisabled(true);
       intSegment.setDisabled(true);
       intNumberTotal.setDisabled(true);
       intDaysExpired.setDisabled(true);
       intPreviusKeys.setDisabled(true);
    }

    public Boolean validateEmpty() {
        if (cmbCountry.getSelectedItem()  == null) {
            cmbCountry.setFocus(true);
            this.showMessage("cms.error.country.notSelected", true, null);     
        } else if(cmbProgram.getSelectedItem()  == null){
            cmbProgram.setFocus(true);
            this.showMessage("cms.error.program.notSelected", true, null);
        } else if(cmbProduct.getSelectedItem()  == null){
            cmbProduct.setFocus(true);
            this.showMessage("cms.error.producto.notSelected", true, null);
        } else if(intSegment.getValue() == null){
            intSegment.setFocus(true);
            this.showMessage("authorize.message.error.length", true, null);
        } else if(intNumberTotal.getValue() == null){
            intNumberTotal.setFocus(true);
            this.showMessage("authorize.message.error.length", true, null);
        } else if(intDaysExpired.getValue() == null){
            intDaysExpired.setFocus(true);
            this.showMessage("authorize.message.error.length", true, null);
        } else if(intPreviusKeys.getValue() == null){
            intPreviusKeys.setFocus(true);
            this.showMessage("authorize.message.error.length", true, null);
        } else {
            return true;
        }
        return false;

    }
    
    public boolean validateKeyProperties(){
        Channel channel = (Channel) cmbChannel.getSelectedItem().getValue();
        Product product = (Product) cmbProduct.getSelectedItem().getValue();
        try{
          keyPropertiesList.clear();
          EJBRequest request1 = new EJBRequest();
          Map params = new HashMap();
          params.put(Constants.CHANNEL_KEY, channel.getId());
          params.put(Constants.PRODUCT_KEY, product.getId());
          request1.setParams(params);
          keyPropertiesList = utilsEJB.getKeyPropertiesByChannelAndProduct(request1);
        } catch (Exception ex) {
            showError(ex);
        } finally{
            if (keyPropertiesList.size() > 0) {
                this.showMessage("authorize.message.error.duplicateKeyProperties", true, null);
                return false;
            }
        }
        return true;
    }
    private void saveKeyProperties(KeyProperties _keyProperties) throws RegisterNotFoundException, NullParameterException, GeneralException {
        boolean consecutiveCharacter = true;
        boolean equalsCharacter = true;
        try {
            KeyProperties keyProperties = null;

            if (_keyProperties != null) {
                keyProperties = _keyProperties;
            } else {
                keyProperties = new KeyProperties();
            }
            
            if(cCharactersYes.isChecked()){
                consecutiveCharacter = true;
            } else {
                consecutiveCharacter = false;
            }
            
            if(cFollowedYes.isChecked()){
                equalsCharacter = true;
            } else {
                equalsCharacter = false;
            }
            
            //Guardar KeyProperties
            if(aChannelsYes.isChecked()){
                if (eventType == WebConstants.EVENT_ADD) {
                    try{
                        EJBRequest request = new EJBRequest();
                        channelList = productEJB.getChannel(request);
                        if(channelList != null){
                            for(Channel ch : channelList){
                              keyProperties = new KeyProperties(); 
                              keyProperties.setChannelId(ch);
                              keyProperties.setProductId((Product) cmbProduct.getSelectedItem().getValue());
                              keyProperties.setKeyLength(intSegment.getValue());
                              keyProperties.setExpirationDays(intDaysExpired.getValue());
                              keyProperties.setTotalPreviousKeys(intPreviusKeys.getValue());
                              keyProperties.setTotalNumericCharacters(intNumberTotal.getValue());
                              keyProperties.setIndConsecutiveEqualCharacters(consecutiveCharacter);
                              keyProperties.setIndContinuousCharacters(equalsCharacter);
                              keyProperties.setCreateDate(new Timestamp(new Date().getTime()));
                              keyProperties = utilsEJB.saveKeyProperties(keyProperties);
                            }
                            this.showMessage("sp.common.save.success", false, null);
                            if (eventType == WebConstants.EVENT_ADD) {
                                btnSave.setVisible(false);
                            } else {
                                btnSave.setVisible(true);
                            }
                        }
                    } catch (EmptyListException ex) {
                    }
                }
            } else {
                keyProperties.setChannelId((Channel) cmbChannel.getSelectedItem().getValue());
                keyProperties.setProductId((Product) cmbProduct.getSelectedItem().getValue());
                keyProperties.setKeyLength(intSegment.getValue());
                keyProperties.setExpirationDays(intDaysExpired.getValue());
                keyProperties.setTotalPreviousKeys(intPreviusKeys.getValue());
                keyProperties.setTotalNumericCharacters(intNumberTotal.getValue());
                keyProperties.setIndConsecutiveEqualCharacters(consecutiveCharacter);
                keyProperties.setIndContinuousCharacters(equalsCharacter);
                if (eventType == WebConstants.EVENT_ADD) {
                    keyProperties.setCreateDate(new Timestamp(new Date().getTime()));
                } else {
                    keyProperties.setUpdateDate(new Timestamp(new Date().getTime()));
                }
                keyProperties = utilsEJB.saveKeyProperties(keyProperties);
                if (eventType == WebConstants.EVENT_ADD) {
                    btnSave.setVisible(false);
                } else {
                    btnSave.setVisible(true);
                }
                this.showMessage("sp.common.save.success", false, null);
            }           
        } catch (WrongValueException ex) {
            showError(ex);
        }
    }
 

    public void onClick$btnSave() throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    if(aChannelsYes.isChecked()){
                        saveKeyProperties(null);
                    } else {
                        if(validateKeyProperties()){
                        saveKeyProperties(null);
                        }
                    }
                break;
                case WebConstants.EVENT_EDIT:
                    saveKeyProperties(keyPropertiesParam);
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
                loadFields(keyPropertiesParam);
                loadCmbContry(eventType);
                onChange$cmbCountry();
                onChange$cmbProgram();
                loadCmbChannel(eventType);
                aChannelsYes.setDisabled(true);
                aChannelsNo.setDisabled(true);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(keyPropertiesParam);
                loadCmbContry(eventType);
                onChange$cmbCountry();
                onChange$cmbProgram();
                blockFields();
                loadCmbChannel(eventType);
                aChannelsYes.setDisabled(true);
                aChannelsNo.setDisabled(true);
                break;
            case WebConstants.EVENT_ADD:
                loadCmbContry(eventType);
                loadCmbChannel(eventType);
                onChange$cmbCountry();
                onChange$cmbProgram();
                cmbChannel.setDisabled(true);
                break;
            default:
                break;
        }
    }
    
    private void loadCmbContry(Integer evenInteger) {
        EJBRequest request1 = new EJBRequest();
        List<Country> countries;
        try {
            countries = utilsEJB.getCountries(request1);
            loadGenericCombobox(countries, cmbCountry, "name", evenInteger, Long.valueOf(keyPropertiesParam != null ? keyPropertiesParam.getProductId().getCountryId().getId() : 0));
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
        
    private void loadCmbProgram(Integer evenInteger, int countryId) {
        EJBRequest request1 = new EJBRequest();
        cmbProgram.getItems().clear();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_COUNTRY_ID, countryId);
        request1.setParams(params);
        List<Program> program = null;
        try {
            program = programEJB.getProgramByCountry(request1);
            loadGenericCombobox(program, cmbProgram, "name", evenInteger, Long.valueOf(keyPropertiesParam != null ? keyPropertiesParam.getProductId().getProgramId().getId() : 0));
        } catch (EmptyListException ex) {
            showError(ex);
            ex.printStackTrace();
        } catch (GeneralException ex) {
            showError(ex);
            ex.printStackTrace();
        } catch (NullParameterException ex) {
            showError(ex);
        } finally {
            if (program == null) {
                this.showMessage("cms.msj.DocumentsPersonTypeNull", false, null);
            }
        }
    }
    
    private void loadCmbProduct(Integer evenInteger, long programId) {
        EJBRequest request1 = new EJBRequest();
        cmbProduct.getItems().clear();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_PROGRAM_ID, programId);
        request1.setParams(params);
        List<Product> product = null;
        try {
            product = productEJB.getProductByProgram(request1);
            loadGenericCombobox(product, cmbProduct, "name", evenInteger, Long.valueOf(keyPropertiesParam != null ? keyPropertiesParam.getProductId().getId() : 0));
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
    
    private void loadCmbChannel(Integer evenInteger) {
        EJBRequest request1 = new EJBRequest();
        List<Channel> channel;
        try {
            channel = productEJB.getChannel(request1);
            loadGenericCombobox(channel, cmbChannel, "name", evenInteger, Long.valueOf(keyPropertiesParam != null ? keyPropertiesParam.getChannelId().getId() : 0));
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
