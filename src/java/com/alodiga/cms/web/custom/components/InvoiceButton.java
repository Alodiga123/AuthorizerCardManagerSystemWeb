package com.alodiga.cms.web.custom.components;


import com.alodiga.cms.web.utils.WebConstants;
import org.zkoss.zul.Button;

public class InvoiceButton extends Button{
    public InvoiceButton(){
        this.setImage("/images/icon-invoice.png");
    }
    public InvoiceButton(String view, Object obj,Long permissionId){
        this.setImage("/images/icon-invoice.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj,permissionId));
        
    }
}
