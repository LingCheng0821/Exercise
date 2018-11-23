package com.fang.jdknewfeatures.email;

import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

public class ComplexExcelTitle {

	private int titleId = 0;
	private LinkedHashMap<Integer, String> titleIdNameMap = new LinkedHashMap<>();

	private HashMap<Integer, Integer> titleIdRowIndexMap = new HashMap<>();
	private HashMap<Integer, Integer> titleIdLeafRowIndexMap = new HashMap<>();

	private HashMap<Integer, Integer> titleIdParentTitleIdMap = new HashMap<>();

	private HashMap<Integer, Integer> titleIdFirstColMap = new HashMap<>();
	private HashMap<Integer, Integer> titleIdWidthMap = new HashMap<>();

	private HashMap<Integer, List<Integer>> leafTitleIdListMap = new HashMap<>();

	private LinkedHashMap<Integer, LinkedHashMap<List<Integer>, Integer>> excelTitle = new LinkedHashMap<>();
	private LinkedHashMap<Integer, LinkedHashMap<Integer, CellRangeAddress>> titleWithCellRangeAddress = new LinkedHashMap<>();

	public void addTitleRow(Integer rowIndex, List<String> currentTitleList, String parentTitle) {
		addTitleRow(rowIndex, currentTitleList, parentTitle, 1);
	}

	public void addTitleRow(Integer rowIndex, List<String> currentTitleList, String parentTitle, int parentTitleRepeatOrder) {
		if (parentTitle == null) {
			parentTitle = "null";
		}

		int parentTitleId = getParentTitleId(parentTitle, parentTitleRepeatOrder);
		titleIdLeafRowIndexMap.put(parentTitleId, rowIndex);

		List<Integer> currentTitleIdList = new ArrayList<>();
		for (String titleName : currentTitleList) {
			titleId++;
			titleIdNameMap.put(titleId, titleName);
			titleIdRowIndexMap.put(titleId, rowIndex);
			titleIdParentTitleIdMap.put(titleId, parentTitleId);
			currentTitleIdList.add(titleId);
		}

		leafTitleIdListMap.put(parentTitleId, currentTitleIdList);

		if (excelTitle.containsKey(rowIndex)) {
			excelTitle.get(rowIndex).put(currentTitleIdList, parentTitleId);
		} else {
			LinkedHashMap<List<Integer>, Integer> titleRowValue = new LinkedHashMap<>();
			titleRowValue.put(currentTitleIdList, parentTitleId);
			excelTitle.put(rowIndex, titleRowValue);
		}
	}

	public LinkedHashMap<Integer, String> getTitleIdNameMap() {
		return titleIdNameMap;
	}

	public LinkedHashMap<Integer, LinkedHashMap<Integer, CellRangeAddress>> getTitleWithCellRangeAddress() {
		this.computeCellRangeAddress();
		return titleWithCellRangeAddress;
	}

	public void computeCellRangeAddress() {
		Set<Integer> rowIndexSet = excelTitle.keySet();
		LinkedHashMap<Integer, CellRangeAddress> titleRowCellRangeAddressMap = null;
		for (Integer rowIndex : rowIndexSet) {
			titleRowCellRangeAddressMap = new LinkedHashMap<>();

			LinkedHashMap<List<Integer>, Integer> map = excelTitle.get(rowIndex);
			Set<List<Integer>> keySet = map.keySet();
			for (List<Integer> titleIds : keySet) {
				for (Integer titleId : titleIds) {
					int firstRow = rowIndex;
					int lastRow = getLeafRowIndex(titleId) - 1;
					int firstCol = getFirstCol(titleId);
					int lastCol = firstCol + getWidth(titleId) - 1;
					titleRowCellRangeAddressMap.put(titleId, new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
				}
			}

			titleWithCellRangeAddress.put(rowIndex, titleRowCellRangeAddressMap);
		}
	}

	private int getParentTitleId(String parentTitle, int parentTitleRepeatOrder) {
		int index = 0;
		for (Integer titleId : titleIdNameMap.keySet()) {
			if (parentTitle.equals(titleIdNameMap.get(titleId))) {
				index++;
				if (index == parentTitleRepeatOrder) {
					return titleId;
				}
			}
		}
		return 0;
	}

	private int getFirstCol(Integer titleId) {
		if (titleIdFirstColMap.containsKey(titleId)) {
			return titleIdFirstColMap.get(titleId);
		}

		int firstCol = 0;
		if (titleId != 0) {
			Integer parentId = titleIdParentTitleIdMap.get(titleId);
			firstCol += getFirstCol(parentId);

			List<Integer> siblingTitleIdList = getLeafTitleIdList(parentId);
			for (int i = 0; i < siblingTitleIdList.indexOf(titleId); i++) {
				firstCol += getWidth(siblingTitleIdList.get(i));
			}
		}
		titleIdFirstColMap.put(titleId, firstCol);
		return firstCol;
	}

	private List<Integer> getLeafTitleIdList(Integer titleId) {
		return leafTitleIdListMap.get(titleId);
	}

	private int getWidth(Integer titleId) {
		if (titleIdWidthMap.containsKey(titleId)) {
			return titleIdWidthMap.get(titleId);
		}
		int width = 1;
		List<Integer> leafTitleIdList = getLeafTitleIdList(titleId);
		if (leafTitleIdList != null) {
			width = 0;
			for (Integer leafTitleId : leafTitleIdList) {
				width += getWidth(leafTitleId);
			}
		}
		titleIdWidthMap.put(titleId, width);
		return width;
	}

	private int getLeafRowIndex(Integer titleId) {
		return titleIdLeafRowIndexMap.containsKey(titleId) ? titleIdLeafRowIndexMap.get(titleId) : excelTitle.size();
	}

	public void printCellRangeAddress() {
		for (Integer rowIndex : titleWithCellRangeAddress.keySet()) {
			System.out.println("========================");
			LinkedHashMap<Integer, CellRangeAddress> linkedHashMap = titleWithCellRangeAddress.get(rowIndex);
			for (Integer titleId : linkedHashMap.keySet()) {
				String titleName = titleIdNameMap.get(titleId);
				System.out.println(titleName + "\t\t" + linkedHashMap.get(titleId));
			}
		}
	}
}