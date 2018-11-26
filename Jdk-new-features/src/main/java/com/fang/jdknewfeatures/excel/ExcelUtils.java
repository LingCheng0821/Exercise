package com.fang.jdknewfeatures.excel;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.streaming.SheetDataWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <b>
 * 本Excel工具类支持：<br>
 * 1. 多表单导出
 * 2. 隐藏列功能（输入隐藏列标题名，隐藏该列，如果是标题名重复，目前只能隐藏其中第一列）<br>
 * 3. 自定义样式功能，日期样式指定，’|’为分隔符，第一部分为
 * time标识，固定值，第二部分为将字符串解析成日期的格式，第三部分为Excel中显示的日期格式<br>
 * ” time|yyyy-MM-dd hh:mm:ss|yyyy/MM/dd hh:mm:ss”<br>
 * 其他样式直接书写，”string”,”num”,”percent”,”0.0”,”0.00”,以及符合Excel自定义样式规则的任意数字格式<br>
 * 4. 带有合并单元格的复杂表头功能（输入参数主要为，行索引，当前行标题名集合，对应父标题）<br>
 * 5. 自定义表头绘制类，如果需要更复杂的表头，请自定义类继承ExcelTitleWriter，重写writerTitle方法<br>
 * 6. 支持原BI组MonResult 对应的json格式导出，支持带有合并单元格的复杂表头功能，支持MonResult 中定义的datatype 样式
 * </b>
 */
public class ExcelUtils {

  private static final String suffix = ".xlsx";
  private static final String tempDir = "TempExcel";

  private static ThreadLocal<HashMap<String, CellStyle>> threadLocal = new ThreadLocal<HashMap<String, CellStyle>>();


  /**
   * @param request 请求
   * @param response 响应
   * @param dataSheets 多表单对象集合
   * @param fileName excel文件名称<br>
   *            <pre>
   *            // 示例代码
   *            String sheetName = "表单一"; // sheetName 为表单名称
   *            List<ExcelTitleCell> tableTitle = new ArrayList<>(); // tableTitle 为表单标题对象,需要自己添加内容
   *            List<List<String>> data = new ArrayList<>(); // data为表单数据对象,
   *            ExcelSheet<String> sheet = new ExcelSheet<>(sheetName, tableTitle, data);
   *            List<ExcelSheet<String>> dataSheets = new ArrayList<>();
   *            dataSheets.add(sheet);
   *            // 合并单元格参考  {@link Example}, 可直接运行
   *            </pre>
   */
  public static <T> void simpleExportExcelByDataSheets(HttpServletRequest request, HttpServletResponse response,
                                                       List<ExcelSheet<T>> dataSheets, String fileName) {
    long starttime = System.currentTimeMillis();
    System.out.println("开始导出Excel数据：" + starttime);

    String path = request.getServletContext().getRealPath("");

    Properties prop = System.getProperties();
    String os = prop.getProperty("os.name");

    String realPath = "";
    if (os.startsWith("win") || os.startsWith("Win")) {
      realPath = path + tempDir;// windows下
    } else {
      realPath = path.replace("ebcenter.new", "") + tempDir;// Linux下
    }

    File file = new File(realPath);
    if (!file.exists() && !file.isDirectory()) {
      file.mkdir();
    }
    System.err.println("==ExcelUtils.simpleExportExcelByDataSheets.realPath=" + realPath);
    String fileRealName = fileName + "_" + UUID.randomUUID() + suffix;
    try {
      createExcel(dataSheets, realPath + File.separator + fileRealName);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("生成excel失败");
    }
    String excelPath = realPath + File.separator + fileRealName;
    System.out.println("Notice:realPath=" + realPath + " \nfileRealName=" + fileRealName + " \nexcelPath=" + excelPath);

    responseExcel(request, response, realPath, fileRealName);
    long endtime = System.currentTimeMillis();
    System.out.printf("simpleExportExcelByDataSheets 导出Excel完成! 耗费时间 : %.2f%n ", (endtime - starttime) / 1000.0);
  }

  public static <T> void createExcel(List<ExcelSheet<T>> dataSheets, String dstExcelFilename) throws IOException {
    SXSSFWorkbook wb = new SXSSFWorkbook();
    for (int i = 0; i < dataSheets.size(); i++) {
      ExcelSheet<T> excelSheet = dataSheets.get(i);
      SXSSFSheet sheet = (SXSSFSheet) wb.createSheet(excelSheet.getSheetName());
      dealWithSheet(excelSheet, wb, sheet);
    }

    File excelFile = new File(dstExcelFilename);
    if (excelFile.exists()) {
      excelFile.delete();
    }
    FileOutputStream outputStream = new FileOutputStream(excelFile);
    wb.write(outputStream);
    outputStream.close();
    try {
      deleteSXSSFTempFiles(wb);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    threadLocal.remove();
  }

  public static <T> byte[] createExcelStream(ArrayList<ExcelSheet<T>> dataSheets, String dstExcelFilename) throws IOException {
    SXSSFWorkbook wb = new SXSSFWorkbook();
    for (int i = 0; i < dataSheets.size(); i++) {
      ExcelSheet<T> excelSheet = dataSheets.get(i);
      SXSSFSheet sheet = (SXSSFSheet) wb.createSheet(excelSheet.getSheetName());
      dealWithSheet(excelSheet, wb, sheet);
    }

    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    wb.write(byteStream);
    byte[] wbByte = byteStream.toByteArray();
    byteStream.close();
    try {
      deleteSXSSFTempFiles(wb);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    threadLocal.remove();
    return wbByte;
  }

  private static <T> void dealWithSheet(ExcelSheet<T> excelSheet, SXSSFWorkbook wb, SXSSFSheet sheet) {
    Row row = null;
    Cell cell = null;

    List<ExcelTitleCell> tableTitle = excelSheet.getTableTitle();
    int rowIndex = writeTitle(excelSheet, wb, sheet);

    int columnIndex = 0;
    List<List<T>> sheetData = excelSheet.getData();
    System.out.println(sheetData.size());

    SimpleDateFormat sdf = new SimpleDateFormat();
    for (int i = 0; i < sheetData.size(); i++) {
      List<T> rowData = sheetData.get(i);
      row = sheet.createRow(rowIndex);
      columnIndex = 0;
      for (int j = 0; j < tableTitle.size(); j++) {
        T t = rowData.get(j);
        String styleType = tableTitle.get(j).getStyleType();
        styleType = (styleType == null ? "" : styleType.toLowerCase());

        int cellType = getCellType(tableTitle.get(j).getStyleType());
        cell = row.createCell(columnIndex, cellType);

        switch (cellType) {
          case Cell.CELL_TYPE_STRING:
            if (styleType.toLowerCase().startsWith("time")) {
              String parsePattern = styleType.substring(styleType.indexOf("|") + 1, styleType.lastIndexOf("|"));
              if (StringUtils.isBlank(parsePattern)) {
                parsePattern = "yyyy-MM-dd";
              }
              try {
                sdf.applyPattern(parsePattern);
                cell.setCellStyle(getStyle(wb, styleType));
                cell.setCellValue(sdf.parse(String.valueOf(t)));
              } catch (ParseException e) {
                cell.setCellValue(String.valueOf(t));
              }
            } else {
              cell.setCellValue(String.valueOf(t));
            }
            break;
          case Cell.CELL_TYPE_NUMERIC:
            try {
              Double cellValue = Double.parseDouble(String.valueOf(t));
              switch (styleType) {
                case "0":
                case "int":
                  styleType = "0";
                  cell.setCellStyle(getStyle(wb, styleType));
                  cell.setCellValue(cellValue);
                  break;
                case "thousand":
                  styleType = "#,##0";
                  cell.setCellStyle(getStyle(wb, styleType));
                  cell.setCellValue(cellValue);
                  break;
                case "0.00":
                case "double":
                  styleType = "0.00";
                  cell.setCellStyle(getStyle(wb, styleType));
                  cell.setCellValue(cellValue);
                  break;
                case "0.0":
                  cell.setCellStyle(getStyle(wb, styleType));
                  cell.setCellValue(cellValue);
                  break;
                case "0.00%":
                case "percent":
                case "percentage":
                  styleType = "0.00%";
                  cell.setCellStyle(getStyle(wb, styleType));
                  cell.setCellValue(cellValue);
                  break;
                default:
                  cell.setCellValue(cellValue);
                  break;
              }
            } catch (NumberFormatException e) {
              cell.setCellType(Cell.CELL_TYPE_BLANK);
              cell.setCellValue(String.valueOf(t));
            }
            break;
          default:
            cell.setCellValue(String.valueOf(t));
            break;
        }

        columnIndex++;
      }
      rowIndex++;
    }

    List<Integer> hiddenColumns = excelSheet.getHiddenColumnIndexList();
    if (hiddenColumns != null) {
      for (int i : hiddenColumns) {
        sheet.setColumnHidden(i, true);
      }
    }

  }

  private static CellStyle getStyle(SXSSFWorkbook wb, String styleType) {
    CellStyle cellStyle;
    if (styleType.contains("|")) {
      styleType = styleType.substring(styleType.lastIndexOf("|") + 1, styleType.length());
    }
    HashMap<String, CellStyle> styleMap = threadLocal.get();
    if (styleMap == null) {
      styleMap = new HashMap<>();
      threadLocal.set(styleMap);
      cellStyle = wb.createCellStyle();
      cellStyle.setDataFormat(wb.createDataFormat().getFormat(styleType));
      styleMap.put(styleType, cellStyle);
    } else {
      cellStyle = styleMap.get(styleType);
      if (cellStyle == null) {
        cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.createDataFormat().getFormat(styleType));
        styleMap.put(styleType, cellStyle);
      }
    }
    return cellStyle;
  }

  private static <T> int writeTitle(ExcelSheet<T> excelSheet, SXSSFWorkbook wb, SXSSFSheet sheet) {
    List<ExcelTitleCell> tableTitle = excelSheet.getTableTitle();
    int rowIndex = 0;

    ExcelTitleWriter<T> titleWriter = excelSheet.getTitleWriter();
    if (titleWriter == null) {
      int columnIndex = 0;
      Row row = null;
      Cell cell = null;
      row = sheet.createRow(rowIndex);
      for (int i = 0; i < tableTitle.size(); i++) {
        ExcelTitleCell titleCell = tableTitle.get(i);
        cell = row.createCell(columnIndex, Cell.CELL_TYPE_STRING);
        String titleValue = titleCell.getValue();
        cell.setCellValue(titleValue);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        columnIndex++;
      }
      rowIndex++;
    } else {
      titleWriter.setWb(wb);
      titleWriter.setSheet(sheet);
      titleWriter.setExcelSheet(excelSheet);
      rowIndex = titleWriter.writerTitle();
    }
    return rowIndex;
  }

  private static int getCellType(String styleType) {
    int cellType = 0;
    styleType = (styleType == null ? "string" : styleType.toLowerCase());
    switch (styleType) {
      case "string":
        cellType = Cell.CELL_TYPE_STRING;
        break;
      case "0.0":
      case "0.00":
      case "int":
      case "num":
      case "double":
      case "0.00%":
      case "percent":
      case "percentage":
      case "thousand":
        cellType = Cell.CELL_TYPE_NUMERIC;
        break;
      default:
        cellType = Cell.CELL_TYPE_STRING;
        break;
    }
    return cellType;
  }

  /**
   * 合并单元格加边框工具类
   *
   * @param border
   * @param region
   * @param sheet
   * @param wb
   */
  public static void setRegionBorder(int border, CellRangeAddress region, Sheet sheet, Workbook wb) {
    RegionUtil.setBorderBottom(border, region, sheet, wb);
    RegionUtil.setBorderLeft(border, region, sheet, wb);
    RegionUtil.setBorderRight(border, region, sheet, wb);
    RegionUtil.setBorderTop(border, region, sheet, wb);
  }

  /**
   * Returns a private attribute of a class
   *
   * @param containingClass
   *            The class that contains the private attribute to retrieve
   * @param fieldToGet
   *            The name of the attribute to get
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

  /**
   * Deletes all temporary files of the SXSSFWorkbook instance
   *
   * @param workbook
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   */
  public static void deleteSXSSFTempFiles(SXSSFWorkbook workbook) throws NoSuchFieldException, IllegalAccessException {

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

  // 该方法可以通过文件名返回excel文件给客户端浏览器
  public static void responseExcel(HttpServletRequest request, HttpServletResponse response, String path,
                                   String fileName) {
    try {
      String temp = fileName;
      fileName = fileName.substring(0, fileName.lastIndexOf("_")) + suffix;
      response.setContentType("application/msexcel;charset=utf-8");
      response.setHeader("Content-disposition",
       "attachment;filename=" + new String(fileName.getBytes("GBK"), "ISO8859_1"));

      ServletOutputStream out = response.getOutputStream();
      BufferedInputStream bis = null;
      BufferedOutputStream bos = null;
      try {
        bis = new BufferedInputStream(new FileInputStream(new File(path + File.separator + temp)));
        bos = new BufferedOutputStream(out);
        byte[] buff = new byte[2048];
        int bytesRead = 0;
        // Simple read/write loop.
        while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
          bos.write(buff, 0, bytesRead);
        }
        bos.flush();

      } catch (final IOException e) {
        throw e;
      } finally {
        if (bis != null) {
          bis.close();
        }
        if (bos != null) {
          bos.close();
        }
        System.out.println(path + File.separator + temp);
        if (deleteFile(path + File.separator + temp)) {
          System.out.println("删除成功");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean deleteFile(String sPath) {
    boolean flag = false;
    File file = new File(sPath);
    // 路径为文件且不为空则进行删除
    if (file.isFile() && file.exists()) {
      file.delete();
      flag = true;
    }
    return flag;
  }
}

