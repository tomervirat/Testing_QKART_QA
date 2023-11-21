package qkart_qa;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

    /*
     * Takes a screenshot of the current state of the WebDriver.
     *
     * @param driver The WebDriver instance for which the screenshot is to be taken.
     * 
     * @param screenshotType The type or format of the screenshot (e.g., "PNG",
     * "JPEG").
     * 
     * @param description A brief description or name for the screenshot.
     */
    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            // Check if the directory is present to store the screenshots
            File theDir = new File("/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now()); // Current Timestamp
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);
            // Take screenshot
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            // Create a new file
            File DestFile = new File("screenshots/" + fileName);
            // Copy the screenshot to this file
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStart(ITestContext context) {
        System.out.println("onStart method started");
    }

    public void onFinish(ITestContext context) {
        System.out.println("onFinish method finished");
    }

    public void onTestStart(ITestResult result) {
        System.out.println("New Test Started: " + result.getName());
        takeScreenshot(Qkart_QA.driver, "TestStart", result.getName());
    }

    /*
     * Prepare the raw data to be entered in the excel sheel.
     * 
     * @paramn result The ITestResult to get the data about the test methods.
     */
    public void dataToWrite(ITestResult result) {
        Object parameters[] = result.getParameters(); // get the parameters
        String param = "["; // variable to store all the parameters in String form
        for (int i = 0; i < parameters.length; i++) {
            param = param + parameters[i].toString();
            if (i != parameters.length - 1)
                param = param + ", ";
        }
        param = param + "]";
        String status = ""; // Value to store the result of TestCase

        // Switch case to get the result using status value
        switch (result.getStatus()) {
            case -1:
                status = "CREATED";
                break;
            case 1:
                status = "SUCCESS";
                break;
            case 2:
                status = "FAILURE";
                break;
            case 3:
                status = "SKIP";
                break;
            case 4:
                status = "SUCCESS_PERCENTAGE_FAILURE";
                break;
            case 5:
                status = "STARTED";
                break;

            default:
                break;
        }

        // Call the writeTestResult method to write the results of test cases into excel
        // sheet
        ExcelUtility.writeTestResult(result.getName(), param, status);
    }

    public void onTestSuccess(ITestResult result) {
        System.out.println("onTestSuccess Method: " + result.getName());
        dataToWrite(result);
        takeScreenshot(Qkart_QA.driver, "TestSuccess", result.getName());
    }

    public void onTestFailure(ITestResult result) {
        System.out.println("onTestFailure Method: " + result.getName());
        dataToWrite(result);
        takeScreenshot(Qkart_QA.driver, "TestFailed", result.getName());
    }

    public void onTestSkipped(ITestResult result) {
        System.out.println("onTestSkipped Method: " + result.getName());
        dataToWrite(result);
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("onTestFailedButWithinSuccessPercentage: " + result.getName());
        dataToWrite(result);
    }
}
