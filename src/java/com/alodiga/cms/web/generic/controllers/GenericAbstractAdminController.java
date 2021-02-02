package com.alodiga.cms.web.generic.controllers;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Window;
import com.alodiga.cms.web.utils.WebConstants;

public abstract class GenericAbstractAdminController extends GenericAbstractController implements GenericAdminController {

    private String typeView = ".view";
    private String typeEdit = ".edit";
    private String typeAdd = ".add";
    
    public void initView(int eventType, String view, Window window) {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                initToEdit(view + typeEdit, window);
                break;
            case WebConstants.EVENT_VIEW:
                initToView(view + typeView, window);
                break;
            case WebConstants.EVENT_ADD:
                initToAdd(view + typeAdd, window);
                break;
            default:
                initToView(view, window);
                break;
        }
    }

    protected void initToView(String view, Window window) {
        window.setTitle(Labels.getLabel(view));
        loadData();
        blockFields();
    }

    protected void initToEdit(String view, Window window) {
        window.setTitle(Labels.getLabel(view));
        loadData();
    }

    protected void initToAdd(String view, Window window) {
        window.setTitle(Labels.getLabel(view));
        loadData();
    }
}
