package com.alodiga.cms.web.custom.components;

import com.alodiga.cms.web.utils.WebConstants;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listcell;

public class ListcellViewButton extends Listcell {

    public ListcellViewButton() {
    }

    public ListcellViewButton(String destinationView, Object obj, Long permissionId) {
        ViewButton button = new ViewButton(destinationView, obj,permissionId);
        button.setTooltiptext(Labels.getLabel("sp.common.actions.view"));
        button.setClass("open orange");
        button.setParent(this);
    }

    public ListcellViewButton(String destinationView, Object obj, boolean isRedirect, Long permissionId) {
        ViewButton viewButton = new ViewButton(destinationView, obj,permissionId);
        viewButton.setTooltiptext(Labels.getLabel("sp.common.actions.view"));
        viewButton.setParent(this);
    }
    
    public ListcellViewButton(String destinationView, Object obj, boolean isRedirect) {
        ViewButton viewButton = new ViewButton(destinationView, obj);
        viewButton.setTooltiptext(Labels.getLabel("sp.common.actions.view"));
        viewButton.setClass("open orange");
        viewButton.setParent(this);
    }
    
    public ListcellViewButton(String destinationView, Object obj) {
        ViewButton viewButton = new ViewButton(destinationView, obj);
        viewButton.setTooltiptext(Labels.getLabel("sp.common.actions.view"));
        Sessions.getCurrent().setAttribute(WebConstants.EVENTYPE, WebConstants.EVENT_VIEW);
        viewButton.setParent(this);
    }
    
}
