package com.alodiga.cms.web.custom.components;


import com.alodiga.cms.web.utils.WebConstants;
import org.zkoss.zul.Button;

public class PaymentButton extends Button{

    public PaymentButton(String view, Object obj,Long permissionId){
        this.setImage("/images/icon-dolar.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj,permissionId));
        
    }

    public PaymentButton(String view, Object obj,String images, Long permissionId){
       this.setImage("/images/icon-dolar.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj,permissionId));

    }
}
