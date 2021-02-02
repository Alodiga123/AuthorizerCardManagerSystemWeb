package com.alodiga.cms.web.custom.components;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Listcell;

public class ListcellAddDescendantButton extends Listcell {

    public ListcellAddDescendantButton() {
    }

    public ListcellAddDescendantButton(String destinationView, Object obj, Long permissionId) {
        AddDescendantButton button = new AddDescendantButton(destinationView, obj,permissionId);
        button.setTooltiptext(Labels.getLabel("sp.common.actions.add.children"));
        button.setClass("open orange");
        button.setParent(this);
    }

    public ListcellAddDescendantButton(String destinationView, Object obj, boolean isRedirect, Long permissionId) {
        AddDescendantButton button = new AddDescendantButton(destinationView, obj,permissionId);
        button.setParent(this);
    }
}
