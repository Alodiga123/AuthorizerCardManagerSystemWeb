package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.ejb.ClosingTimerEJB;
import com.alodiga.cms.web.generic.controllers.GenericAbstractController;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.util.Date;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

public class AutomaticServicesController extends GenericAbstractController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Label lblInfo;
    private Label lblDailyClosingDate;
    private Label lblDailyClosingInterval;

    private ClosingTimerEJB closingTimerEJB = null;


    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }


    @Override
    public void initialize() {
        try {
            super.initialize();
            closingTimerEJB = (ClosingTimerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CLOSING_TIMER_EJB);
            showPPNExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("authorize.message.error.general"));
        }
    }

    private void showPPNExecutionDates() {
        Date date1 = closingTimerEJB.getNextExecutionDate();
        lblDailyClosingDate.setValue(date1 != null ? date1.toString() : Labels.getLabel("wallet.crud.automatic.commission.noDate"));
        Long dailyInterval = closingTimerEJB.getTimeoutInterval() / 86400000;// 86400000 Milisegundos en un dia
        lblDailyClosingInterval.setValue(dailyInterval.toString());

    }

    public void onClick$btnPPNRestart() {
        try {
            closingTimerEJB.restart();
            lblInfo.setValue(Labels.getLabel("authorize.crud.automatic.commission.success"));
            showPPNExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("authorize.message.error.general"));
        }
    }

    public void onClick$btnPPNStop() {
        try {
            closingTimerEJB.stop();
            lblInfo.setValue(Labels.getLabel("authorize.crud.automatic.commission.success"));
            showPPNExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("authorize.message.error.general"));
        }
    }

    public void onClick$btnPPNTimeout() {
        try {
            closingTimerEJB.forceTimeout();
            String response = Labels.getLabel("authorize.crud.automatic.commission.timeoutMessage");
            lblInfo.setValue(response);
            showPPNExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("authorize.message.error.general"));
        }
    }

    public void onClick$btnPPNNextExecution() {
        try {
            showPPNExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("authorize.message.error.general"));
        }
    }
 
}
