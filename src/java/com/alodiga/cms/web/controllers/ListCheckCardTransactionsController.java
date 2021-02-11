package com.alodiga.cms.web.controllers;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.ejb.ProductEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.commons.exception.RegisterNotFoundException;
import com.alodiga.cms.web.custom.components.ListcellEditButton;
import com.alodiga.cms.web.custom.components.ListcellViewButton;
import com.alodiga.cms.web.generic.controllers.GenericAbstractListController;
import static com.alodiga.cms.web.generic.controllers.GenericDistributionController.request;
import com.alodiga.cms.web.utils.Utils;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Card;
import com.cms.commons.models.Channel;
import com.cms.commons.models.Country;
import com.cms.commons.models.TransactionsManagement;
import com.cms.commons.models.Transaction;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import com.cms.commons.util.QueryConstants;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

public class ListCheckCardTransactionsController extends GenericAbstractListController<TransactionsManagement> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Combobox cmbTransactionType;
    private Combobox cmbChannel;
    private Datebox datefrom;
    private Datebox dateuntil;
    private Label lblCard;
    private Textbox txtAnswer;
    private UtilsEJB utilsEJB = null;
    private ProductEJB productsEJB = null;
    private List<TransactionsManagement> transactionsManagement = null;
    private boolean f1 = true;
    private Card cardParam;
    private Tab tabCardTransactionsMananger;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        cardParam = (Card) Sessions.getCurrent().getAttribute("object");
        initialize();
    }

    public void startListener() {
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            //Evaluar Permisos
            permissionEdit = true;
            permissionAdd = true; 
            permissionRead = true;
            adminPage = "adminManageTransactions.zul";
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            productsEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            datefrom.setFormat("dd/MM/yyyy");
            datefrom.setValue(new Timestamp(new java.util.Date().getTime()));
            dateuntil.setFormat("dd/MM/yyyy");
            dateuntil.setValue(new Timestamp(new java.util.Date().getTime()));
            loadTransactionType();
            loadChannel();
            getCard();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute(WebConstants.EVENTYPE, WebConstants.EVENT_ADD);
        Executions.getCurrent().sendRedirect(adminPage);
    }
    
    public void loadTransactionType(){
        try {
            cmbTransactionType.getItems().clear();
            EJBRequest request = new EJBRequest();
            List<Transaction> transaction = productsEJB.getTransaction(request);
            Comboitem item = new Comboitem();
            item.setLabel(Labels.getLabel("sp.common.all"));
            item.setParent(cmbTransactionType);
            cmbTransactionType.setSelectedItem(item);
            for (int i = 0; i < transaction.size(); i++) {
                item = new Comboitem();
                item.setValue(transaction.get(i));
                item.setLabel(transaction.get(i).getDescription());
                item.setParent(cmbTransactionType);
            }
        } catch (Exception ex) {
            this.showError(ex);
        }
    }
    
    public void loadChannel(){
        try {
            cmbChannel.getItems().clear();
            EJBRequest request = new EJBRequest();
            List<Channel> channel = productsEJB.getChannel(request);
            Comboitem item = new Comboitem();
            item.setLabel(Labels.getLabel("sp.common.all"));
            item.setParent(cmbChannel);
            cmbChannel.setSelectedItem(item);
            for (int i = 0; i < channel.size(); i++) {
                item = new Comboitem();
                item.setValue(channel.get(i));
                item.setLabel(channel.get(i).getName());
                item.setParent(cmbChannel);
            }
        } catch (Exception ex) {
            this.showError(ex);
        }
    }

    public void loadList(boolean filter) throws RegisterNotFoundException, NullParameterException, GeneralException {
        List<TransactionsManagement> transactionsManagement = new ArrayList<TransactionsManagement>();
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            EJBRequest request = new EJBRequest();
            params = new HashMap<String, Object>();
            params.put(QueryConstants.PARAM_BEGINNING_DATE, datefrom.getValue());
            params.put(QueryConstants.PARAM_ENDING_DATE, dateuntil.getValue());
            params.put(QueryConstants.PARAM_CARD_NUMBER , lblCard.getValue());

            if (cmbTransactionType.getSelectedItem() != null && cmbTransactionType.getSelectedIndex() != 0) {
                  params.put(QueryConstants.PARAM_TRANSACTION_ID, ((Transaction) cmbTransactionType.getSelectedItem().getValue()).getId());
            }  
            if (cmbChannel.getSelectedItem() != null && cmbChannel.getSelectedIndex() != 0) {
                  params.put(QueryConstants.PARAM_CHANNEL_ID, ((Channel) cmbChannel.getSelectedItem().getValue()).getId());
            } 
            if (!txtAnswer.getText().equals("")) {
                  params.put(QueryConstants.PARAM_RESPONSE_CODE, txtAnswer.getText());
            }
            request.setParams(params);
            transactionsManagement = productsEJB.searchTransactionsManagementByParams(request);
              
        } catch (NullParameterException ex) {
        } catch (EmptyListException ex) {
        } catch (GeneralException ex) {
        }
        lbxRecords.getItems().clear();
        Listitem item = null;
        if (transactionsManagement != null && transactionsManagement.size() != 0 ) {
            btnDownload.setVisible(true);
            for (TransactionsManagement transaction : transactionsManagement) {
                item = new Listitem();
                item.setValue(transaction);
                String pattern = "dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                item.appendChild(new Listcell(simpleDateFormat.format(transaction.getDateTransaction())));
                item.appendChild(new Listcell(transaction.getTransactionTypeId().toString()));
                item.appendChild(new Listcell(transaction.getCardNumber()));
//                item.appendChild(new Listcell(transaction.getLocalCurrencyTransactionId().toString()));
                item.appendChild(new Listcell(""));
                if(transaction.getLocalCurrencyTransactionAmount() != null){
                   item.appendChild(new Listcell(transaction.getLocalCurrencyTransactionAmount().toString())); 
                } else {
                   item.appendChild(new Listcell("")); 
                }
                if(transaction.getResponseCode() != null){
                   item.appendChild(new Listcell(transaction.getResponseCode())); 
                } else {
                   item.appendChild(new Listcell(""));  
                }
                if(transaction.getMccCodeTrade() != null){
                   item.appendChild(new Listcell(transaction.getMccCodeTrade())); 
                } else {
                   item.appendChild(new Listcell("")); 
                }
                if(transaction.getChannelId() != null){
                    // Modificar por autorizador
                   item.appendChild(new Listcell(transaction.getChannelId().toString())); 
                } else {
                   item.appendChild(new Listcell("")); 
                }

                item.appendChild(permissionRead ? new ListcellViewButton(adminPage, transaction) : new Listcell());
                item.setParent(lbxRecords);
            }
        } else {
                btnDownload.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);
        }
    }

    private void showEmptyList(){
                Listitem item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);  
    }
    
    public void getCard(){
        Card card = null;
        AdminCardTransactionsControllers adminCard = new AdminCardTransactionsControllers();
        if(adminCard.getCardParent().getId() != null){
            card = adminCard.getCardParent();
            lblCard.setValue(card.getCardNumber()); 
        }
    }
    
    public void onClick$btnDownload() throws InterruptedException {
        try {
            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            StringBuilder file = new StringBuilder(Labels.getLabel("cms.list.cardTransactions"));
            file.append("_");
            file.append(date);
            Utils.exportExcel(lbxRecords, file.toString());
        } catch (Exception ex) {
            showError(ex);
        }
        
    }
    
    public void onClick$btnClear() throws InterruptedException {
        txtAnswer.setText("");
        datefrom.setText("");
        dateuntil.setText("");
    }
    
    public void clearFields() {
    }
    
    public Boolean validateDate(){
        if(datefrom.getValue().compareTo(dateuntil.getValue()) >= 0){
          this.showMessage("cms.error.date.beginDateBeforeUntilDate", true, null);  
        } else {
            divInfo.setVisible(false);
            return true;
        }
        return false;
    }
    
    public void onClick$btnSearch() throws RegisterNotFoundException, NullParameterException, GeneralException {
        if(validateDate()){
            loadList(f1);
            datefrom.setText("");
            dateuntil.setText("");
        } 
    }
    
    
    
    @Override
    public List<TransactionsManagement> getFilterList(String filter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadDataList(List<TransactionsManagement> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
