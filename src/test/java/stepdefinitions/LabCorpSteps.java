package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverManager;

import java.time.Duration;

public class LabCorpSteps {

    private WebDriver driver;
    private WebDriverWait wait;
    private String jobTitle;
    private String jobId;
    private String jobPageUrl;

    @Before("@ui")
    public void startBrowser() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @After("@ui")
    public void closeBrowser() {
        DriverManager.quitDriver();
    }

    @Given("I am on the LabCorp careers site")
    public void openCareersSite() {
        driver.get("https://www.labcorp.com");
        acceptCookies(By.id("onetrust-accept-btn-handler"));

        WebElement careers = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Careers")));
        driver.get(careers.getAttribute("href"));
        acceptCookies(By.id("onetrust-accept-btn-handler"));
    }

    @When("I search and open the first job for {string}")
    public void searchAndOpenJob(String keyword) {
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("typehead")));
        searchBox.clear();
        searchBox.sendKeys(keyword);

        WebElement job = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("li.phsJobsSuggested_0 a[data-ph-at-id='suggested-data-link']")));
        driver.get(job.getAttribute("href"));
        acceptCookies(By.id("onetrust-accept-btn-handler"));
    }

    @Then("I should see job {string} in {string} with id {string}")
    public void verifyJobDetails(String expectedTitle, String locationKeyword, String expectedId) {
        jobTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.job-title")))
            .getText().trim();
        String location = driver.findElement(By.xpath("//*[contains(@class,'location')]")).getText();
        String rawId = driver.findElement(By.cssSelector("span.jobId")).getText();
        jobId = rawId.replaceAll("\\D", "");

        Assertions.assertEquals(expectedTitle, jobTitle);
        Assertions.assertTrue(location.contains(locationKeyword));
        Assertions.assertEquals(expectedId, jobId);
    }

    @And("the job page should mention {string} and {string} and {string}")
    public void verifyJobPageText(String text1, String text2, String text3) {
        String page = driver.findElement(By.tagName("body")).getText();
        Assertions.assertTrue(page.contains(text1));
        Assertions.assertTrue(page.contains(text2));
        Assertions.assertTrue(page.contains(text3));
    }

    @When("I apply and check Workday for {string} in {string}")
    public void applyAndCheckWorkday(String applyPageText, String workdayLocation) {
        jobPageUrl = driver.getCurrentUrl();

        WebElement applyLink = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("a[data-ph-at-id='apply-link']")));
        driver.get(applyLink.getAttribute("href"));
        acceptCookies(By.cssSelector("button[data-automation-id='legalNoticeAcceptButton']"));

        String applyTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3")))
            .getText().trim();
        Assertions.assertEquals(jobTitle, applyTitle);
        Assertions.assertTrue(driver.findElement(By.tagName("body")).getText().contains(applyPageText));

        driver.get(driver.getCurrentUrl().replace("/apply", ""));
        acceptCookies(By.cssSelector("button[data-automation-id='legalNoticeAcceptButton']"));

        String workdayTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("h2[data-automation-id='jobPostingHeader']"))).getText().trim();
        String workdayId = driver.findElement(
            By.cssSelector("div[data-automation-id='requisitionId'] dd")).getText().trim();
        String location = driver.findElement(
            By.cssSelector("div[data-automation-id='locations'] dd")).getText();

        Assertions.assertEquals(jobTitle, workdayTitle);
        Assertions.assertEquals(jobId, workdayId);
        Assertions.assertTrue(location.contains(workdayLocation));
    }

    @Then("I should return to the job search page")
    public void returnToJobSearch() {
        driver.get(jobPageUrl);
        acceptCookies(By.id("onetrust-accept-btn-handler"));

        WebElement jobSearch = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Job Search")));
        driver.get(jobSearch.getAttribute("href"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("search-results"));
    }

    private void acceptCookies(By locator) {
        for (WebElement button : driver.findElements(locator)) {
            if (button.isDisplayed()) {
                button.click();
                return;
            }
        }
    }
}
