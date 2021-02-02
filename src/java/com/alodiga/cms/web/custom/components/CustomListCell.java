package com.alodiga.cms.web.custom.components;

import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public class CustomListCell extends Listcell {
	public static final int TYPE_TEXT = 1;
	public static final int TYPE_INT = 2;
	public InputElement input;
	
	public CustomListCell(String label, String width, int type, boolean readonly){
		super();
		
		switch (type) {
			case TYPE_TEXT:
				input = new Textbox(label);
				break;
			case TYPE_INT:
				input = new Intbox(label!=null?Integer.valueOf(label):0);
				break;
			default:
				input = new Textbox(label);
				break;
		}
		input.setReadonly(readonly);
		input.setWidth(width);
		input.setParent(this);
	}
}
