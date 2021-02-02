package com.alodiga.cms.web.custom.components;

import org.zkoss.zul.Button;

public class ChangeStatusButton extends Button {

    public ChangeStatusButton(Boolean bool) {
        if (bool) {
            this.setImage("/images/icon-enable.png");
        } else {
            this.setImage("/images/icon-disable.png");
        }
    }

    public void changeImageStatus(Boolean bool) {
        if (bool) {
            this.setImage("/images/icon-disable.png");
        } else {
            this.setImage("/images/icon-enable.png");

        }
    }
}
