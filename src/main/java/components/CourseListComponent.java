 package components;

 import org.openqa.selenium.By;
 import org.openqa.selenium.WebDriver;
 import org.openqa.selenium.WebElement;
 import org.openqa.selenium.support.ui.ExpectedConditions;
 import org.openqa.selenium.support.ui.WebDriverWait;

 import java.time.Duration;
 import java.util.List;
 import java.util.stream.Collectors;

 public class CourseListComponent {

     private final WebDriver driver;
     private final WebDriverWait wait;
     private final By cards = By.cssSelector("a.sc-zzdkm7-0");


    @com.google.inject.Inject
    public CourseListComponent(WebDriver driver) {
         this.driver = driver;
         this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
     }

     public void waitForReady() {
         wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cards));
     }

     public List<CourseCardComponent> getAllCards() {
         List<WebElement> els = driver.findElements(cards);
         return els.stream()
                  .map(e -> new CourseCardComponent(driver, e))
                  .collect(Collectors.toList());
     }

     public List<String> getAllTitles() {
         return getAllCards().stream()
                             .map(CourseCardComponent::getTitle)
                             .collect(Collectors.toList());
     }

     public void clickByName(String name) {
         getAllCards().stream()
             .filter(c -> c.getTitle().equalsIgnoreCase(name))
             .findFirst()
             .orElseThrow(() -> new IllegalArgumentException("Курс не найден: " + name))
             .click();
     }

     public List<CourseCardComponent> getCardsWithDates() {
         return getAllCards().stream()
             .filter(c -> c.tryGetStartDate().isPresent())
             .collect(Collectors.toList());
     }
 }