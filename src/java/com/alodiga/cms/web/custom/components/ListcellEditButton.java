package com.alodiga.cms.web.custom.components;

import com.alodiga.cms.web.utils.WebConstants;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listcell;

public class ListcellEditButton extends Listcell {

    public ListcellEditButton() {
        EditButton editButton = new EditButton();
        editButton.setParent(this);
    }

    public ListcellEditButton(String destinationView, Object obj, Long permissionId) {
        EditButton button = new EditButton(destinationView, obj, permissionId);
        button.setTooltiptext(Labels.getLabel("sp.common.actions.edit"));
        button.setClass("open orange");
        button.setParent(this);
    }

    public ListcellEditButton(String destinationView, Object obj) {
        EditButton button = new EditButton(destinationView, obj);
        Sessions.getCurrent().setAttribute(WebConstants.EVENTYPE, WebConstants.EVENT_EDIT);
        button.setTooltiptext(Labels.getLabel("sp.common.actions.edit"));
        button.setClass("open orange");
        button.setParent(this);
    }


}
