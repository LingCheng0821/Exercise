package com.fang.jdknewfeatures.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Example {

	public static final String path = System.getProperty("user.dir");

	public static void main(String[] args) throws IOException {
		String dstExcelFilename = null;
		dstExcelFilename = path + "/src/main/java/com/fang/ebcenter/util/excel/考勤分析报表.xlsx";
		test01(dstExcelFilename);

		dstExcelFilename = path + "/src/main/java/com/fang/ebcenter/util/excel/打卡分析报表.xlsx";
		test02(dstExcelFilename);
	}

	public static void test02(String dstExcelFilename) throws IOException {
		String sheetName = "打卡分析报表";

		List<ExcelTitleCell> tableTitle = new ArrayList<>();

		String[] heads = { "人员id", "城市", "集团", "分部", "部门", "姓名", "邮箱", "状态", "请假（次）", "因公外出（次）", "正常（次）", "全勤天数", "出勤天数", "日均工作时长", "迟到(次)", "严重迟到(次)", "早退(次)", "严重早退（次）", "上午缺卡", "下午缺卡", "一天缺卡" };
		String[] types = { "num", "string", "string", "string", "string", "string", "string", "string", "num", "num", "num", "num", "0.0", "0.0", "num", "num", "num", "num", "num", "num", "num" };
		List<String> headList = new ArrayList<>();
		List<String> typeList = new ArrayList<>(headList.size());
		Collections.addAll(headList, heads);
		Collections.addAll(typeList, types);

		List<String> workDateDayList = Arrays.asList("20170313", "20170314", "20170315", "20170316", "20170317");
		for (int i = 0; i < headList.size(); i++) {
			if (headList.get(i).equals("状态")) {
				ExcelTitleCell.addExcelTitle(tableTitle, headList.get(i), typeList.get(i));
				for (int j = 0; j < workDateDayList.size(); j++) {
					ExcelTitleCell.addExcelTitle(tableTitle, "上午", "string");
					ExcelTitleCell.addExcelTitle(tableTitle, "下午", "string");
				}
			} else {
				ExcelTitleCell.addExcelTitle(tableTitle, headList.get(i), typeList.get(i));
			}
		}

		List<List<String>> data = new ArrayList<>();

		ExcelTitleWriter<String> titleWriter = new ExcelTitleWriter<>();
		ComplexExcelTitle complexExcelTitle = new ComplexExcelTitle();
		complexExcelTitle.addTitleRow(0, Arrays.asList("打卡分析汇总表"), null);
		complexExcelTitle.addTitleRow(1, Arrays.asList("统计日期：20170313～20170317    报表生成时间：2017-03-27 13:38:01"), "打卡分析汇总表");
		complexExcelTitle.addTitleRow(2,
				Arrays.asList("人员id", "城市", "集团", "分部", "部门", "姓名", "邮箱", "状态", "日期", "请假（次）", "因公外出（次）", "正常（次）", "全勤天数", "出勤天数", "日均工作时长", "迟到(次)", "严重迟到(次)", "早退(次)", "严重早退（次）", "旷工(天)"),
				"统计日期：20170313～20170317    报表生成时间：2017-03-27 13:38:01");
		complexExcelTitle.addTitleRow(3, Arrays.asList("20170313", "20170314", "20170315", "20170316", "20170317"), "日期");
		complexExcelTitle.addTitleRow(3, Arrays.asList("上午缺卡", "下午缺卡", "一天缺卡"), "旷工(天)");
		complexExcelTitle.addTitleRow(4, Arrays.asList("上午", "下午"), "20170313");
		complexExcelTitle.addTitleRow(4, Arrays.asList("上午", "下午"), "20170314");
		complexExcelTitle.addTitleRow(4, Arrays.asList("上午", "下午"), "20170315");
		complexExcelTitle.addTitleRow(4, Arrays.asList("上午", "下午"), "20170316");
		complexExcelTitle.addTitleRow(4, Arrays.asList("上午", "下午"), "20170317");
		titleWriter.setComplexExcelTitle(complexExcelTitle);

		ExcelSheet<String> sheet = new ExcelSheet<>(sheetName, tableTitle, data, titleWriter);
		ArrayList<ExcelSheet<String>> dataSheets = new ArrayList<>();
		dataSheets.add(sheet);
		ExcelUtils.createExcel(dataSheets, dstExcelFilename);
	}

	public static void test01(String dstExcelFilename) throws IOException {
		String sheetName = "考勤分析报表";
		List<ExcelTitleCell> tableTitle = new ArrayList<>();
		String[] heads = { "员工ID", "公司", "部门", "姓名", "状态", "公司Email", "身份证号",
				// "考勤开始日期", "考勤结束日期",
				"全勤天数", "出勤天数", "入离职缺勤天数", "迟到早退≤1小时次数", "迟到早退＞1小时次数", "实际旷工天数", "总旷工天数",
				"病假", "事假", "年假", "婚假", "产假", "小产假", "调休", "产检", "丧假", "陪产假", "哺乳假", "因公外出", "出差",
				"病假", "事假", "年假", "婚假", "产假", "小产假", "调休", "产检", "丧假", "陪产假", "因公外出", "出差",
				//	"病假", "事假", "年假", "婚假", "产假", "小产假", "调休", "产检", "丧假", "陪产假", "哺乳假", "因公外出", "出差",
				"工资补差", "餐补补差",
				//	"上月补齐工作日天数", "本月预发工作日天数", "工资周期实际预发天数", 
				"日均出勤小时数" };
		String[] types = { "num", "string", "string", "string", "string", "string", "string",
				//"num", "num",
				"num", "0.00", "num", "num", "num", "0.00", "0.00",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
				//	"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
				"0.00", "0.00",
				//	"num", "num", "num", 
				"0.00" };
		List<String> headList = Arrays.asList(heads);
		List<String> typeList = Arrays.asList(types);
		for (int j = 0; j < headList.size(); j++) {
			ExcelTitleCell.addExcelTitle(tableTitle, headList.get(j), typeList.get(j));
		}

		List<List<String>> data = new ArrayList<>();

		ExcelTitleWriter<String> titleWriter = new ExcelTitleWriter<>();
		ComplexExcelTitle complexExcelTitle = new ComplexExcelTitle();
		complexExcelTitle.addTitleRow(0, Arrays.asList("统计日期：20170215～20170313    报表生成时间：2017-03-22"), null);
		complexExcelTitle.addTitleRow(1,
				Arrays.asList("员工ID", "公司", "部门", "姓名", "状态", "公司Email", "身份证号", "全勤天数", "出勤天数", "入离职缺勤天数", "迟到早退≤1小时次数", "迟到早退＞1小时次数", "实际旷工天数", "总旷工天数", "请假（周期内）", "销假", "工资补差", "餐补补差", "日均出勤小时数"),
				"统计日期：20170215～20170313    报表生成时间：2017-03-22");
		complexExcelTitle.addTitleRow(2, Arrays.asList("病假", "事假", "年假", "婚假", "产假", "小产假", "调休", "产检", "丧假", "陪产假", "哺乳假", "因公外出", "出差"), "请假（周期内）");
		complexExcelTitle.addTitleRow(2, Arrays.asList("病假", "事假", "年假", "婚假", "产假", "小产假", "调休", "产检", "丧假", "陪产假", "因公外出", "出差"), "销假");
		titleWriter.setComplexExcelTitle(complexExcelTitle);

		ExcelSheet<String> sheet = new ExcelSheet<>(sheetName, tableTitle, data, titleWriter);

		ArrayList<ExcelSheet<String>> dataSheets = new ArrayList<>();
		dataSheets.add(sheet);
		ExcelUtils.createExcel(dataSheets, dstExcelFilename);
	}

}
