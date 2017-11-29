package com.phone.konka.accountingbook.Utils;

import android.os.Environment;
import android.util.Log;

import com.phone.konka.accountingbook.Bean.DetailTagBean;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/29.
 */

public class ExcelUtil {

    public static final String BILL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Bill/";
    public static final String[] TAG_NAME = {"year", "month", "day", "tag", "money"};


    public static void writeExcel(String excelName, String sheetName, List<DetailTagBean> list) {

        try {

            File dir = new File(BILL_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String path = BILL_PATH + excelName;
            File file = new File(path);
            file.createNewFile();

            OutputStream os = new FileOutputStream(file);

            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet(sheetName);

            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < 5; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(TAG_NAME[i]);
            }

            for (int i = 0; i < list.size(); i++) {
                Row contentRow = sheet.createRow(i + 1);
                DetailTagBean bean = list.get(i);
                for (int j = 0; j < 5; j++) {
                    Cell cell = contentRow.createCell(j);
                    switch (j) {
                        case 0:
                            cell.setCellValue(bean.getYear());
                            break;
                        case 1:
                            cell.setCellValue(bean.getMonth());
                            break;
                        case 2:
                            cell.setCellValue(bean.getDay());
                            break;
                        case 3:
                            cell.setCellValue(bean.getTag());
                            break;
                        case 4:
                            cell.setCellValue(bean.getMoney());
                            break;
                    }
                }
            }

            wb.write(os);
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<DetailTagBean> readExcel(String excelName) {

        List<DetailTagBean> list = new ArrayList<>();


        try {
            InputStream is = new FileInputStream(BILL_PATH + excelName);
            POIFSFileSystem poi = new POIFSFileSystem(is);

            HSSFWorkbook wb = new HSSFWorkbook(poi);
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            if (rowIterator.hasNext())
                rowIterator.next();
            while (rowIterator.hasNext()) {
                DetailTagBean bean = new DetailTagBean();
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                for (int i = 0; cellIterator.hasNext(); i++) {
                    Cell cell = cellIterator.next();
                    switch (i) {
                        case 0:
                            bean.setYear((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            bean.setMonth((int) cell.getNumericCellValue());
                            break;
                        case 2:
                            bean.setDay((int) cell.getNumericCellValue());
                            break;
                        case 3:
                            bean.setTag(cell.getStringCellValue());
                            break;
                        case 4:
                            bean.setMoney(cell.getNumericCellValue());
                            break;
                    }
                }
                list.add(bean);
            }

        } catch (Exception e) {

            Log.i("ddd", "捕获到异常:" + e);
            e.printStackTrace();
        }

        return list;

    }


}
