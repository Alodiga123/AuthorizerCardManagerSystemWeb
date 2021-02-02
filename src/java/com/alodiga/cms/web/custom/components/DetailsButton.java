package com.alodiga.cms.web.custom.components;

import com.alodiga.cms.web.utils.WebConstants;
import org.zkoss.zul.Button;

public class DetailsButton extends Button{

    public DetailsButton(String view, Object obj,Long permissionId){
        this.setImage("/images/icon-eye.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj,permissionId));
        
    }

    public DetailsButton(String view, Object obj,String images, Long permissionId){
       this.setImage("/images/icon-eye.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj,permissionId));

    }
}
