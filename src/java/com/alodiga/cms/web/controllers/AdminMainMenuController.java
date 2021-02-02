package com.alodiga.cms.web.controllers;

import com.alodiga.cms.commons.managers.PermissionManager;
import com.alodiga.cms.web.utils.AccessControl;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.models.Permission;
import com.cms.commons.models.PermissionGroup;
import com.cms.commons.models.User;
import com.cms.commons.util.Constants;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listgroup;
import org.zkoss.zul.Listitem;

public class AdminMainMenuController extends GenericForwardComposer {

    private static final long serialVersionUID = -9145887024839938515L;
    User currentuser = null;
    Listcell ltcFullName;
    Listcell ltcProfile;
    Listcell ltcLogin;
    private static String OPTION = "option";
    private static String OPTION_NONE = "none";
    private static String OPTION_CUSTOMERS_LIST = "ltcCustomerList";
    private List<Permission> permissions;
    private List<PermissionGroup> permissionGroups;
    private List<PermissionGroup> pGroups;
    private PermissionManager pm = null;
    private Listbox lbxPermissions;
    private Long languageId;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    public void initialize() {
        try {
            pm = PermissionManager.getInstance();
            languageId = AccessControl.getLanguage();
            loadPemissions();
            loadAccountData();
            checkOption();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkOption() {

    }
    
        private void loadPemissions() {
        try {
            permissions = pm.getPermissions();
            if (permissions != null && !permissions.isEmpty()) {
                loadMenu();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private Permission loadPermission(Long permissionId) {
        for (Permission p : permissions) {
            if (p.getId().equals(permissionId)) {
                return p;
            }
        }
        return null;
    }
    
    private void loadMenu() {
        try {
            pGroups = new ArrayList<PermissionGroup>();
            permissionGroups = pm.getPermissionGroups();
            for (PermissionGroup pg : permissionGroups) {
                if (existPermissionInGroup(permissions, pg.getId())) {
                    pGroups.add(pg);
                }
            }

            if (!pGroups.isEmpty()) {
                for (PermissionGroup pg : pGroups) {
                    switch (pg.getId().intValue()) {
                        case 1://Secutiry Management
                            loadSecurityManagementGroup(pg);
                            break;
                        case 10://Configuration Menu
                            loadConfigurationMenuGroup(pg);
                            break; 
                        default:
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
       
    private void loadSecurityManagementGroup(PermissionGroup permissionGroup) {
        Listgroup listgroup = createListGroup(permissionGroup);
        createCell(Constants.LIST_USER, "listUser.zul", permissionGroup, listgroup);
        createCell(Constants.LIST_EMPLOYEE, "listEmployee.zul", permissionGroup, listgroup);
        createCell(Constants.LIST_PASSWORD_CHANGE_REQUEST, "listPasswordChangeRequest.zul", permissionGroup, listgroup);    
        createCell(Constants.LIST_PROFILE, "listProfile.zul", permissionGroup, listgroup);    
        createCell(Constants.LIST_PROFILE_LANGUAGE, "listProfileData.zul", permissionGroup, listgroup);
        createCell(Constants.LIST_USER_PROFILES, "listUserHasProfile.zul", permissionGroup, listgroup);
    }
    
    private void loadConfigurationMenuGroup(PermissionGroup permissionGroup) {
        Listgroup listgroup = createListGroup(permissionGroup);
        createCell(Constants.LIST_SYSTEM_OPTIONS, "listPermission.zul", permissionGroup, listgroup);        
        createCell(Constants.LIST_SYSTEM_OPTIONS_LANGUAGE, "listPermissionData.zul", permissionGroup, listgroup);  
        createCell(Constants.LIST_MENU_OPTIONS, "listPermissionGroup.zul", permissionGroup, listgroup);  
        createCell(Constants.LIST_MENU_OPTIONS_LANGUAGE, "listPermissionGroupData.zul", permissionGroup, listgroup);    
    }
    
    private void createCell(Long permissionId, String view, PermissionGroup permissionGroup, Listgroup listgroup) {
        Permission permission = loadPermission(permissionId);
        if (permission != null) {
            Listitem item = new Listitem();
            Listcell listCell = new Listcell();
            listCell.setLabel(permission.getPermissionDataByLanguageId(languageId).getAlias());
            listCell.addEventListener("onClick", new RedirectListener(view, permissionId, permissionGroup));
            listCell.setId(permission.getId().toString());
            if (Sessions.getCurrent().getAttribute(WebConstants.VIEW) != null && (Sessions.getCurrent().getAttribute(WebConstants.VIEW).equals(view))) {
                if ((!WebConstants.HOME_ADMIN_ZUL.equals("/" + view))) {
                    listgroup.setOpen(true);
                    listCell.setStyle("background-color: #D8D8D8");
                    listCell.setLabel(">> " + listCell.getLabel());
                }
            }
            listCell.setParent(item);
            item.setParent(lbxPermissions);
        }
    }
    
    private Listgroup createListGroup(PermissionGroup permissionGroup) {
        Listgroup listgroup = new Listgroup();
        listgroup.setOpen(false);
        Listcell listcell = new Listcell();
        listcell.setLabel(permissionGroup.getPermissionGroupDataByLanguageId(languageId).getAlias());
        listcell.setParent(listgroup);
        listgroup.setParent(lbxPermissions);
        return listgroup;
    }
    
    private boolean existPermissionInGroup(List<Permission> ps, Long permissionGroupId) {
        for (Permission p : ps) {
            if (p.getPermissionGroupId().getId().equals(permissionGroupId)) {
                return true;
            }
        }
        return false;
    }
    
    class RedirectListener implements EventListener {

        private String view = null;
        private Long permissionId = null;
        private PermissionGroup permissionGroup;

        public RedirectListener() {
        }

        public RedirectListener(String view, Long permissionId, PermissionGroup permissionGroup) {
            this.view = view;
            this.permissionId = permissionId;
            this.permissionGroup = permissionGroup;
        }
        
        @Override
        public void onEvent(Event event) throws UiException, InterruptedException {
            Executions.sendRedirect(view);
            Sessions.getCurrent().setAttribute(WebConstants.VIEW, view);
            Sessions.getCurrent().setAttribute(WebConstants.PERMISSION_GROUP, permissionGroup.getId());
        }
    }
    
    

    private void loadAccountData() {
        try {
            currentuser = AccessControl.loadCurrentUser();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        User user = (User) session.getAttribute(Constants.USER_OBJ_SESSION);
        ltcFullName.setLabel(user.getFirstNames() + " " + user.getLastNames());
        //TODO:
        ltcProfile.setLabel("Administrador");
        ltcLogin.setLabel(user.getLogin());    
        
    }

    public void onEvent(Event event) throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

class RedirectListener implements EventListener {

    private String view = null;
    private Long permissionId = null;

    public RedirectListener() {
    }

    public RedirectListener(String view, Long permissionId) {
        this.view = view;
        this.permissionId = permissionId;
    }

    public RedirectListener(String view) {
        this.view = view;
    }

    @Override
    public void onEvent(Event event) throws UiException, InterruptedException {
        Executions.sendRedirect(view);
    }
}
