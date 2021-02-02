package com.alodiga.cms.web.custom.components;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;


public class DistributionCombobox {

    public DistributionCombobox() {
    }

    public static Combobox fillComponent(Combobox combobox, Map<String, String> values) {
        Set<String> keys = values.keySet();

        for (String key : keys) {
            Comboitem comboitem = new DistributionComboitem();
            String value = values.get(key);
            comboitem.setId(key);
            comboitem.setValue(value);
            comboitem.setParent(combobox);
        }
        return combobox;
    }

   


}
