package com.alodiga.cms.web.generic.controllers;


import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.audit.Audit;
import java.util.ArrayList;
import java.util.List;

public interface GenericDistributionController {

    public EJBRequest request = new EJBRequest();
    public Audit audit = new Audit();
    public List<Audit> audits = new ArrayList<Audit>();

    public void initialize();

    //public void loadPermission(AbstractDistributionEntity clazz) throws Exception;

}
