package com.alodiga.cms.web.custom.components;


import com.alodiga.cms.web.utils.WebConstants;
import org.zkoss.zul.Button;

public class AddDescendantButton extends Button{
    public AddDescendantButton(){
        this.setImage("/images/icon-add.png");
    }
    public AddDescendantButton(String view, Object obj, Long permissionId){
        this.setImage("/images/icon-add.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_ADD_DESCENDANT, view, obj,permissionId));
        
    }
}
