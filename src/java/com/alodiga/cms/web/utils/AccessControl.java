package com.alodiga.cms.web.utils;


import com.alodiga.cms.commons.ejb.AccessControlEJB;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.RegisterNotFoundException;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Language;
import com.cms.commons.models.User;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zk.ui.Sessions;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;


public class AccessControl {

    private static AccessControlEJB accessEjb = null;
    static Map<String, Object> params = null;
    static EJBRequest request = null;

    private static boolean needUpdate = true;   

    public static boolean hasPermission(String entity, String action) {
        HashMap<String, List<String>> permissionsMap = (HashMap<String, List<String>>) Sessions.getCurrent().getAttribute(WebConstants.SESSION_PERMISSION);
        if (permissionsMap != null && permissionsMap.containsKey(entity)) {
            List<String> permissions = permissionsMap.get(entity);
            if (permissions.contains(action)) {
                return true;
            }
        }
        return false;
    }
    
    public static Long getLanguage() {
        Locale locale = Locales.getCurrent();
        if (locale.getLanguage().equals("es")) {
            return Language.SPANISH;
        } else {
            return Language.ENGLISH;
        }
    }
    
    public static User loadCurrentUser() throws RegisterNotFoundException, GeneralException, Exception {
        return (User) Sessions.getCurrent().getAttribute(WebConstants.SESSION_USER);
    }

    public static void logout() {
        Sessions.getCurrent().removeAttribute(WebConstants.SESSION_ACCOUNT);
        Sessions.getCurrent().removeAttribute(WebConstants.SESSION_CUSTOMER);
        Sessions.getCurrent().removeAttribute(WebConstants.SESSION_PERMISSION);
        Sessions.getCurrent().removeAttribute(WebConstants.SESSION_USER);
    }
}
