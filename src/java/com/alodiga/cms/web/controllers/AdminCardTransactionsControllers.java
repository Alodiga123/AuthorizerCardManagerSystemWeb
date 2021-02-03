package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.CardEJB;
import com.alodiga.cms.commons.ejb.ProductEJB;
import com.alodiga.cms.commons.ejb.ProgramEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.models.Card;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.text.SimpleDateFormat;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

public class AdminCardTransactionsControllers extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Label lblCountry;
    private Label lblProgram;
    private Label lblProduct;
    private Label lblDocumentType;
    private Label lblIdentificactionNumber;
    private Label lblCustomerName;
    private Label lblCarHolder;
    private Label lblAccount;
    private Label lblEmail;
    private Label lblPhone;
    private Label lblCardNumber;
    private Label lblCardHolder;
    private Label lblIssueDate;
    private Label lblStatus;
    private Label lblExpirationDate;
    private Label lblFirstNames;
    private Label lblLastNames;
    private Label lblAddress;
    private UtilsEJB utilsEJB = null;
    private CardEJB cardEJB = null;
    private ProgramEJB programEJB = null;
    private ProductEJB productEJB = null;
    private Card cardParam;
    private Button btnSave;
    private Integer evenType;
    public static Card cardParent = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        evenType = (Integer) (Sessions.getCurrent().getAttribute(WebConstants.EVENTYPE));
        if (eventType == WebConstants.EVENT_ADD) {
            cardParam = null;
        } else {
            cardParam = (Card) Sessions.getCurrent().getAttribute("object");
            cardParent = cardParam;
        }
        initialize();
        loadData();
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            cardEJB = (CardEJB) EJBServiceLocator.getInstance().get(EjbConstants.CARD_EJB);
            programEJB = (ProgramEJB) EJBServiceLocator.getInstance().get(EjbConstants.PROGRAM_EJB);
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
    }
    
    public Card getCardParent() {
        return this.cardParent;
    }
    
    private void loadFields(Card card) {
        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            
            //Grupo de Tarjeta
            lblCardNumber.setValue(card.getAlias());
            lblCardHolder.setValue(card.getCardHolder());
            lblAccount.setValue(card.getAssignedAccount());
            lblProduct.setValue(card.getProductId().getName());
            lblExpirationDate.setValue(simpleDateFormat.format(card.getExpirationDate()));
            lblStatus.setValue(card.getCardStatusId().getDescription());
            
            //Grupo Tarjetahabiente
            lblFirstNames.setValue(card.getPersonCustomerId().getNaturalCustomer().getFirstNames());
            lblLastNames.setValue(card.getPersonCustomerId().getNaturalCustomer().getLastNames());
            lblDocumentType.setValue(card.getPersonCustomerId().getNaturalCustomer().getDocumentsPersonTypeId().getDescription());
            lblIdentificactionNumber.setValue(card.getPersonCustomerId().getNaturalCustomer().getIdentificationNumber());
            lblEmail.setValue(card.getPersonCustomerId().getEmail());
            lblPhone.setValue(card.getPersonCustomerId().getPhonePerson().getNumberPhone());
            lblAddress.setValue(card.getPersonCustomerId().getNaturalCustomer().getPersonId().getPersonHasAddress().getAddressId().getAddressLine1());

        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void blockFields() {
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        return true;
    }

    private void saveCardStatus(Card _card) {
        try {
            Card card = null;

            if (_card != null) {
                card = _card;
            } else {
                card = new Card();
            }
            
            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception ex) {
            showError(ex);
        }

    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (evenType) {
                case WebConstants.EVENT_ADD:
                    saveCardStatus(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveCardStatus(cardParam);
                    break;
            }
        }
    }

    public void loadData() {
        switch (evenType) {
            case WebConstants.EVENT_EDIT:
                loadFields(cardParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(cardParam);
                blockFields();
                break;
            case WebConstants.EVENT_ADD:
                break;
            default:
                break;
        }
    }

}
