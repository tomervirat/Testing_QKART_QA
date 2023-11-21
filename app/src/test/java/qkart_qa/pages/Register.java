package qkart_qa.pages;

import java.sql.Timestamp;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Register {
    WebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/register";
    public String lastGeneratedUsername = "";
    WebDriverWait wait;

    /*
     * Constructor.
     */
    public Register(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /*
     * Navigate to register page.
     */
    public void navigateToRegisterPage() {
        if (!driver.getCurrentUrl().equals(this.url)) {
            driver.get(this.url);
        }
        // Waiting for the form to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'css-ikzlcq')]")));
    }

    /*
     * Clear the text content of a text box using keyboard shortcuts.
     *
     * @param textBox The WebElement representing the text box to be cleared.
     */
    public void clearTextbox(WebElement textBox) {
        // Use Actions class to perform keyboard shortcuts for text box clearing
        new Actions(this.driver)
                .click(textBox) // Click on the text box
                .keyDown(Keys.COMMAND) // Hold down the Command key (for Mac)
                .sendKeys("a") // Type 'a' to select all text
                .keyUp(Keys.COMMAND) // Release the Command key
                .sendKeys(Keys.BACK_SPACE) // Press Backspace to delete the selected text
                .perform(); // Perform the sequence of actions

        // Wait for the text box to be cleared
        wait.until(ExpectedConditions.textToBePresentInElement(textBox, ""));
    }

    /*
     * Register a new user
     *
     * @param Username The username to be registered for new user.
     * 
     * @param Password The password to be set for the new user.
     * 
     * @param makeUsernameDynamic The boolean value to decide if the username is
     * dynamic or not
     * 
     * @return True if the registration for the new user is successful, false
     * otherwise.
     */
    public Boolean registerUser(String Username, String Password, Boolean makeUsernameDynamic)
            throws InterruptedException {
        try {

            // Locate the Username Text Box
            WebElement username_txt_box = this.driver.findElement(By.id("username"));

            // Get time stamp for generating a unique username
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            String test_data_username; // Variable for new username

            // Check if user is dynamic
            if (makeUsernameDynamic) {
                // Concatenate the timestamp to string to form unique timestamp
                test_data_username = Username + "_" + String.valueOf(timestamp.getTime());
            } else
                test_data_username = Username;

            // Clear and send the generated username in the username field
            clearTextbox(username_txt_box);
            username_txt_box.sendKeys(test_data_username);

            // Locate the password Text Box
            WebElement password_txt_box = this.driver.findElement(By.id("password"));
            String test_data_password = Password; // Variable for password

            // Clear and send the password in the password field
            clearTextbox(password_txt_box);
            password_txt_box.sendKeys(test_data_password);

            // Locate the confirm Password Text Box
            WebElement confirm_password_txt_box = this.driver.findElement(By.id("confirmPassword"));

            // Clear and send the password in the confirm password field
            clearTextbox(confirm_password_txt_box);
            confirm_password_txt_box.sendKeys(test_data_password);

            // Locate the register now button
            WebElement register_now_button = this.driver.findElement(By.className("button"));

            // Click on the register now button
            register_now_button.click();

            // Set the value to lastGeneratedUsername
            this.lastGeneratedUsername = test_data_username;

            // Wait until the URL contains "/login" indicating a successful registration
            wait.until(ExpectedConditions.urlContains("/login"));

            return driver.getCurrentUrl().endsWith("/login");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Registration of a new user is failed : " + e.getMessage());
            return false;
        }
    }
}
