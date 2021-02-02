package com.alodiga.cms.web.controllers;


import com.alodiga.cms.web.generic.controllers.GenericAbstractController;
import java.util.Calendar;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

public class IndexViewController extends GenericAbstractController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtLogin;
    private Textbox txtPassword;
    private Label lblInfo;
    private Label lblInfo02;
    private String urlRedirect = null;
    private Vlayout vl01;
    private Vlayout vl02;

//    private Label lblUpdatedVersion;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    public void initialize() {
        try {            

            Calendar today = Calendar.getInstance();
            Calendar endingDate = Calendar.getInstance();
            endingDate.set(Calendar.MONTH, Calendar.AUGUST);
            endingDate.set(Calendar.DAY_OF_MONTH, 1);
            endingDate.set(Calendar.YEAR, 2020);

        } catch (Exception e) {
            e.printStackTrace();
            lblInfo.setValue(Labels.getLabel("sp.error.general"));
        }
    }



    public void onClick$lblRecoverReturn() throws InterruptedException {
        vl01.setVisible(true);
        vl02.setVisible(false);
    }

    public void onClick$btnLoginRecover() throws InterruptedException {
        if (validateEmptyRecover()) {
           
        }
    }

    public Boolean validateEmptyRecover() {
        Boolean valid = true;
       
        return valid;
    }

 



    public void onClick$lblRecoverPassword() throws InterruptedException {
        vl01.setVisible(false);
        vl02.setVisible(true);
    }

    public void onOK$btnRecover() {
        //TODO:
    }






    public void onClick$btnAccessNumbers() {
        Executions.getCurrent().sendRedirect("/docs/access-numbers.pdf", "_blank");
    }

    public void onClick$btnRates() {
        Executions.getCurrent().sendRedirect("/docs/new_rates.pdf", "_blank");
    }

    public void onClick$btnTerms() {
        Executions.getCurrent().sendRedirect("/docs/terms.pdf", "_blank");
    }
}
