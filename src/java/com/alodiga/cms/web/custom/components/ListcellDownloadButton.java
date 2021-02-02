package com.alodiga.cms.web.custom.components;

import org.zkoss.zul.Listcell;

public class ListcellDownloadButton extends Listcell {

	public ListcellDownloadButton(){
		
	}
	
	public ListcellDownloadButton(String destinationView) {
        DownloadButton downloadButton = new DownloadButton(destinationView);
        downloadButton.setParent(this);
    }
	
}
