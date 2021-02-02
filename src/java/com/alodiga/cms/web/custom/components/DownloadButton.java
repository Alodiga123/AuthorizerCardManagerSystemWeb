package com.alodiga.cms.web.custom.components;

import java.io.FileInputStream;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;

public class DownloadButton extends Button {

    public DownloadButton() {
        this.setImage("/images/icon-send.png");
    }

    public DownloadButton(final String file) {
        this.setImage("/images/icon-send.png");
        this.addEventListener("onClick", new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                String extension = null;

                FileInputStream is = new FileInputStream(file);

                if (file.contains(".")) {
                    String[] aux = file.split("\\.");
                    extension = aux[1];
                }

                Filedownload.save(is, "image/" + extension, file.split("/")[3]);
            }
        });
    }
}
