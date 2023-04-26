package ru.dns;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class CartTest
{
    private static WebDriver driver;
    private String urlOpenProduct = "https://www.dns-shop.ru/search/?q=%D0%B0%D0%BB%D0%B8%D1%81%D0%B0&category=17a8c3d816404e77";
    private By addProductToCart = By.xpath("//div[4]/button[2]");
    private By waitingBeforeEnteringTheCart = By.xpath("//*[contains(@class, 'buttons__link')]");
    private By openCart = By.xpath("//div[3]/div/div/a");
    private By numberOfItemsInTheCart = By.xpath("//nav[@id='header-search']/div/div[3]/div/div/a/span");
    private By deleteProductToCart = By.xpath("//*[contains(@class, 'remove-button__title')]");
    private By productBlock = By.cssSelector(".cart-items__product");
    private By messageEmptyCart = By.cssSelector(".empty-message__title-empty-cart");

    @BeforeAll
    public static void setUp()
    {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize ();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    }

    @Test
    public void addItemToCart()
    {
        driver.get(urlOpenProduct);
        driver.findElement(addProductToCart).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // Ожидание когда появится 1 у корзины
        wait.until(visibilityOfElementLocated(waitingBeforeEnteringTheCart));

        driver.findElement(openCart).click();
        driver.navigate().refresh();

        // Проверяю, что у корзины появилась "1"
        WebElement element = driver.findElement(numberOfItemsInTheCart);
        String textElement = element.getText();
        String message = String.format("В коризне неверное количество товаров. Ожидалось: %s, Получили: %s", "1", textElement);
        Assertions.assertEquals("1", textElement, message);

        driver.findElement(deleteProductToCart).click();
    }

    @Test
    public void deleteItemFromCart()
    {
        driver.get(urlOpenProduct);
        driver.findElement(addProductToCart).click();
        driver.findElement(openCart).click();
        driver.navigate().refresh();

       //  Проверяю, что есть карточка товара и товар удаляется.
        if (driver.findElement(productBlock).isDisplayed()) {
            driver.findElement(deleteProductToCart).click();
            System.out.println("Товар удален.");
        } else {
            Assertions.fail("Карточка товара не удалена.");
        }
    }

    @Test
    public void emptyCartText()
    {
        driver.get(urlOpenProduct);
        driver.findElement(addProductToCart).click();
        driver.findElement(openCart).click();
        driver.navigate().refresh();
        driver.findElement(deleteProductToCart).click();

        WebElement element = driver.findElement(messageEmptyCart);
        // Проверяю, что в корзине отображается текст "Корзина пуста"
        String textElement = element.getText();
        Assertions.assertEquals("Корзина пуста", textElement);
    }

    @AfterAll
    public static void close()
    {
        driver.quit();
    }
}
