package com.alodiga.cms.web.custom.components;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Listcell;

public class ListcellDetailsButton extends Listcell {

    public ListcellDetailsButton() {
    }

    public ListcellDetailsButton(String destinationView, Object obj,Long permissionId) {
        DetailsButton button = new DetailsButton(destinationView, obj,permissionId);
        button.setTooltiptext(Labels.getLabel("sp.common.actions.view"));
        button.setClass("open orange");
        button.setParent(this);
    }

    public ListcellDetailsButton(String destinationView, Object obj, String images,Long permissionId) {
        DetailsButton button = new DetailsButton(destinationView, obj,images,permissionId);
        button.setClass("open orange");
        button.setParent(this);
    }
}
