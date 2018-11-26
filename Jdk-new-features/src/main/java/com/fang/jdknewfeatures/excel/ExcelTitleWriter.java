package com.fang.jdknewfeatures.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ExcelTitleWriter<T> {

	protected CellStyle columnHeadStyle;
	protected CellStyle vCenterStyle;

	protected ExcelSheet<T> excelSheet;
	protected SXSSFWorkbook wb;
	protected SXSSFSheet sheet;

	private ComplexExcelTitle complexExcelTitle;

	/**
	 * 表单需要设置的行高的映射,key 行索引,value 高度系数,一个倍数,即要将行高设置为原先的多少倍
	 */
	private HashMap<Integer, Double> rowHeightCoefficientMap = new HashMap<>();

	public void setComplexExcelTitle(ComplexExcelTitle complexExcelTitle) {
		this.complexExcelTitle = complexExcelTitle;
	}

	public ExcelSheet<T> getExcelSheet() {
		return excelSheet;
	}

	public void setExcelSheet(ExcelSheet<T> excelSheet) {
		this.excelSheet = excelSheet;
	}

	public SXSSFWorkbook getWb() {
		return wb;
	}

	public void setWb(SXSSFWorkbook wb) {
		this.wb = wb;
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		columnHeadStyle = wb.createCellStyle();
		columnHeadStyle.setFont(font);
		columnHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
		columnHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上下居中
		columnHeadStyle.setLocked(true);
		columnHeadStyle.setWrapText(true);
		// 标题行背景色,意义不大,不建议,故取消
		// columnHeadStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		// columnHeadStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		columnHeadStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		columnHeadStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		columnHeadStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		columnHeadStyle.setBorderTop(CellStyle.BORDER_MEDIUM);

		vCenterStyle = wb.createCellStyle();
		vCenterStyle.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
		vCenterStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
		vCenterStyle.setWrapText(true);// 自动换行
	}

	public SXSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(SXSSFSheet sheet) {
		this.sheet = sheet;
	}

	public HashMap<Integer, Double> getRowHeightCoefficientMap() {
		return rowHeightCoefficientMap;
	}

	public void setRowHeightCoefficient(int rowIndex, double heightCoefficient) {
		this.rowHeightCoefficientMap.put(rowIndex, heightCoefficient);
	}

	public int writerTitle() {
		LinkedHashMap<Integer, LinkedHashMap<Integer, CellRangeAddress>> titleWithCellRangeAddress = complexExcelTitle.getTitleWithCellRangeAddress();
		LinkedHashMap<Integer, String> titleIdNameMap = complexExcelTitle.getTitleIdNameMap();

		rowHeightCoefficientMap.putAll(this.excelSheet.getRowHeightCoefficientMap());

		Row row = null;
		Set<Integer> rowIndexSet = titleWithCellRangeAddress.keySet();

		// sheet.createRow 的时候会覆盖前前面合并单元格设置的边框,所以先创建poi row ,之后再再加设置
		Map<Integer, Row> rowMap = new HashMap<>();
		for (Integer rowIndex : rowIndexSet) {
			row = sheet.createRow(rowIndex);
			rowMap.put(rowIndex, row);

			Double heightCoefficient = rowHeightCoefficientMap.containsKey(rowIndex) ? rowHeightCoefficientMap.get(rowIndex) : 1.0;
			row.setHeight((short) (row.getHeight() * heightCoefficient));
		}

		Cell cell = null;
		String titleName = null;
		CellRangeAddress region = null;
		for (Integer rowIndex : rowIndexSet) {
			row = rowMap.get(rowIndex);

			LinkedHashMap<Integer, CellRangeAddress> linkedHashMap = titleWithCellRangeAddress.get(rowIndex);
			for (Integer titleId : linkedHashMap.keySet()) {
				titleName = titleIdNameMap.get(titleId);
				region = linkedHashMap.get(titleId);
				sheet.addMergedRegion(region);
				cell = row.createCell(region.getFirstColumn());
				if (titleName != null && titleName.trim().matches("^(-?\\d+)(\\.\\d+)?$")) {
					cell.setCellValue(Double.parseDouble(titleName.trim()));
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				} else {
					cell.setCellValue(titleName);
					cell.setCellType(Cell.CELL_TYPE_STRING);
				}
				cell.setCellStyle(columnHeadStyle);
				ExcelUtils.setRegionBorder(CellStyle.BORDER_MEDIUM, region, sheet, wb);
			}
		}
		return rowIndexSet.size();
	};
}
