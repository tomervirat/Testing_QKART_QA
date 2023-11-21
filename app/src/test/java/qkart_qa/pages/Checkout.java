package qkart_qa.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    WebDriver driver;
    WebDriverWait wait;

    /*
     * Constructor.
     */
    public Checkout(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(4));
    }

    /*
     * Add a new address to the address list.
     *
     * @param addressString The complete address to be added.
     * 
     * @return True if the address is successfully added, false otherwise.
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // check if the address length is valid
            if (addresString.length() < 20) {
                System.out.println("The address should be greater or equal to 20 characters");
                return false;
            }

            // Locate and click the "Add new address" button
            WebElement addNewAddBtn = driver.findElement(By.xpath("//button[contains(text(), 'Add new address')]"));
            wait.until(ExpectedConditions.elementToBeClickable(addNewAddBtn));
            addNewAddBtn.click();

            // Locate the address text area and enter the address
            WebElement addTextArea = driver
                    .findElement(By.xpath("//textarea[@placeholder='Enter your complete address']"));
            wait.until(ExpectedConditions.elementToBeClickable(addTextArea));
            addTextArea.sendKeys(addresString);
            wait.until(ExpectedConditions.attributeToBe(addTextArea, "value", addresString));

            // Click the "Add" button
            driver.findElement(By.xpath("//button[contains(@class, 'css-177pwqq') and contains(text(),'Add')]"))
                    .click();

            // Wait for the address to be visible in the address list
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                    "//p[contains(@class, 'yg30e6') and text()='%s']", addresString))));

            // Address added successfully
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error while adding the new address : " + e.getMessage());
            return false;
        }
    }

    /*
     * Select an existing address from the address list.
     * 
     * @param addressString The address to be selected.
     * 
     * @return True if the address is successfully selected, false otherwise.
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            // Locate the given address and select by clicking on it
            WebElement addressToSelectBtn = driver.findElement(By.xpath(String.format(
                    "//p[contains(@class, 'yg30e6') and text()='%s']/preceding-sibling::*", addressToSelect)));
            wait.until(ExpectedConditions.elementToBeClickable(addressToSelectBtn));
            addressToSelectBtn.click();

            // Address selected successfully
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception while selecting an address : " + e.getMessage());
            return false;
        }
    }

    /*
     * Place an order.
     *
     * @return True if the order is successfully placed, false otherwise.
     */
    public Boolean placeOrder() {
        try {
            // Locate the place order button and click on it
            WebElement placeOrderBtn = driver.findElement(By.xpath("//button[text()='PLACE ORDER']"));
            placeOrderBtn.click();

            // Wait until the URL contains "/thanks" indicating a successful order placement
            wait.until(ExpectedConditions.urlContains("/thanks"));

            // Order placed successfully
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Unable to place order : " + e.getMessage());
            return false;
        }
    }

    /*
     * Verify that the insufficient balance message is displayed.
     * 
     * @return True if the insufficient balance message is displayed, false
     * otherwise.
     */
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            // Message to be verified
            String expectedErrorMsg = "You do not have enough balance in your wallet for this purchase";

            // Locate the message
            WebElement inBalAlert = driver.findElement(By.id("notistack-snackbar"));
            wait.until(ExpectedConditions.visibilityOf(inBalAlert));

            // Verify the message
            String inBalAlertText = inBalAlert.getText();
            if (inBalAlertText.equals(expectedErrorMsg))
                return true;
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error while validating the insufficient balance message : " + e.getMessage());
            return false;
        }
    }

}
