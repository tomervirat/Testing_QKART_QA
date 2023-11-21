package qkart_qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.FluentWait;
import com.google.common.base.Function;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Home {

    WebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/";
    WebDriverWait wait;

    /*
     * Constructor.
     */
    public Home(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /*
     * Navigate to home page.
     */
    public void navigateToHomePage() {
        if (!driver.getCurrentUrl().equals(this.url)) {
            driver.get(url);
        }
        // Wait until the body gets loaded
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
    }

    /*
     * Search for a product.
     *
     * @param product The product name to be searched.
     * 
     * @return True if the search for the product is successful, false otherwise.
     */
    public Boolean searchForProduct(String product) {
        try {
            // Locate the searchbox
            WebElement searchBox = driver.findElement(By.xpath("//input[@placeholder='Search for items/categories']"));
            searchBox.clear(); // Clear the searchbox
            searchBox.sendKeys(product); // send the product name to the searchbox

            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(30))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(Exception.class);

            // Wait for the product to be displayed
            wait.until((Function<WebDriver, Boolean>) WebDriver -> {
                WebElement element = driver.findElement(By.xpath("//div[contains(@class, 'css-sycj1h')]//p"));
                String elementText = element.getText().toLowerCase().trim();

                boolean condition1 = elementText.contains(product.toLowerCase().trim());
                boolean condition2 = isNoResultFound();

                return condition1 || condition2;
            });
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    /*
     * Verify that No product found message should be displayed
     * 
     * @return True if No products found message is displayed, false otherwise.
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // Locate the message element and update the status
            status = driver.findElement(By.xpath("//div[contains(@class, 'css-1msksyp')]//h4")).getText()
                    .equals(" No products found ") ? true : false;
            return status;
        } catch (Exception e) {
            // TODO: handle exception
            return status;
        }
    }

    /*
     * Retrieve the list of search results displayed on the page.
     *
     * @return List of WebElements representing the search results.
     * If an exception occurs, an empty list is returned.
     */
    public List<WebElement> getSearchResults() {
        try {
            // Find and return the list of WebElements with the specified class name
            return driver.findElements(By.className("css-s18byi"));
        } catch (Exception e) {
            // Return an empty list;
            return Collections.emptyList();
        }
    }

    /*
     * Add the given product to the cart
     *
     * @param productName The product name to be added to the cart.
     * 
     * @return True if the product is addedd to the cart successfully, false
     * otherwise.
     */
    public Boolean addProductToCart(String productName) {
        try {
            // Get the list of searched products
            List<WebElement> searchedElements = getSearchResults();
            // Check if a product is present in the search results with that product name of
            // the list is empty
            if (searchedElements.size() != 0) {

                // Iterate through the list
                for (WebElement ele : searchedElements) {
                    // Get the name of each product
                    String searchedProductName = ele.findElement(By.xpath(".//descendant::p")).getText();
                    // Verify the product name and click on Add to Cart button
                    if (searchedProductName.equals(productName)) {
                        ele.findElement(By.xpath(".//descendant::button[contains(@class,'css-54wre3')]")).click();
                    }

                    // Wait for the product to be added to the cart
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath(String.format(
                                    "//div[contains(@class,'css-zgtx0t')]/descendant::div[contains(text(), '%s')]",
                                    productName))));

                    // Product added to the cart successfully
                    return true;
                }
            }
            System.out.println("Unable to find the given product");
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Click on checkout button.
     * 
     * @return True if the click on checkout button is successful, false otherwise.
     */
    public Boolean clickCheckout() {
        try {
            // Locate the checkout button
            WebElement checkoutButton = driver.findElement(By.xpath("//BUTTon[contains(text(), 'Checkout')]"));
            // Wait until the checkout button is clickable
            wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
            checkoutButton.click(); // Click on checkout button

            // Wait until the URL contains "/checkout" indicating a successful navigation to
            // checkout page
            wait.until(ExpectedConditions.urlContains("/checkout"));
            // Navigation to checkout page is successful
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception while clicking on checkout button: " + e.getMessage());
            return false;
        }
    }

    /*
     * Change the quantity of a product in the shopping cart.
     *
     * @param productName The name of the product for which the quantity needs to be
     * changed.
     * 
     * @param quantity The new quantity to be set for the specified product.
     * 
     * @return Boolean Returns true if the operation is successful; otherwise,
     * false.
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // Locate the element having the given product name
            WebElement item = driver.findElement(By.xpath(String.format("//div[text()='%s']", productName)));
            // Locate the element denoting the quantity of the given element
            WebElement currQtyofProd = item
                    .findElement(By.xpath(".//following-sibling::div/descendant::div[contains(@class, 'css-olyig7')]"));
            // Get the value of current quantity
            int currQtyofProdValue = Integer.parseInt(currQtyofProd.getText());

            // Loop to increase the quantity of the given product
            while (currQtyofProdValue < quantity) {

                // Set the new quantity by increasing the old quantity by 1
                String newQty = Integer.toString(currQtyofProdValue + 1);

                // Locate and click on increment quantity button
                currQtyofProd.findElement(By.xpath(".//following-sibling::*")).click();

                // String to locate the quantity of the given product
                String qtyString = String.format(
                        "//div[text()='%s']/following-sibling::div/descendant::div[contains(@class, 'css-olyig7')]",
                        productName);
                // Wait until the new value of quantity is displayed
                wait.until(ExpectedConditions.textToBe(By.xpath(qtyString), newQty));

                // Update the current count of the quantity
                currQtyofProdValue = Integer.parseInt(currQtyofProd.getText());
            }

            // Loop to decrease the quantity of the given product
            while (currQtyofProdValue > quantity) {

                // Set the new quantity by decreasing the old quantity by 1
                String newQty = Integer.toString(currQtyofProdValue - 1);

                // Locate and click on decrement quantity button
                currQtyofProd.findElement(By.xpath(".//preceding-sibling::*")).click();

                // String to locate the quantity of the given product
                String qtyString = String.format(
                        "//div[text()='%s']/following-sibling::div/descendant::div[contains(@class, 'css-olyig7')]",
                        productName);

                // Check if the new quantity of element is '0'
                if (newQty.equals("0")) {
                    // Wait for the product to be removed from the cart
                    wait.until(ExpectedConditions.invisibilityOf(item));
                    // Product removed from the cart successfully
                    return true;
                }

                // Wait until the new value of quantity is displayed
                wait.until(ExpectedConditions.textToBe(By.xpath(qtyString), newQty));

                // Update the current count of the quantity
                currQtyofProdValue = Integer.parseInt(currQtyofProd.getText());
            }

            // Quantity of the given product is updated successfully
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error while updating the quantity of products in cart : " + e.getMessage());
            return false;
        }
    }

    /*
     * Perform logout operation.
     * 
     * @return True if the logout is successful, false otherwise.
     */
    public Boolean performLogout() {
        try {
            // Locate the logout button and click on it
            WebElement logoutBtn = driver.findElement(By.xpath("//button[text() = 'Logout']"));
            wait.until(ExpectedConditions.elementToBeClickable(logoutBtn));
            logoutBtn.click();

            // Wait util logout is successful and Registered button is displayed again.
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text() = 'Register']")));
            // Logout is successful
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error while clicking on logout button");
            return false;
        }
    }

    /*
     * Verify that the cart contains expected items in the cart.
     * 
     * @param expectedCartContents List containing the name of expected items.
     * 
     * @return True if the expected items are present in the cart, false otherwise.
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            // Locate and wait until all the elements present in the cart are displayed
            List<WebElement> cartItems = driver.findElements(By.xpath("//div[contains(@class, 'css-zgtx0t')]"));
            wait.until(ExpectedConditions.visibilityOfAllElements(cartItems));

            // Create a new List to store the name of the products
            List<String> cartItemsName = new ArrayList<>();
            // Iterate through the procuts' List
            for (WebElement ele : cartItems) {
                // Get the products' Name and add them to the List
                cartItemsName.add(
                        ele.findElement(By.xpath(".//descendant::div[contains(@class, 'css-1gjj37g')]/div")).getText());
            }

            // Verify the actual and expected list of products
            if (expectedCartContents.equals(cartItemsName))
                return true;
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error while validating cart contents : " + e.getMessage());
            return false;
        }
    }

}
