package com.fang.jdknewfeatures.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExcelSheet<T> {
	private String sheetName;
	private List<List<T>> data;
	private List<ExcelTitleCell> tableTitle;
	private List<Integer> hiddenColumnIndexList;
	private List<String> hiddenColumnNameList;
	private ExcelTitleWriter<T> titleWriter;

	/**
	 * 表单需要设置的行高的映射,key 行索引,value 高度系数,一个倍数,即要将行高设置为原先的多少倍
	 */
	private HashMap<Integer, Double> rowHeightCoefficientMap = new HashMap<>();

	public ExcelSheet() {
		super();
	}

	public ExcelSheet(String sheetName, List<ExcelTitleCell> tableTitle, List<List<T>> data) {
		this.sheetName = sheetName;
		this.tableTitle = tableTitle;
		this.data = data;
	}

	public ExcelSheet(String sheetName, List<ExcelTitleCell> tableTitle, List<List<T>> data, ExcelTitleWriter<T> titleWriter) {
		this.sheetName = sheetName;
		this.tableTitle = tableTitle;
		this.data = data;
		this.titleWriter = titleWriter;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<List<T>> getData() {
		return data;
	}

	public void setData(List<List<T>> data) {
		this.data = data;
	}

	public List<ExcelTitleCell> getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(List<ExcelTitleCell> tableTitle) {
		this.tableTitle = tableTitle;
	}

	public List<Integer> getHiddenColumnIndexList() {
		if (hiddenColumnNameList != null) {
			hiddenColumnIndexList = new ArrayList<>();
			List<String> computeTableTitleNameList = new ArrayList<>();
			for (int i = 0; i < tableTitle.size(); i++) {
				computeTableTitleNameList.add(tableTitle.get(i).getValue());
			}

			for (String hiddenColumnName : hiddenColumnNameList) {
				if (hiddenColumnName != null) {
					int indexOf = computeTableTitleNameList.indexOf(hiddenColumnName);
					computeTableTitleNameList.set(indexOf, null);
					hiddenColumnIndexList.add(indexOf);
				}
			}
		}
		return hiddenColumnIndexList;
	}

	public List<String> getHiddenColumnNameList() {
		return hiddenColumnNameList;
	}

	public void setHiddenColumnNameList(List<String> hiddenColumnNameList) {
		this.hiddenColumnNameList = hiddenColumnNameList;
	}

	public ExcelTitleWriter<T> getTitleWriter() {
		return titleWriter;
	}

	public void setTitleWriter(ExcelTitleWriter<T> titleWriter) {
		this.titleWriter = titleWriter;
	}

	public HashMap<Integer, Double> getRowHeightCoefficientMap() {
		return rowHeightCoefficientMap;
	}

	public void setRowHeightCoefficient(int rowIndex, double heightCoefficient) {
		this.rowHeightCoefficientMap.put(rowIndex, heightCoefficient);
	}

	@Override
	public String toString() {
		return "ExcelSheet [sheetName=" + sheetName + ", data=" + data + ", tableTitle=" + tableTitle + "]";
	}

}
