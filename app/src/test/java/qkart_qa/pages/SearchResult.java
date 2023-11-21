package qkart_qa.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchResult {
    WebElement parent; // Variable to locate the a product

    /*
     * Constructor.
     */
    public SearchResult(WebElement parent) {
        this.parent = parent;
    }

    /*
     * Retrieve the title of the parent product.
     * 
     * @return String Return the title of the parent product.
     */
    public String getTitleofResult() {
        String title = parent.findElement(By.className("css-yg30e6")).getText();
        return title;
    }

    /*
     * Verify if size chat button exists.
     * 
     * @return True if the size chart button containing 'SIZE CHART' exists, false
     * otherwise.
     */
    public Boolean verifySizeChartExists() {
        try {
            Boolean status = false;
            // Locate the size chart button with the help of parent element
            WebElement element = parent.findElement(By.tagName("button"));
            status = element.getText().equals("SIZE CHART");
            return status;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Size chart link does not exist " + e.getMessage());
            return false;
        }
    }

    /*
     * Open the size chart.
     *
     * @param driver The driver to perform the operation.
     * 
     * @return True if the size chart is opened successfully, false otherwise.
     */
    public Boolean openSizechart(WebDriver driver) {
        try {
            // Locate the size chart using the parentElement and click on it
            WebElement sizeChartBtn = parent.findElement(By.tagName("button"));
            sizeChartBtn.click();
            // Locate the 'td' tag and wait for it to get displayed
            List<WebElement> tableElements = driver.findElements(By.tagName("td"));
            new WebDriverWait(driver,
                    Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfAllElements(tableElements));

            // Successfully opened the size chart
            return true;
        } catch (Exception e) {
            System.out.println("Exception while opening Size chart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Check the existance of Size Dropdown.
     *
     * @param driver The driver to perform the operation.
     * 
     * @return True if the size drop down is displayed, false otherwise.
     */
    public Boolean verifyExistenceofSizeDropdown(WebDriver driver) {
        Boolean status = false;
        try {
            // Locate and wait for the size drop down
            WebElement element = driver.findElement(By.className("css-13sljp9"));
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(element));
            status = element.isDisplayed(); // check for isDisplayed
            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Verify the size chart is showing the correct contents.
     *
     * @param expectedTableHeaders The List of expected headers
     * 
     * @param expectedTableBody The List of expected body.
     * 
     * @return Boolean Returns true if the actual header and body contents is same
     * as expected, false otherwise.
     */
    public Boolean validateSizeChartContents(List<String> expectedTableHeaders, List<List<String>> expectedTableBody,
            WebDriver driver) {
        Boolean status = true;
        try {
            // Locate the header elements
            List<WebElement> tableHeader = driver.findElements(By.xpath("//tr[contains(@class, 'css-mnddxn')]/th"));
            String tempHeaderValue; // Variable to store a value of header

            // Iterate through the header elements
            for (int i = 0; i < expectedTableHeaders.size(); i++) {
                tempHeaderValue = tableHeader.get(i).getText();
                // Verify the values
                if (!expectedTableHeaders.get(i).equals(tempHeaderValue)) {
                    return false;
                }
            }

            // Locate the table body
            List<WebElement> tableBodyRows = driver.findElements(By.xpath("//tr[contains(@class, 'css-171yt5d')]"));
            // List for table body row
            List<WebElement> tempBodyRow;
            // Iterate through the List to check the row
            for (int i = 0; i < expectedTableBody.size(); i++) {
                tempBodyRow = tableBodyRows.get(i).findElements(By.tagName("td"));
                // Iterate through the row to get the cell values
                for (int j = 0; j < expectedTableBody.get(i).size(); j++) {
                    tempHeaderValue = tempBodyRow.get(j).getText();
                    // Verify the values
                    if (!expectedTableBody.get(i).get(j).equals(tempHeaderValue)) {
                        return false;
                    }
                }
            }
            return status;

        } catch (Exception e) {
            System.out.println("Error while validating chart contents" + e.getMessage());
            return false;
        }
    }

    /*
     * Close the opened size chart.
     *
     * @param driver The driver to perform the operation.
     * 
     * @return True if the size chart is closed successfully, false otherwise.
     */
    public Boolean closeSizeChart(WebDriver driver) {
        try {
            // Synchronize and pause the execution of the current thread for a specified
            // duration.
            synchronized (driver) {
                driver.wait(2000);
            }

            Actions action = new Actions(driver); // Create an object of action class

            // Clickon "ESC" key close the size chart modal
            action.sendKeys(Keys.ESCAPE);
            action.perform();

            // Wait for the invisibility of the size chart modal
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("MuiDialog-paperScrollPaper")));

            // Successfully closed the size chart modal
            return true;
        } catch (Exception e) {
            System.out.println("Exception while closing the size chart: " + e.getMessage());
            return false;
        }
    }

}
