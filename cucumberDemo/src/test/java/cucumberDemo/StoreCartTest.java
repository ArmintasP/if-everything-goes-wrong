package cucumberDemo;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreCartTest {
    private static String username;
    private static String password;

    private WebDriver driver;
    private WebDriverWait driverWait;

    @BeforeAll
    public static void registerUser() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setLogLevel(ChromeDriverLogLevel.OFF);

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        registerUser(driver);
        driver.quit();
    }

    @Before
    public void setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setLogLevel(ChromeDriverLogLevel.OFF);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After
    public void quitDriver() {
        driver.quit();
        driver = null;
        driverWait = null;
    }

    @Given("the user logins using valid credentials")
    public void login() {
        driver.get("https://www.demoblaze.com");

        driverWait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"login2\"]")))
                .click();

        driverWait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"loginusername\"]")))
                .sendKeys(username);
        driverWait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"loginpassword\"]")))
                .sendKeys(password);

        driverWait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[@onclick=\"logIn()\" and text()=\"Log in\"]")))
                .click();
    }

    @When("he browses {string} category")
    public void browseProducts(String categoryName) {
        driverWait
                .ignoring(StaleElementReferenceException.class)
                .until((driver) -> {
                    driver.findElement(By.xpath("//a[text() = \"" + categoryName + "\"]")).click();
                    return true;
                });
    }

    @And("selects an {string}")
    public void selectProduct(String productName) {
        driverWait
                .ignoring(StaleElementReferenceException.class)
                .until((driver) -> {
                    driver.findElement(By.xpath("//div[@class = \"card-block\"]//a[contains(text(), \"" + productName + "\")]")).click();
                    return true;
                });
    }

    @And("clicks `Add to cart`")
    public void addToCart() {
        driverWait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[text()=\"Add to cart\"]")))
                .click();
    }

    @Then("he sees {string}")
    public void inspectLastMessage(String expectedMessage) {
        driverWait.until(ExpectedConditions.alertIsPresent());
        String alertMessage = driver.switchTo().alert().getText();

        assertEquals(alertMessage, expectedMessage);
    }

    private static void registerUser(WebDriver driver) {
        WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.demoblaze.com");

        username = UUID.randomUUID().toString();
        password = UUID.randomUUID().toString();

        driver.findElement(By.xpath("//*[@id=\"signin2\"]"))
                .click();

        driver.findElement(By.xpath("//*[@id=\"sign-username\"]"))
                .sendKeys(username);
        driver.findElement(By.xpath("//*[@id=\"sign-password\"]"))
                .sendKeys(password);

        driver.findElement(By.xpath("//button[@onclick=\"register()\" and text()=\"Sign up\"]"))
                .click();

        driverWait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }
}
