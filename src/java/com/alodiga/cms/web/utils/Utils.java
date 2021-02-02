package com.alodiga.cms.web.utils;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.zkoss.util.resource.Labels;


public class Utils {

    public static String getPromotionTypeName(String promotionType) {
        String value = "";
        if (promotionType.equals("INITIAL_PURCHASE")) {
            value = Labels.getLabel("promotionType.initialPurchase");
        } else if (promotionType.equals("ACCOUNT_CREATION")) {
            value = Labels.getLabel("promotionType.accountCreation");
        } else if (promotionType.equals("GOAL_ACCOMPLISHMENT")) {
            value = Labels.getLabel("promotionType.goalAccomplishment");
        }

        return value;
    }

    public static String getStatusName(boolean status) {
        String value = "";
        if (status) {
            value = Labels.getLabel("common.enabled");
        } else {
            value = Labels.getLabel("common.disabled");
        }
        return value;
    }

 
    public static void exportExcel(Listbox box, String nameFile) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("Distribution");
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        HSSFClientAnchor anchor = new HSSFClientAnchor();
        anchor.setAnchor((short) 0, 0, 0, 0, (short) 1, 1, 1023, 255);
        anchor.setAnchorType(2);
        String webPath = Sessions.getCurrent().getWebApp().getRealPath("");
        webPath += "/images/img-alodiga-logo.png";
        try {
            File files = new File(webPath);
            HSSFPicture picture = patriarch.createPicture(anchor, loadPicture(files, workbook));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //nothing
        }
        HSSFRow row = sheet.createRow(0);
        HSSFFont fontRedBold = workbook.createFont();
        HSSFFont fontNormal = workbook.createFont();
        fontRedBold.setColor(HSSFFont.COLOR_RED);
        fontRedBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontNormal.setColor(HSSFFont.COLOR_NORMAL);
        fontNormal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        // Create the style
        HSSFCellStyle cellStyleRedBold = workbook.createCellStyle();
        HSSFCellStyle cellStyleNormal = workbook.createCellStyle();
        cellStyleRedBold.setFont(fontRedBold);
        cellStyleNormal.setFont(fontNormal);

        HSSFRow row2 = sheet.createRow(2);
        HSSFCell cell0 = row2.createCell(0);
        cell0.setCellValue(Labels.getLabel("file.commission.topUp.letterHead1"));

        HSSFRow rowTitle = sheet.createRow(0);
        HSSFCell cellTitle = rowTitle.createCell(4);
        cellTitle.setCellValue(nameFile);
        nameFile += ".xls";
        // headers
        int i = 3;
        row = sheet.createRow(3);
        for (Object head : box.getHeads()) {
            for (Object header : ((Listhead) head).getChildren()) {
                String h = ((Listheader) header).getLabel();
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(cellStyleRedBold);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(h);
                i++;
            }
        }
        // dettaglio
        int x = 4;
        int y = 3;
        for (Object item : box.getItems()) {
            row = sheet.createRow(x);
            y = 3;
            for (Object lbCell : ((Listitem) item).getChildren()) {
                String h;
                h = ((Listcell) lbCell).getLabel();
                HSSFCell cell = row.createCell(y);
                cell.setCellStyle(cellStyleNormal);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(h);
                y++;
            }
            x++;
        }
        FileOutputStream fOut = new FileOutputStream(nameFile);
        // Write the Excel sheet
        workbook.write(fOut);
        fOut.flush();
        // Done deal. Close it.
        fOut.close();
        File file = new File(nameFile);
        Filedownload.save(file, "XLS");
    }

    private static int loadPicture(File path, HSSFWorkbook wb) throws IOException {
        int pictureIndex;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            // read in the image file
            fis = new FileInputStream(path);
            bos = new ByteArrayOutputStream();
            int c;
            // copy the image bytes into the ByteArrayOutputStream
            while ((c = fis.read()) != -1) {
                bos.write(c);
            }
            // add the image bytes to the workbook
            pictureIndex = wb.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return pictureIndex;
    }
}
