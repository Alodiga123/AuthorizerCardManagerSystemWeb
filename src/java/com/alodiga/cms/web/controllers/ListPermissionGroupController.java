package com.alodiga.cms.web.controllers;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.alodiga.cms.web.custom.components.ListcellEditButton;
import com.alodiga.cms.web.custom.components.ListcellViewButton;
import com.alodiga.cms.web.generic.controllers.GenericAbstractListController;
import com.alodiga.cms.web.utils.Utils;
import com.alodiga.cms.web.utils.WebConstants;
import com.cms.commons.models.PermissionGroup;
import com.cms.commons.models.User;
import com.cms.commons.util.Constants;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ListPermissionGroupController extends GenericAbstractListController<PermissionGroup> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private UtilsEJB utilsEJB = null;
    private List<PermissionGroup> permissionGroupList = null;
    private PermissionGroup currentPermissionGroup;
    private User user = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }
    
       
    @Override
    public void initialize() {
        super.initialize();
        try {
            //Evaluar Permisos
            permissionEdit = true;
            permissionAdd = true;
            permissionRead = true;
            user = (User) session.getAttribute(Constants.USER_OBJ_SESSION);
            adminPage = "adminPermissionGroup.zul";
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            getData();
            loadDataList(permissionGroupList);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
   public void getData() {
    permissionGroupList = new ArrayList<PermissionGroup>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            permissionGroupList = utilsEJB.getPermissionGroup(request);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
        } catch (GeneralException ex) {
            showError(ex);
        }
    }

   public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Sessions.getCurrent().removeAttribute("object");
        Executions.getCurrent().sendRedirect(adminPage);
    }
    
       
   public void onClick$btnDownload() throws InterruptedException {
        try {
            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            StringBuilder file = new StringBuilder(Labels.getLabel("cms.menu.permission.group.list"));
            file.append("_");
            file.append(date);
            Utils.exportExcel(lbxRecords, file.toString());
        } catch (Exception ex) {
            showError(ex);
        }
        
    } 
   
    public void startListener() {
    }

    public void loadDataList(List<PermissionGroup> list) {
        String indEnabled = null;
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                btnDownload.setVisible(true);
                for (PermissionGroup permissionGroup : list) {
                    item = new Listitem();
                    item.setValue(permissionGroup);
                    item.appendChild(new Listcell(permissionGroup.getName()));
                    if (permissionGroup.getEnabled() == true) {
                        indEnabled = "Yes";
                    } else {
                        indEnabled = "No";
                    }                    
                    item.appendChild(new Listcell(indEnabled));
                    item.appendChild(new ListcellEditButton(adminPage, permissionGroup));
                    item.appendChild(new ListcellViewButton(adminPage, permissionGroup,true));
                    item.setParent(lbxRecords);
                }
            } else {
                btnDownload.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);
            }

            } catch (Exception ex) {
            showError(ex);
            }
    }

    @Override
    public List<PermissionGroup> getFilterList(String filter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
