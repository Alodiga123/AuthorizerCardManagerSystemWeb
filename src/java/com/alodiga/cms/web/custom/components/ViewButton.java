package com.alodiga.cms.web.custom.components;


import com.alodiga.cms.web.utils.WebConstants;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;

public class ViewButton extends Button{
    public ViewButton(){
        this.setImage("/images/icon-invoice.png");
    }
    public ViewButton(String view, Object obj,Long permissionId){
        this.setImage("/images/icon-invoice.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj,permissionId)); 
    }
    
    public ViewButton(String view, Object obj){
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_VIEW);
        this.setImage("/images/icon-invoice.png");
        this.setClass("open orange");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj)); 
    }
}
