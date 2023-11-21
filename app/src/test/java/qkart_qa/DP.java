package qkart_qa;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

public class DP {

    @DataProvider(name = "data-provider")
    public Object[][] dataProvider(Method m) throws IOException {
        String sheetName = m.getName();
        String currentDir = System.getProperty("user.dir");
        String filePath = currentDir + "/src/test/resources/Dataset.xlsx";

        FileInputStream excelFile = new FileInputStream(filePath);
        try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(0);
            int rowSize = sheet.getLastRowNum();
            int colSize = row.getLastCellNum();

            // Excel sheet is corrupted so manually setting the rowSize for testcase04 and
            // testcase 11
            if (sheetName.equals("TestCase04") || sheetName.equals("TestCase11"))
                rowSize = 3;

            // 2D object to store the data
            Object[][] data = new Object[rowSize][colSize - 1];
            for (int i = 1; i <= rowSize; i++) {
                row = sheet.getRow(i);
                for (int j = 1; j < colSize; j++) {
                    Cell cell = row.getCell(j);
                    if (cell.getCellType() == CellType.NUMERIC) {
                        data[i - 1][j - 1] = (int) cell.getNumericCellValue();
                    } else if (cell.getCellType() == CellType.BOOLEAN) {
                        data[i - 1][j - 1] = cell.getBooleanCellValue();
                    } else {
                        data[i - 1][j - 1] = cell.getStringCellValue();

                    }
                }
            }
            return data;
        } catch (Exception e) {
            System.out.println("Error while fetching the data from excel sheet: " + e.getMessage());
            return new Object[0][0];
        }
    }
}
