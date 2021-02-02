package com.alodiga.cms.web.generic.controllers;

import org.zkoss.zul.Button;

public abstract class GenericAbstractListController<T> extends GenericAbstractController implements GenericListController<T> {
    public Button btnAdd;
    public Button btnDownload;
    public Button btnSeach;
    public Button btnCancel;
    public Button btnProcessRefund;
    public String adminPage = "";
    public String adminInvoices = "";
}
