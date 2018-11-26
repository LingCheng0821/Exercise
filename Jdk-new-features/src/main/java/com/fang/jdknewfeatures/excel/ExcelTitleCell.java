package com.fang.jdknewfeatures.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelTitleCell {

	private String value;
	private String styleType;

	public static void addExcelTitle(List<ExcelTitleCell> cells, String value, String styleType) {
		cells.add(new ExcelTitleCell(value, styleType));
	}
}
