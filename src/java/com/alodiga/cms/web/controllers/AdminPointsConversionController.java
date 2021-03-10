package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.CardEJB;
import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.commons.ejb.ProgramEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.ejb.UserEJB;
import com.alodiga.cms.commons.ejb.ProductEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.commons.exception.RegisterNotFoundException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.cms.web.utils.AccessControl;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import com.cms.commons.enumeraciones.DocumentTypeE;
import com.cms.commons.enumeraciones.TransactionE;
import com.cms.commons.enumeraciones.StatusTransactionManagementE;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.models.AccountCard;
import com.cms.commons.models.BalanceHistoryCard;
import com.cms.commons.models.BonusCard;
import com.cms.commons.models.Channel;
import com.cms.commons.models.Country;
import com.cms.commons.models.KeyProperties;
import com.cms.commons.models.PhonePerson;
import com.cms.commons.models.Product;
import com.cms.commons.models.Program;
import com.cms.commons.models.Sequences;
import com.cms.commons.models.TransactionPoint;
import com.cms.commons.models.TransactionsManagement;

import com.cms.commons.models.User;
import com.cms.commons.util.Constants;
import com.cms.commons.util.QueryConstants;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

public class AdminPointsConversionController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Label lblCard;
    private Label lblCustomerName;
    private Label lblCountry;
    private Label lblIdentificationType;
    private Label lblIdentificationNumber;
    private Label lblPhone;
    private Label lblEmail;
    private Label lblCardNumber;
    private Label lblCardHolder;
    private Label lblExpirationDate;
    private Label lblCardStatus;
    private Label lblPoints;
    private Label lblLoyaltyProgram;
    private Label lblTransactionType;
    private Label lblChannel;
    private Label lblPointsAcumulated;
    private Intbox intPointsToConvert;
    private PersonEJB personEJB = null;
    private UtilsEJB utilsEJB = null;
    private UserEJB userEJB = null;
    private CardEJB cardEJB = null;
    private ProductEJB productEJB = null;
    private TransactionPoint transactionPointParam = null;
    private List<PhonePerson> phoneUserList = null;
    List<Sequences> sequencesList = new ArrayList<Sequences>();
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;
    private User currentuser = null;
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        transactionPointParam = (Sessions.getCurrent().getAttribute("object") != null) ? (TransactionPoint) Sessions.getCurrent().getAttribute("object") : null;
        eventType = (Integer) Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE);
        if (eventType == WebConstants.EVENT_ADD) {
            transactionPointParam= null;
        } else {
            transactionPointParam = (TransactionPoint) Sessions.getCurrent().getAttribute("object");
        }
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            cardEJB = (CardEJB) EJBServiceLocator.getInstance().get(EjbConstants.CARD_EJB);
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    
    public void clearFields() {

    }


    private void loadFields(TransactionPoint transactionPoint) {
        PhonePerson mainPhoneUser = null;
        try {
            //Obtener el usuario logueado
            currentuser = AccessControl.loadCurrentUser();
            User user = (User) session.getAttribute(Constants.USER_OBJ_SESSION);
            
            //Datos del cliente
            StringBuilder userName = new StringBuilder(user.getFirstNames());
            userName.append(" ");
            userName.append(user.getLastNames());
            lblCustomerName.setValue(userName.toString());
            lblCountry.setValue(user.getPersonId().getCountryId().getName());
            lblIdentificationType.setValue(user.getDocumentsPersonTypeId().getDescription());
            lblIdentificationNumber.setValue(user.getIdentificationNumber());
            lblEmail.setValue(user.getPersonId().getEmail());
            
            //Obtener el telefono principal
            EJBRequest request = new EJBRequest();
            HashMap params = new HashMap();
            params.put(Constants.PERSON_KEY, user.getPersonId().getId());
            request.setParams(params);
            phoneUserList = personEJB.getPhoneByPerson(request);
            if(phoneUserList != null){
              for(PhonePerson phoneUser : phoneUserList){
                    if(phoneUser.getIndMainPhone() == true){
                        mainPhoneUser = phoneUser;
                    }
                }
              lblPhone.setValue(mainPhoneUser.getNumberPhone());
            } else {
                lblPhone.setValue("");
            }
            
            //Obtener informacion de la tarjeta
            lblCard.setValue(transactionPoint.getCardId().getProductId().getName());
            lblCardNumber.setValue(transactionPoint.getCardId().getCardNumber());
            lblCardHolder.setValue(transactionPoint.getCardId().getCardHolder());
            lblExpirationDate.setValue(transactionPoint.getCardId().getExpirationDate().toString());
            lblCardStatus.setValue(transactionPoint.getCardId().getCardStatusId().getDescription());
            
            //Conversión de Puntos
            lblLoyaltyProgram.setValue(transactionPoint.getProgramLoyaltyTransactionId().getProgramLoyaltyId().getDescription());
            lblTransactionType.setValue(transactionPoint.getProgramLoyaltyTransactionId().getTransactionId().getDescription());
            lblChannel.setValue(transactionPoint.getProgramLoyaltyTransactionId().getChannelId().getName());
            lblPointsAcumulated.setValue(transactionPoint.getPoints().toString());
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
      public String getSequencesByDocumentType(int documentTypeId){
          String seq = null;
          try{
          EJBRequest request1 = new EJBRequest();
          Map params = new HashMap();
          params.put(Constants.DOCUMENT_TYPE_KEY, documentTypeId);
          request1.setParams(params);
          sequencesList = utilsEJB.getSequencesByDocumentType(request1);
          if(sequencesList != null){
              seq = utilsEJB.generateNumberSequence(sequencesList, Constants.ORIGIN_APPLICATION_CMS_AUTHORIZE);
          }
        
        } catch (Exception ex) {
            showError(ex);
        }
          return seq;
      }
    
    public void blockFields() {
       btnSave.setVisible(false);
    }

    public Boolean validateEmpty(TransactionPoint transactionPoint) {
        if (intPointsToConvert.getValue()  == null) {
            intPointsToConvert.setFocus(true);
            this.showMessage("error.msj.pointsConversionNull", true, null);     
        } else if (intPointsToConvert.getValue() > transactionPoint.getPoints()){
            intPointsToConvert.setFocus(true);
            this.showMessage("error.msj.pointsConversionMaxTotalPoints", true, null);
        } else {
            return true;
        }
        return false;

    }
     
    private void savePointsConversion(TransactionPoint _transactionPoint) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Float conversionRatePoints = 0F;
        Float amountCredit = 0F;
        Float currentBalance = 0F;
        Float newBalance = 0F;
        Float maximunBalance = 0F;
        Integer transactionPointsTotal = 0;
        Integer bonusPointsTotal = 0;
        AccountCard accountCard = null;
        Product product = null;
        BalanceHistoryCard balanceHistoryCard = null;
        BonusCard bonusCard = null;
        Integer documentTypeId = DocumentTypeE.BONUS_TRANSACTION_CMS.getId();
        String sequences = null;
        String succesCode = "00";
        try {
           TransactionPoint transactionPoint = null;
           TransactionsManagement transactionsManagement = null;
           if(_transactionPoint != null){
               transactionPoint = _transactionPoint;
           } 
           
            //Obtener el canal interno del CMS
            EJBRequest request1 = new EJBRequest();
            request1.setParam(Constants.CHANNEL_INT_CMS);
            Channel channel = productEJB.loadChannel(request1);
            
            //Se da el formato a la fecha de expiracion de la tarjeta
            String pattern = "MMyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String expirationCardDate = simpleDateFormat.format(transactionPoint.getCardId().getExpirationDate());
                
           //Obtener la tasa de conversión y se realiza la conversión de los puntos
           conversionRatePoints = transactionPoint.getProgramLoyaltyTransactionId().getProgramLoyaltyId().getConversionRatePoints();
           amountCredit = intPointsToConvert.getValue() * conversionRatePoints;
           
           //Obtener el saldo actual de la tarjeta y sumarlo con la conversion de puntos
           accountCard = cardEJB.getAccountCardByCard(transactionPoint.getCardId().getId());
           currentBalance = accountCard.getCurrentBalance();
           newBalance = currentBalance +  amountCredit;
           
           //Obtener el maximo balance permitido por el producto
           maximunBalance = transactionPoint.getCardId().getProductId().getMaximumBalance();
           
           //Verificar si el nuevo saldo de la tarjeta cumple con el maximo permitido
           if(newBalance <= maximunBalance){ 
               //Se Genera la secuencia y se crea la Transacción 
               sequences = getSequencesByDocumentType(documentTypeId);
               transactionsManagement = new TransactionsManagement();
               transactionsManagement.setTransactionNumberIssuer(sequences);
               transactionsManagement.setTransactionDateIssuer(new Timestamp(new Date().getTime()));
               transactionsManagement.setTransactionTypeId(TransactionE.BONIFICATION_CMS.getId());
               transactionsManagement.setChannelId(channel.getId());
               transactionsManagement.setStatusTransactionManagementId(StatusTransactionManagementE.APPROVED.getId());
               transactionsManagement.setCardNumber(transactionPoint.getCardId().getCardNumber());
               transactionsManagement.setCardHolder(transactionPoint.getCardId().getCardHolder());
               transactionsManagement.setExpirationCardDate(expirationCardDate);
               transactionsManagement.setResponseCode(succesCode);
               transactionsManagement.setSettlementTransactionAmount(amountCredit);
               transactionsManagement.setCreateDate(new Timestamp(new Date().getTime()));
               transactionsManagement = productEJB.saveTransactionsManagement(transactionsManagement);
              
               //Actualizar el estado de cuenta
               accountCard.setCurrentBalance(newBalance);
               accountCard.setUpdateDate(new Timestamp(new Date().getTime()));
               accountCard = cardEJB.saveAccountCard(accountCard);
               
               //Obtener el Balance History y actualizarlo
               balanceHistoryCard = cardEJB.getBalanceHistoryCardByCard(transactionPoint.getCardId().getId());
               if(balanceHistoryCard != null){
                   balanceHistoryCard.setCurrentBalance(newBalance);
                   balanceHistoryCard.setPreviousBalance(currentBalance);
                   balanceHistoryCard.setTransactionsManagementId(transactionsManagement.getId());
                   balanceHistoryCard.setUpdateDate(new Timestamp(new Date().getTime()));
                   balanceHistoryCard = cardEJB.saveBalanceHistoryCard(balanceHistoryCard);
               } else {
                   balanceHistoryCard = new BalanceHistoryCard();
                   balanceHistoryCard.setCardUserId(transactionPoint.getCardId());
                   balanceHistoryCard.setCurrentBalance(newBalance);
                   balanceHistoryCard.setPreviousBalance(currentBalance);
                   balanceHistoryCard.setTransactionsManagementId(transactionsManagement.getId());
                   balanceHistoryCard.setCreateDate(new Timestamp(new Date().getTime()));
                   balanceHistoryCard = cardEJB.saveBalanceHistoryCard(balanceHistoryCard);
               }
               
               //Se aplica el descuentos de los puntos utilizados en TransactionPoints
               transactionPointsTotal = transactionPoint.getPoints() - intPointsToConvert.getValue();
               transactionPoint.setPoints(transactionPointsTotal);
               transactionPoint.setUpdateDate(new Timestamp(new Date().getTime()));
               transactionPoint = productEJB.saveTransactionPoint(transactionPoint);
               
               //Se aplica el descuentos de los puntos utilizados en BonusCard
               bonusCard = cardEJB.getBonusCardByCardId(transactionPoint.getCardId().getId());
               bonusPointsTotal = bonusCard.getTotalPointsAccumulated() - intPointsToConvert.getValue();
               bonusCard.setTotalPointsAccumulated(bonusPointsTotal);
               bonusCard.setUpdateDate(new Timestamp(new Date().getTime()));
               bonusCard = cardEJB.saveBonusCard(bonusCard);

               this.showMessage("sp.common.save.success", false, null);
               btnSave.setVisible(false);
           } else {
              this.showMessage("error.msj.pointsConversion.totalPointsToConvert", true, null); 
           }

           
        } catch (WrongValueException ex) {
            showError(ex);
        }
    }
 

    public void onClick$btnSave() throws RegisterNotFoundException, NullParameterException, GeneralException {
         if(validateEmpty(transactionPointParam)){
            savePointsConversion(transactionPointParam);  
         }
    }

    public void onclick$btnBack() {
        Executions.getCurrent().sendRedirect("listPointsConversion.zul");
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(transactionPointParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(transactionPointParam);
                break;
            default:
                break;
        }
    }
      
    private Object getSelectedItem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
