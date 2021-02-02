package com.alodiga.cms.web.generic.controllers;

import org.zkoss.zul.Window;

public interface GenericAdminController {

    public void initView(int eventType, String view, Window window);
    public void clearFields();
    public void loadData();
    public void blockFields();
    public void showMessage(String message, boolean isError, Exception exception);
    public void clearMessage();
}
