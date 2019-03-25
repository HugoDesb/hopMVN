package webscraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

public class FindRecommandation {
    public static void main(String[] args) {
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver");

        FirefoxOptions fo = new FirefoxOptions();
        fo.setLogLevel(FirefoxDriverLogLevel.fromString("trace"));
        //fo.setHeadless(true);

        WebDriver driver = new FirefoxDriver(fo);

        // And now use this to visit Google
        driver.get("https://www.has-sante.fr/portail/jcms/c_2857558/fr/borreliose-de-lyme-et-autres-maladies-vectorielles-a-tiques");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Find the text input element by its name
        //WebElement element = driver.findElement(By.name("q"));
        List<WebElement> elements = driver.findElements(By.cssSelector(".pdf"));
        for (WebElement el : elements) {
            for (WebElement link : el.findElements(By.tagName("a"))) {
                System.out.println(link.getText());
                if(link.getText().matches(".*\\s[Rr]ecommandation(s?).*")){
                    System.out.println("Select this link : " + link.getText());
                    WebDriverWait wdw = new WebDriverWait(driver, 10);
                    wdw.until(ExpectedConditions.elementToBeClickable(link));
                    wdw.until(ExpectedConditions.visibilityOf(link));
                    link.click();
                    //Actions actions = new Actions(driver);
                    //actions.moveToElement(link).click().build().perform();
                    wdw.until(ExpectedConditions.numberOfWindowsToBe(2));
                    Set<String> tabs = driver.getWindowHandles();
                    for (String tab :
                            tabs) {
                        System.out.println(tab);
                    }


                }
            }
        }

        // Enter something to search for
        //element.sendKeys("Cheese!");

        // Now submit the form. WebDriver will find the form for us from the element
        //element.submit();

        // Check the title of the page
        //System.out.println("Page title is: " + driver.getTitle());

        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        //(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
        //    public Boolean apply(WebDriver d) {
        //        return d.getTitle().toLowerCase().startsWith("cheese!");
        //    }
        //});

        // Should see: "cheese! - Google Search"
        //System.out.println("Page title is: " + driver.getTitle());

        //Close the browser
        //driver.quit();
    }
}
