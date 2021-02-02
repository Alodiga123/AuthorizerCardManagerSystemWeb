package com.alodiga.cms.web.utils;

import java.io.File;

public class WebConstants {

    public static final int EVENT_ADD = 1;
    public static final int EVENT_EDIT = 2;
    public static final int EVENT_DELETE = 3;
    public static final int EVENT_VIEW = 4;
    public static final int REASON_FOUND = 6;
    public static final int STATUS_ACTIVE = 8;
    public static final int STATUS_CUSTOM = 2;
    public static final int STATUS_BLOCKED = 9;
    public static final int EVENT_ADD_DESCENDANT = 5;
    public static final String EVENTYPE = "eventType";
    public static final String OPTION_MENU = "optionMenu";
    public static final String OPTION_LIST = "optionList";
    public static final String IND_PERSON_TYPE_CUSTOMER = "indPersonTypeCustomer";
    public static final String PROGRAM = "program";
    public static final String COUNTRY = "country";
    public static final String PRODUCT = "product";
    public static final String ISSUER = "issuer";
    public static final String CARD = "card";
    public static final String IND_RATE_APPROVE = "indRateApprove";
    public static final String IND_RATE_PROGRAM_APPROVE = "indRateProgramApprove";
    public static final String PRODUCT_TYPE = "productType";
    public static final String IND_OWNER_PROGRAM = "indOwnerProgram";
    public static final String IND_OWNER_PROGRAM_SELECT = "indOwnerProgramSelect";
    public static final String SESSION_ACCOUNT = "sp_account";
    public static final String SESSION_DISTRIBUTOR = "sp_loguedDistributor";
    public static final String SESSION_FULLSITE = "sp_fullSite";
    public static final String SESSION_USER = "sp_user";
    public static final String SESSION_REPORT = "sp_report";
    public static final String SESSION_CUSTOMER = "sp_customer";
    public static final String PHONE_NUMBER_CUSTOMER = "sp_phoneNumber";
    public static final String SESSION_PERMISSION = "sp_permission";
    public static final String SESSION_TRANSACTION = "sp_transaction";
    public static final String NEW_CUSTOMER_SHOP = "sp_new_customer_shop";
    public static final String PREFIX_SOLICITUDE_TYPE = "sp_common.solicitude.type.";
    public static final String PREFIX_PROFILE = "sp.common.profile.";
    public static final String PREFIX_SOLICITUDE_STATUS = "sp.common.solicitude.status.";
    public static final String PREFIX_ACCOUNT = "acc_";
    public static final String PREFIX_DISTRIBUTOR = "dis_";
    public static final String PREFIX_ALL_STORE = "ALL_C";
    public static final String PREFIX_CUSTOMER = "cos_";
    public static final String PREFIX_COMMISSION_CHANGE = "com_";
    public static final String PREFIX_PRODUCT = "pro_";
    public static final String PREFIX_T_STATUS = "tSta_";
    public static final String PREFIX_PROVIDER = "prov__";
    public static final String CONNECTIONPOOL = "jdbc/Multilevelchannel";
    public static final String COLUMN_LABEL = "columnLabel";
    public static final String DATA = "data";
    public static final String ID_PARAM_REPORT_OPEN = "/*<accountSql>*/";
    public static final String ID_PARAM_REPORT_CLOSED = "/*</accountSql>*/";
    public static final int INTEGER = 1;
    public static final int FLOAT = 2;
    public static final int STRING = 3;
    public static final int DATE = 4;
    public static final int PERSON_CLASIFICATION = 7;
    public static final int PERSON_TYPES = 1;
    public static final String ID_MONETARY_TYPE_TRUE = "true";
    public static final String ID_MONETARY_TYPE_FALSE = "false";
    public static final String MAIN_APPLICANT = "Solicitante Principal";
    public static final String CARD_REQUEST_NATURAL_PERSON = "Empleado";
    public static final String LEGAL_REPRESENTATIVE = "Representante Legal";
    public static final int MAIN_CUSTOMER = 1;
    public static final int COMPLEMENTARY_CUSTOMER = 2;
    public static final int NATURAL_CUSTOMER = 1;
    public static final int LEGAL_CUSTOMER = 2;
    
    
    public static final String MENU_SECURITY = "menu.security";
    public static final String MENUITEM_SECURITY_PROFILE = "menu.security.profile";
    public static final String MENUITEM_SECURITY_USER = "menu.security.user";
    public static final String MENUITEM_SECURITY_AUDITORY = "menu.security.audit";
    public static final String MENUITEM_SECURITY_PREFERENCE = "menu.security.preference";
    public static final String MENU_INVENTORY = "menu.inventory";
    public static final String MENUITEM_INVENTORY_PRODUCT = "menu.inventory.product";
    public static final String MENUITEM_INVENTORY_MOBILE_OPERATOR = "menu.inventory.mobileOperator";
    public static final String MENUITEM_INVENTORY_PROVIDER = "menu.inventory.provider";
    public static final String MENUITEM_INVENTORY_TOP_UP_COMMISSION = "menu.inventory.topUp.commission";
    public static final String MENU_CONTENT = "menu.content";
    public static final String MENUITEM_CONTENT_REPORT = "menu.content.report";
    public static final String MENUITEM_CONTENT_PROMOTION = "menu.content.promotion";
    public static final String MENUITEM_CONTENT_PROMOTION_NOTIFICATION = "menu.content.promotion.notification";
    public static final String MENUITEM_CONTENT_SOLICITUDE = "menu.content.solicitude";
    public static final String MENUITEM_CONTENT_NOTIFICATION = "menu.content.notification";
    public static final String MENUITEM_CONTENT_TICKET = "menu.content.ticket";
    public static final String MENUITEM_CONTENT_BANNER = "menu.content.banner";
    public static final String MENU_MANAGEMENT = "menu.management";
    public static final String MENUITEM_MANAGEMENT_CUSTOMER = "menu.management.customer";
    public static final String MENUITEM_MANAGEMENT_ACCOUNT = "menu.management.account";
    public static final String MENUITEM_MANAGEMENT_ACCOUNT_TREE = "menu.management.account.tree";
    public static final String MENUITEM_MANAGEMENT_TRANSACTION = "menu.management.transaction";
    public static final String MENUITEM_MANAGEMENT_CALLS = "menu.management.calls";
    public static final String MENUITEM_MANAGEMENT_AUTOMATIC_SERVICE = "menu.management.automatic.service";
    public static final String MENUITEM_MANAGEMENT_COMMISSIONS = "menu.management.commission";
    public static final String MENUITEM_MANAGEMENT_REPORT = "menu.management.report";
    public static final String MENUITEM_MANAGEMENT_BALANCE_ADJUSTMENT = "menu.security.balanceAdjustment";
    public static final String MENUITEM_MANAGEMENT_SMS_TEST = "menu.security.test.sms";
    public static final String MENUITEM_MANAGEMENT_MAIL_TEST = "menu.security.test.mail";
    public static final String MENUITEM_MANAGEMENT_PROMOTION_AUTOMATIC_SERVICE = "menu.management.promotion.automatic.service";
    public static final String MENUITEM_MANAGEMENT_ALOPOINS_SHOW = "menu.management.alopoints";
    public static final String MENUITEM_MANAGEMENT_COUNTRY = "menu.management.country";
    public static final String MENU_REPORT = "menu.report";
    public static final String MENUITEM_REPORT_SHOW = "menu.module.reports";

    public static final String VIEW = "view";
    public static final String PERMISSION_GROUP = "permission_group";
    public static final String HOME_ADMIN_ZUL = "home-admin.zul";
    public static final String ID_ELEMENT = "id";    

    //CRUD Programs
    public static final String PROGRAM_BRANDED_YES = "Yes";
    public static final String PROGRAM_BRANDED_NO = "No";
    public static final String PROGRAM_BRANDED_SI = "Si";
    public static final String PROGRAM_RELOADABLE_YES = "Yes";
    public static final String PROGRAM_RELOADABLE_SI = "Si";
    public static final String PROGRAM_RELOADABLE_NO = "No";
    public static final String PROGRAM_CASHACCES_YES = "Yes";
    public static final String PROGRAM_CASHACCES_SI = "Si";
    public static final String PROGRAM_PROGRAM_CASHACCES_NO = "No";
    public static final String PROGRAM_INTERNATIONAL_YES = "Yes";
    public static final String PROGRAM_INTERNATIONAL_SI = "Si";
    public static final String PROGRAM_PROGRAM_INTERNATIONAL_NO = "No";
    public static final String PROGRAM_SOURCE_OF_FOUND_OTHER = "Other";
    public static final String PROGRAM_SOURCE_OF_FOUND_OTROS = "Otros";

    public static final String LEGALREPRESENTATIVE_GENDER_FEMALE = "F";
    public static final String LEGALREPRESENTATIVE_GENDER_MALE = "M";
    public static final Boolean IND_NATURAL_PERSON = true;
    public static final Boolean IND_LEGAL_PERSON = false;

    //CRUD Transaction
    public static final String PROGRAM_MONETARY_TYPE_YES = "Yes";
    public static final String PPROGRAM_MONETARY_TYPE_NO = "No";
    public static final String PROGRAM_MONETARY_TYPE_SI = "Si";

    //CRUD ProgramLoyalty
    public static final int STATUS_PROGRAM_LOYALTY_PENDING = 1;
    public static final int STATUS_PROGRAM_LOYALTY_ACTIVE = 2;
    public static final int STATUS_PROGRAM_LOYALTY_INACTIVE = 3;
    
    public static final int PROGRAM_LOYALTY_TYPE_POINT = 1;
    public static final int PROGRAM_LOYALTY_TYPE_BONIFICATION = 2;
    public static final int STATUS_CUSTOMER = 1;
    
    //CRUD PlasticCustomizingRequest
    public static final int STATUS_PLASTIC_CUSTOMIZING_REQUEST_PENDING = 1;

    //CRUD Product
    public static final int VALIDITY_MONTH_24 = 24;
    public static final int VALIDITY_MONTH_36 = 36;
    public static final int VALIDITY_MONTH_48 = 48;
    public static final int PRODUCT_USE_DOMESTIC = 1;
    public static final int PRODUCT_USE_INTERNATIONAL = 2;
    public static final int PRODUCT_USE_BOTH = 3;
    public static final int PRODUCT_STATUS_PENDING = 1;
    public static final int PRODUCT_STATUS_ACTIVATED = 2;
    public static final int PRODUCT_STATUS_INACTIVATED = 3;
   
    //CRUD CardAssigment 
    public static final int CARD_LENGHT_12 = 12;
    public static final int CARD_LENGHT_16 = 16;
    public static final int CARD_LENGHT_18 = 18;
    public static final int CARD_LENGHT_20 = 20;
    
    //CRUD PlasticManufacturer
    public static final int STATUS_PLASTIC_MANUFACTURER_PERSONALIZED = 1;
    public static final int STATUS_PLASTIC_MANUFACTURER_ERROR = 2;
    public static final int STATUS_PLASTIC_MANUFACTURER_PENDING = 3;
    
    //CRUD DeliveryRequest
    public static final int STATUS_DELIVERY_REQUEST_PENDING = 1;
    
    //Address Type
    public static final int ADDRESS_TYPE_COMPANY = 5;
    
    //Tipos de Transacciones
    public static final int RECARGA = 1;
    public static final int RECARGA_INICIAL = 2;
    public static final int RETIRO_DOMESTICO = 3;
    public static final int RETIRO_INTERNACIONAL = 4;
    public static final int COMPRA_DOMESTICA_PIN = 5;
    public static final int COMPRA_INTERNACIONAL_PIN = 6;
    public static final int DEPOSITO = 7;
    public static final int CONSULTA = 8;
    public static final int TRANSFERENCIAS_PROPIAS = 9;
    public static final int ACTIVACION_TARJETA = 10;
    public static final int CREACION_CUENTA = 23;
    public static final int RECARGA_MANUAL = 24;
    public static final int TARIFA_TRANSACCION_CMS = 25;
    
    public static final String GENERAL_RATE= "generalRate";
    
}