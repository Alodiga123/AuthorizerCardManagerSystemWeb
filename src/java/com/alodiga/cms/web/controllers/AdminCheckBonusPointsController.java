package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.commons.ejb.UserEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.ejb.CardEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.commons.exception.RegisterNotFoundException;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.cms.web.utils.AccessControl;
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
import com.cms.commons.models.BonusCard;
import com.cms.commons.models.Card;
import com.cms.commons.models.ComercialAgency;
import com.cms.commons.models.Country;
import com.cms.commons.models.DocumentsPersonType;
import com.cms.commons.models.Employee;
import com.cms.commons.models.NaturalCustomer;
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

public class AdminCheckBonusPointsController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbCards;
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
    private PersonEJB personEJB = null;
    private UtilsEJB utilsEJB = null;
    private UserEJB userEJB = null;
    private CardEJB cardEJB = null;
    private User userParam;
    private Button btnSave;
    private Integer eventType;
    private Toolbarbutton tbbTitle;
    private List<PhonePerson> phoneUserList = null;
    private List<NaturalCustomer> naturalCustomerList = null;
    private User currentuser = null;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        userParam = (Sessions.getCurrent().getAttribute("object") != null) ? (User) Sessions.getCurrent().getAttribute("object") : null;
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
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
    }

    public void blockFields() {
        btnSave.setVisible(false);
    }

    public void loadData() {
        PhonePerson mainPhoneUser = null;
        Integer eventType = 1;
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
            
            //Obtener las tarjetas del cliente
            loadCards(user.getPersonId().getId());
            
      } catch (Exception ex) {
            ex.printStackTrace();
      } 
    }

    private void loadCards(Long personId) {
        try {
            cmbCards.getItems().clear();
            EJBRequest request = new EJBRequest();
            List<Card> cards = cardEJB.getCardByPerson(personId);
            Comboitem item = new Comboitem();
            item.setParent(cmbCards);
            cmbCards.setSelectedItem(item);
            for (int i = 0; i < cards.size(); i++) {
                item = new Comboitem();
                item.setValue(cards.get(i));
                item.setLabel(cards.get(i).getProductId().getName());
                item.setParent(cmbCards);
            }
        } catch (Exception ex) {
            this.showError(ex);
        }
    }
    
    public void onChange$cmbCards(){
        this.clearMessage();
        cmbCards.setVisible(true);
        Card cards = (Card) cmbCards.getSelectedItem().getValue();
        lblCardNumber.setValue(cards.getCardNumber());
        lblCardStatus.setValue(cards.getCardStatusId().getDescription());
        lblExpirationDate.setValue(cards.getExpirationDate().toString());
        lblCardHolder.setValue(cards.getCardHolder());
        
        //Obtener los puntos de la tarjeta
        BonusCard bonusCard = getBonusCard(cards.getId());
        if(bonusCard != null){
          lblPoints.setValue(bonusCard.getTotalPointsAccumulated().toString());  
        } else {
           StringBuilder noPoints = new StringBuilder(Labels.getLabel("authorize.crud.checkBonusPoints.noPoints"));
           lblPoints.setValue(noPoints.toString());
        }
    }
    
    public BonusCard getBonusCard(Long cardId){
        BonusCard bonusCard = null;
        try{
          bonusCard = cardEJB.getBonusCardByCardId(cardId);
          if(bonusCard == null){
            return bonusCard; 
          }
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return bonusCard;
    }
    
    private Object getSelectedItem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
