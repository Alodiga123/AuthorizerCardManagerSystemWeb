package com.alodiga.cms.web.custom.components;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Listcell;

public class ListcellPaymentButton extends Listcell {

    public ListcellPaymentButton() {
    }

    public ListcellPaymentButton(String destinationView, Object obj,Long permissionId) {
        PaymentButton button = new PaymentButton(destinationView, obj,permissionId);
        button.setTooltiptext(Labels.getLabel("sp.common.actions.payment"));
        button.setClass("open orange");
        button.setParent(this);
    }

    public ListcellPaymentButton(String destinationView, Object obj, String images,Long permissionId) {
        PaymentButton button = new PaymentButton(destinationView, obj,images,permissionId);
        button.setClass("open orange");
        button.setParent(this);
    }
}
