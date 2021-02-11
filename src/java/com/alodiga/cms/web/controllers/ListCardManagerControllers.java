package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.CardEJB;
import com.alodiga.cms.commons.ejb.ProgramEJB;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.web.custom.components.ListcellEditButton;
import com.alodiga.cms.web.custom.components.ListcellViewButton;
import com.alodiga.cms.web.generic.controllers.GenericAbstractListController;
import com.alodiga.cms.web.utils.Utils;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Card;
import com.cms.commons.models.Country;
import com.cms.commons.models.Program;
import com.cms.commons.models.User;
import com.cms.commons.util.Constants;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import com.cms.commons.util.QueryConstants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ListCardManagerControllers extends GenericAbstractListController<Card> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtName;
    private Combobox cmbCountry;
    private Combobox cmbProgram;
    private UtilsEJB utilsEJB = null;
    private CardEJB cardEJB = null;
    private ProgramEJB programEJB = null;
    private Program program = null;
    private List<Card> card = null;
    private User currentUser;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            currentUser = (User) session.getAttribute(Constants.USER_OBJ_SESSION);
            adminPage = "TabCardTransactions.zul";
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            programEJB = (ProgramEJB) EJBServiceLocator.getInstance().get(EjbConstants.PROGRAM_EJB);
            cardEJB = (CardEJB) EJBServiceLocator.getInstance().get(EjbConstants.CARD_EJB);
            loadCmbCountry(WebConstants.EVENT_ADD);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    
    public void onChange$cmbCountry() {
        cmbProgram.setValue("");
        Country country = (Country) cmbCountry.getSelectedItem().getValue();
        loadCmbProgram (WebConstants.EVENT_ADD, country.getId());
    }
    

    public void onClick$btnSearch() throws InterruptedException {
        if (validateEmpty()) {
            program = (Program) cmbProgram.getSelectedItem().getValue();
            Sessions.getCurrent().setAttribute(WebConstants.PROGRAM, program);
            getData(program.getId());
            loadDataList(card);
        }
    }
    

    public Boolean validateEmpty() {
        if (cmbProgram.getSelectedItem() == null) {
            cmbProgram.setFocus(true);
            this.showMessage("cms.error.program.notSelected", true, null);
        } else {
            return true;
        }
        return false;
    }


    public void getData(Long programId) {
        try {
            card = new ArrayList<Card>();
            card = cardEJB.getCardByProgramAndTransactionalStatus(programId);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            showEmptyList();
        } catch (GeneralException ex) {
            showError(ex);
        } 
    }

    public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Sessions.getCurrent().removeAttribute("object");
        Executions.getCurrent().sendRedirect(adminPage);
    }

    public void onClick$btnDownload() throws InterruptedException {
         try {
            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            StringBuilder file = new StringBuilder(Labels.getLabel("cms.menu.card.manager.list"));
            file.append("_");
            file.append(date);
            Utils.exportExcel(lbxRecords, file.toString());
        } catch (Exception ex) {
            showError(ex);
        }
        
    }

    public void onClick$btnClear() throws InterruptedException {
        txtName.setText("");
    }

    public void startListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public void loadDataList(List<Card> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                btnDownload.setVisible(true);
                for (Card card : list) {
                    item = new Listitem();
                    item.setValue(card);
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    item.appendChild(new Listcell(card.getAlias()));
                    item.appendChild(new Listcell(card.getCardHolder()));
                    item.appendChild(new Listcell(card.getPersonCustomerId().getEmail()));
                    item.appendChild(new Listcell(simpleDateFormat.format(card.getExpirationDate())));
                    item.appendChild(new Listcell(card.getCardStatusId().getDescription()));
                    item.appendChild(new ListcellEditButton(adminPage, card));
                    item.appendChild(new ListcellViewButton(adminPage, card, true));
                    item.setParent(lbxRecords);
                }
            } else {
                btnDownload.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showEmptyList() {
        Listitem item = new Listitem();
        item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
        item.appendChild(new Listcell());
        item.appendChild(new Listcell());
        item.appendChild(new Listcell());
        item.setParent(lbxRecords);
    }
    
    
    private void loadCmbCountry(Integer evenInteger) {
        cmbCountry.getItems().clear();
        EJBRequest request1 = new EJBRequest();
        List<Country> countries;
        try {
            countries = utilsEJB.getCountries(request1);
            loadGenericCombobox(countries, cmbCountry, "name", evenInteger, Long.valueOf(0));
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
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_COUNTRY_ID, countryId);
        request1.setParams(params);
        List<Program> programs;
        try {
            programs = programEJB.getProgramByCountry(request1);
            loadGenericCombobox(programs, cmbProgram, "name", evenInteger, Long.valueOf(0));
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
    
   
    @Override
    public List<Card> getFilterList(String filter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
