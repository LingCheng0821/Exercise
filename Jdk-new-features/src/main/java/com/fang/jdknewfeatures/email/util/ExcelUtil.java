package com.fang.jdknewfeatures.email.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.streaming.SheetDataWriter;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: bi-cloud
 * @Date: 2018/11/20 17:39
 * @Author: chengling.bj
 * @Description: 导出 excel
 *
 */

public class ExcelUtil {
  private static ThreadLocal<HashMap<String, CellStyle>> threadLocal = new ThreadLocal<HashMap<String, CellStyle>>();

  public static <T> void createExcel(List<T> list, ExcelDataFormatter edf, String filePath, String sheetName) throws Exception {
    // 创建并获取工作簿对象
    Workbook wb = getWorkBook(list, edf, sheetName);
    // 写入到文件
    File excelFile = new File(filePath);
    if (excelFile.exists()) {
      excelFile.delete();
    }
    FileOutputStream out = new FileOutputStream(excelFile);
    wb.write(out);
    out.close();
    try {
      deleteSXSSFTempFiles(wb);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    threadLocal.remove();
  }

  private static <T> Workbook getWorkBook(List<T> list, ExcelDataFormatter edf, String sheetName) throws Exception {
    // 创建工作簿
    Workbook wb = new SXSSFWorkbook();

    if (list == null || list.size() == 0) {
      return wb;
    }

    // 创建一个工作表sheet
    Sheet sheet = wb.createSheet(sheetName);

    dealWithSheet(list, edf, wb, sheet);

    return wb;
  }


  private static <T> void dealWithSheet(List<T> list, ExcelDataFormatter edf, Workbook wb, Sheet sheet) throws Exception {

    // 申明行
    Row row = sheet.createRow(0);

    // 申明单元格
    Cell cell = null;

    CreationHelper createHelper = wb.getCreationHelper();

    Field[] fields = list.get(0).getClass().getDeclaredFields();

    XSSFCellStyle titleStyle = (XSSFCellStyle) wb.createCellStyle();
    titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    // 设置前景色
    titleStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(159, 213, 183)));
    titleStyle.setAlignment(HorizontalAlignment.CENTER);

    Font font = wb.createFont();
    // 设置字体
    titleStyle.setFont(font);

    int columnIndex = 0;
    Excel excel = null;
    for (Field field : fields) {
      field.setAccessible(true);
      excel = field.getAnnotation(Excel.class);
      if (excel == null ) {
        continue;
      }
      // 列宽注意乘256
      sheet.setColumnWidth(columnIndex, excel.width() * 256);
      // 写入标题
      cell = row.createCell(columnIndex);
      cell.setCellStyle(titleStyle);
      cell.setCellValue(excel.name());

      columnIndex++;
    }

    int rowIndex = 1;


    for (T t : list) {
      row = sheet.createRow(rowIndex);
      columnIndex = 0;
      Object o = null;
      for (Field field : fields) {
        CellStyle cs = wb.createCellStyle();
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        field.setAccessible(true);
        // 忽略标记skip的字段
        excel = field.getAnnotation(Excel.class);
        if (excel == null ) {
          continue;
        }
        // 数据
        cell = row.createCell(columnIndex);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        o = field.get(t);
        // 如果数据为空，跳过
        if (o == null) {
          continue;
        }
        // 处理日期类型
        if (o instanceof Date) {
          cs.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
          cell.setCellValue((Date) field.get(t));
        } else if (o instanceof Double || o instanceof Float) {
          cell.setCellValue((Double) field.get(t));
        } else if (o instanceof Boolean) {
          Boolean bool = (Boolean) field.get(t);
          if (edf == null) {
            cell.setCellValue(bool);
          } else {
            Map<String, String> map = edf.get(field.getName());
            if (map != null && map.get(bool.toString().toLowerCase()) != null) {
              cell.setCellValue(map.get(bool.toString().toLowerCase()));
            } else {
              cell.setCellValue(bool);
            }
          }

        } else if (o instanceof Integer) {

          Integer intValue = (Integer) field.get(t);

          if (edf == null) {
            cell.setCellValue(intValue);
          } else {
            Map<String, String> map = edf.get(field.getName());
            if (map != null && map.get(intValue.toString()) != null) {
              cell.setCellValue(map.get(intValue.toString()));
            } else {
              cell.setCellValue(intValue);
            }
          }
        } else if (o instanceof String) {
          String intValue = (String) field.get(t);
          if (edf == null) {
            cell.setCellValue(intValue);
          } else {
            Map<String, String> map = edf.get(field.getName());
            if (map != null && map.get(intValue) != null) {
              cell.setCellValue(map.get(intValue));
            } else {
              cell.setCellValue(intValue);
            }
          }
        } else {
          cell.setCellValue(field.get(t).toString());
        }
        cell.setCellStyle(cs);
        columnIndex++;
      }

      rowIndex++;
    }
  }


  /**
   * Deletes all temporary files of the SXSSFWorkbook instance
   *
   * @param workbook
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   */
  public static void deleteSXSSFTempFiles(Workbook workbook) throws NoSuchFieldException, IllegalAccessException {

    int numberOfSheets = workbook.getNumberOfSheets();

    // iterate through all sheets (each sheet as a temp file)
    for (int i = 0; i < numberOfSheets; i++) {
      Sheet sheetAt = workbook.getSheetAt(i);

      // delete only if the sheet is written by stream
      if (sheetAt instanceof SXSSFSheet) {
        SheetDataWriter sdw = (SheetDataWriter) getPrivateAttribute(sheetAt, "_writer");
        File f = (File) getPrivateAttribute(sdw, "_fd");

        try {
          f.delete();
        } catch (Exception ex) {
          // could not delete the file
        }
      }
    }
  }

  /**
   * Returns a private attribute of a class
   *
   * @param containingClass The class that contains the private attribute to retrieve
   * @param fieldToGet      The name of the attribute to get
   * @return The private attribute
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   */
  public static Object getPrivateAttribute(Object containingClass, String fieldToGet) throws NoSuchFieldException, IllegalAccessException {
    // get the field of the containingClass instance
    Field declaredField = containingClass.getClass().getDeclaredField(fieldToGet);
    // set it as accessible
    declaredField.setAccessible(true);
    // access it
    Object get = declaredField.get(containingClass);
    // return it!
    return get;
  }
}
