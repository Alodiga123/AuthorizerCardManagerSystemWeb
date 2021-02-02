package com.alodiga.cms.web.custom.components;


import com.alodiga.cms.web.utils.WebConstants;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;

public class paymentButton extends Button{

    public paymentButton(String view, Object obj,Long permissionId){
        this.setLabel(Labels.getLabel("/images/icon-show.png"));
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj,permissionId));
        
    }

    public paymentButton(String view, Object obj,String images, Long permissionId){
//        this.setImage("/images/icon-show.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj,permissionId));

    }
}
