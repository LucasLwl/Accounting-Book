package com.phone.konka.accountingbook.Utils;

import android.util.Log;

import com.phone.konka.accountingbook.Base.Config;
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
 * 导入导出Excel文件操作
 * <p>
 * Created by 廖伟龙 on 2017/11/29.
 */

public class ExcelUtil {

    /**
     * Excel文件的根路径
     */
    public static final String BILL_PATH = Config.APP_DIR + "/Bill/";


    /**
     * Excel文件的列字段
     */
    public static final String[] TAG_NAME = {"year", "month", "day", "tag", "iconID", "money"};


    /**
     * 导出Excel文件
     *
     * @param excelName
     * @param sheetName
     * @param list
     */
    public static void writeExcel(final String excelName, final String sheetName, final List<DetailTagBean> list) {

        try {
            File dir = new File(BILL_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            String path = BILL_PATH + excelName;
            File file = new File(path);
            file.createNewFile();

            OutputStream os = new FileOutputStream(file);


//            对应Excel文件
            Workbook wb = new HSSFWorkbook();

//            表格
            Sheet sheet = wb.createSheet(sheetName);

//            第一列填写字段名称
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < TAG_NAME.length; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(TAG_NAME[i]);
            }


//            循环每列，填写账单信息
            for (int i = 0; i < list.size(); i++) {
                Row contentRow = sheet.createRow(i + 1);
                DetailTagBean bean = list.get(i);
                for (int j = 0; j < TAG_NAME.length; j++) {
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
                            cell.setCellValue(bean.getIconID());
                            break;
                        case 5:
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


    /**
     * 读取Excel信息
     *
     * @param excelName
     * @return
     */
    public static List<DetailTagBean> readExcel(String excelName) {

        List<DetailTagBean> list = new ArrayList<>();

        try {
            InputStream is = new FileInputStream(excelName);

            POIFSFileSystem poi = new POIFSFileSystem(is);

//            对应Excel文件
            HSSFWorkbook wb = new HSSFWorkbook(poi);

//            Excel表
            Sheet sheet = wb.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();

//            跳过第一行的字段名称
            if (rowIterator.hasNext())
                rowIterator.next();


//            循环每列，读取Excel表格信息
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
                            bean.setIconID((int) cell.getNumericCellValue());
                            break;
                        case 5:
                            bean.setMoney(cell.getNumericCellValue());
                            break;
                    }
                }
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ddd", e.toString());
        }
        return list;
    }
}
