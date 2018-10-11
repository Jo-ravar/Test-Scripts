package com.miniorange.saml.helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.miniorange.saml.models.Testcase;

public class WriteExcelFile {
	private static String[] columns = { "S.NO", "TestCase", "Result", "Reason" };

	public void writeXLSFile(List<Testcase> testcases, String filePath, String fileName) throws IOException {
		String excelFileName = filePath + fileName + ".xls";
		System.out.println(excelFileName);

		String sheetName = "Sheet1";// name of sheet

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);

		Font headerFont = wb.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);

		// Create a CellStyle with the font
		CellStyle headerCellStyle = wb.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// Create a Row
		HSSFRow headerRow = sheet.createRow(0);

		// Create cells
		for (int i = 0; i < columns.length; i++) {
			HSSFCell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		Font passFont = wb.createFont();
		passFont.setBold(true);
		passFont.setFontHeightInPoints((short) 10);
		passFont.setColor(IndexedColors.GREEN.getIndex());
		CellStyle passCellStyle = wb.createCellStyle();
		passCellStyle.setFont(passFont);

		Font failFont = wb.createFont();
		failFont.setBold(true);
		failFont.setFontHeightInPoints((short) 10);
		failFont.setColor(IndexedColors.RED.getIndex());
		CellStyle failCellStyle = wb.createCellStyle();
		failCellStyle.setFont(failFont);

		// iterating r number of rows
		for (int r = 0; r < testcases.size(); r++) {
			HSSFRow row = sheet.createRow(r + 1);

			HSSFCell snoCell = row.createCell(0);
			HSSFCell nameCell = row.createCell(1);
			HSSFCell resultCell = row.createCell(2);
			HSSFCell reasonCell = row.createCell(3);

			snoCell.setCellValue(r + 1);
			nameCell.setCellValue(testcases.get(r).testName);

			if (testcases.get(r).testResut.equals("FAIL")) {
				resultCell.setCellValue(testcases.get(r).testResut);
				resultCell.setCellStyle(failCellStyle);
			} else {
				resultCell.setCellValue(testcases.get(r).testResut);
				resultCell.setCellStyle(passCellStyle);
			}

			reasonCell.setCellValue(testcases.get(r).testFailureReason);
		}

		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);

		// write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

}
