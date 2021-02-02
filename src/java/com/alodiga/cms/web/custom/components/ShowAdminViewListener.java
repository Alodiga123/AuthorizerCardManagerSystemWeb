package com.alodiga.cms.web.custom.components;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class ShowAdminViewListener implements EventListener {

    private int eventType = -1;
    private String view = null;
    private Long permissionId = null;
    private Object object = null;

    public ShowAdminViewListener() {
    }

    public ShowAdminViewListener(String view) {
        this.view = view;
    }

    public ShowAdminViewListener(int eventType, String view, Object o,Long permissionId) {
        this.eventType = eventType;
        this.view = view;
        this.object = o;
        this.permissionId = permissionId;
    }
  
    public ShowAdminViewListener(int eventType, String view, Object o) {
        this.eventType = eventType;
        this.view = view;
        this.object = o;
    }

    public void onEvent(Event event) throws UiException, InterruptedException {
        Sessions.getCurrent().setAttribute("object", this.object);
        Sessions.getCurrent().setAttribute("eventType", eventType);
   //     AccessControl.saveAction(permissionId, view);
        Executions.sendRedirect(view);
    }
}
