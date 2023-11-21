package qkart_qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.NoSuchElementException;

public class Login {
    WebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/login";
    WebDriverWait wait;

    /*
     * Constructor.
     */
    public Login(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /*
     * Navigate to login page.
     */
    public void navigateToLoginPage() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
        // Waiting for the form to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'css-ikzlcq')]")));
    }

    /*
     * Login the given user
     *
     * @param Username The username to be logged in for the given user.
     * 
     * @param Password The password to be entered for the given user.
     * 
     * @return True if the login for the given user is successful, false otherwise.
     */
    public Boolean PerformLogin(String Username, String Password) throws InterruptedException {
        try {
            // Locate the Username Text Box
            WebElement username_txt_box = this.driver.findElement(By.id("username"));

            // Send the username
            username_txt_box.sendKeys(Username);

            // Wait for user name to be entered
            wait.until(ExpectedConditions.attributeToBe(username_txt_box, "value", Username));

            // Locate the password Text Box
            WebElement password_txt_box = this.driver.findElement(By.id("password"));

            // Send the password
            password_txt_box.sendKeys(Password);

            // Locate the Login Button
            WebElement login_button = driver.findElement(By.className("button"));

            // Click the login Button
            login_button.click();

            // Wait for the login button to be disappear
            FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(30))
                    .pollingEvery(Duration.ofMillis(600)).ignoring(NoSuchElementException.class);
            wait.until(ExpectedConditions.invisibilityOf(login_button));

            // Synchronize and pause the execution of the current thread for a specified
            // duration.
            synchronized (driver) {
                driver.wait(2000);
            }

            // Call VerifyUserLoggedIn method to verify if the login is successful and
            // username is displayed
            return VerifyUserLoggedIn(Username);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Login of a new user is failed : " + e.getMessage());
            return false;
        }
    }

    /*
     * Verify the logged in user
     *
     * @param Username The username to be verified for the given user.
     * 
     * @return True if the user is same as the given user, false otherwise.
     */
    public Boolean VerifyUserLoggedIn(String Username) {
        try {
            // Locate the username label (present on the top right of the page) and verify
            // the username
            WebElement username_label = driver.findElement(By.className("username-text"));
            return username_label.getText().equals(Username);
        } catch (Exception e) {
            return false;
        }

    }

}