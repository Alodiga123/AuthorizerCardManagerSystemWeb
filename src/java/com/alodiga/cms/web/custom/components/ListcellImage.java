package com.alodiga.cms.web.custom.components;

import org.zkoss.zul.Listcell;

public class ListcellImage extends Listcell {

	public ListcellImage() {
	}

	public ListcellImage(String destinationView) {
		this.setImage(destinationView);
	}
}
