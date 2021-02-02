package com.alodiga.cms.web.utils;

import com.alodiga.cms.commons.ejb.AccessControlEJB;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;


@SuppressWarnings("all")
public class LocationManager {

    private static LocationManager instance;
    AccessControlEJB accessControlEJB;
    

    public static synchronized LocationManager getInstance() throws Exception {
        if (instance == null) {
            instance = new LocationManager();
        }
        return instance;
    }

    public void clear() throws Exception {
        instance = new LocationManager();
    }

    private LocationManager() throws Exception {
        accessControlEJB = (AccessControlEJB) EJBServiceLocator.getInstance().get(EjbConstants.ACCESS_CONTROL_EJB);
     
    }

    public AccessControlEJB getAccessControlEJB() {
        return accessControlEJB;
    }

}
