

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class FripkartAutomate {

    public static void main(String[] args) throws Exception {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // 1. Open Flipkart
        driver.get("https://www.flipkart.com/");

        // 2. Close login popup
        driver.findElement(By.xpath("//span[text()='✕']")).click();

        // 3. Search "Bluetooth Speakers"
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Bluetooth Speakers");
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(3000);

        // 4. Apply Brand filter → boAt
        driver.findElement(By.xpath("//div[text()='Brand']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[text()='boAt']")).click();
        Thread.sleep(3000);

        // 5. Apply Customer Ratings → 4★ & above
        driver.findElement(By.xpath("//div[text()='4★ & above']")).click();
        Thread.sleep(3000);

        // 6. Sort by Price → Low to High
        driver.findElement(By.xpath("//div[text()='Price -- Low to High']")).click();
        Thread.sleep(3000);

        // 7. Open first product in new tab
        WebElement firstProduct = driver.findElement(By.xpath("(//a[contains(@href,'/p/')])[1]"));
        Actions a = new Actions(driver);
        a.keyDown(Keys.CONTROL).click(firstProduct).keyUp(Keys.CONTROL).build().perform();

        // Switch to new tab
        String mainWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for (String window : allWindows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
          Thread.sleep(3000);

        // 8. Check "Available offers"
        try {
            List<WebElement> offers = driver.findElements(By.xpath("//div[contains(text(),'Available offers')]/following-sibling::div//li"));
            System.out.println("Number of available offers: " + offers.size());
      } catch (Exception e) {
            System.out.println("Available offers section not found");
        }

        // 9. Check Add to Cart
        try {
            WebElement addToCart = driver.findElement(By.xpath("//button[contains(text(),'Add to cart')]"));

            if (addToCart.isDisplayed()) {

              // Scenario 1
              addToCart.click();
              Thread.sleep(3000);
              takeScreenshot(driver, "cart_result.png");
              System.out.println("Product added to cart");
          } else {
                handleUnavailable(driver);
            }

        } catch (Exception e) {
           
        	// Scenario 2
            handleUnavailable(driver);
        }
            driver.quit();
    }

    // Screenshot method
    public static void takeScreenshot(WebDriver driver, String name) throws Exception {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File(name));
    }

    // Handle unavailable
    public static void handleUnavailable(WebDriver driver) throws Exception {
        System.out.println("Product unavailable — could not be added to cart.");
        takeScreenshot(driver, "result.png");
    }
   }

