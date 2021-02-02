package com.alodiga.cms.web.custom.components;

import com.alodiga.cms.web.utils.WebConstants;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;

public class EditButton extends Button {

    public EditButton() {
        this.setImage("/images/icon-edit.png");
    }

    public EditButton(String view, Object obj, Long permissionId) {
        this.setImage("/images/icon-edit.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_EDIT, view, obj, permissionId));
    }

    public EditButton(String view, Object obj) {
        Sessions.getCurrent().setAttribute(WebConstants.EVENTYPE, WebConstants.EVENT_EDIT);
        this.setImage("/images/icon-edit.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_EDIT, view, obj));
    }

}
