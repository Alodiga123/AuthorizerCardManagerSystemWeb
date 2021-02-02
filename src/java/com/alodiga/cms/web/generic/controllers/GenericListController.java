package com.alodiga.cms.web.generic.controllers;

import java.util.List;


public interface GenericListController<T> {
	public void startListener();
	public List<T> getFilterList(String filter);
	public void loadDataList(List<T> list);
	public void getData();
        public void showMessage(String message, boolean isError, Exception exception);
        public void clearMessage();
}
