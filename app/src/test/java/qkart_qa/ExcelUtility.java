package qkart_qa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

    public static void writeTestResult(String testcaseName, String parameters, String status) {
        Workbook workbook = null;
        try {
            String currentDir = System.getProperty("user.dir");
            String testDataExcelPath = currentDir + "/src/test/resources/TestReport.xlsx";
            File file = new File(testDataExcelPath);

            if (!file.exists()) {
                workbook = new XSSFWorkbook();
            } else {
                FileInputStream fileIn = new FileInputStream(file);
                workbook = WorkbookFactory.create(fileIn);
                fileIn.close();
            }

            String SHEET_NAME = "TestCasesReport";
            Sheet sheet = workbook.getSheet(SHEET_NAME);

            if (sheet == null) {
                sheet = workbook.createSheet(SHEET_NAME);
                Row headingRow = sheet.createRow(0);

                // Write column headings
                Cell headingCell1 = headingRow.createCell(0);
                headingCell1.setCellValue("Test Case Name");

                Cell headingCell2 = headingRow.createCell(1);
                headingCell2.setCellValue("Parameters");

                Cell headingCell3 = headingRow.createCell(2);
                headingCell3.setCellValue("Status");
            }

            // Find the next available row
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            // Write the testcase name to column 0
            Cell testcaseNameCell = newRow.createCell(0);
            testcaseNameCell.setCellValue(testcaseName);
            Cell parametersCell = newRow.createCell(1);
            parametersCell.setCellValue(parameters);
            // Write the status to column 1
            Cell statusCell = newRow.createCell(2);
            statusCell.setCellValue(status);

            // Save and close the workbook
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
