package components;

import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Компонент для работы со списком курсов и меню.
 */
public class CourseListComponent {

    private final WebDriver driver;

    @FindBy(css = "nav .menu-item-courses")
    private WebElement coursesMenu;

    @FindBy(css = "nav .menu-item-courses .submenu li")
    private List<WebElement> subMenuItems;

    @FindBy(css = ".course-card")
    private List<CourseCardComponent> allCards;

    /**
     * Конструктор с инъекцией WebDriver и инициализацией @FindBy полей.
     */
    @Inject
    public CourseListComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Ждёт, пока все карточки курсов станут видимыми.
     */
    public void waitForReady() {
        List<WebElement> roots = allCards.stream()
            .map(CourseCardComponent::getRootElement)
            .collect(Collectors.toList());
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfAllElements(roots));
    }

    /**
     * Возвращает список карточек с датой старта, если она указана.
     */
    public List<CourseCardComponent> getCardsWithDates() {
        return allCards.stream()
            .filter(c -> c.tryGetStartDate().isPresent())
            .collect(Collectors.toList());
    }

    /**
     * Возвращает все заголовки курсов.
     */
    public List<String> getAllTitles() {
        return allCards.stream()
            .map(CourseCardComponent::getTitle)
            .collect(Collectors.toList());
    }

    /**
     * Кликает по курсу с указанным названием.
     */
    public void clickByName(String name) {
        allCards.stream()
            .filter(c -> c.getTitle().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Курс не найден: " + name))
            .click();
    }

    /**
     * Возвращает все объекты CourseCardComponent.
     */
    public List<CourseCardComponent> getAllCards() {
        return allCards;
    }

    /**
     * Наводит курсор на пункт меню "Курсы".
     */
    public void hoverMainMenu() {
        new Actions(driver)
            .moveToElement(coursesMenu)
            .perform();
    }

    /**
     * Возвращает элементы подменю пункта "Курсы".
     */
    public List<WebElement> getSubMenuItems() {
        return subMenuItems;
    }
}
