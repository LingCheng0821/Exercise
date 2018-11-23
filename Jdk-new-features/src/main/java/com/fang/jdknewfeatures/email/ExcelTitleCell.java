package com.fang.jdknewfeatures.email;

import java.util.List;

public class ExcelTitleCell {

	private String value;
	private String styleType;

	public ExcelTitleCell() {
	}

	public ExcelTitleCell(String value, String styleType) {
		this.value = value;
		this.styleType = styleType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStyleType() {
		return styleType;
	}

	public void setStyleType(String styleType) {
		this.styleType = styleType;
	}

	@Override
	public String toString() {
		return "ExcelTitle [value=" + value + ", styleType=" + styleType + "]";
	}

	public static void addExcelTitle(List<ExcelTitleCell> cells, String value, String styleType) {
		cells.add(new ExcelTitleCell(value, styleType));
	}
}
