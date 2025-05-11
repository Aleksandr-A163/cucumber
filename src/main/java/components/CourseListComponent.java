package components;

import com.google.inject.Inject;
import org.openqa.selenium.By;
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

    // Список компонентов курсов
    private List<CourseCardComponent> allCards;

    /**
     * Конструктор: инициализируем стандартные элементы меню.
     */
    @Inject
    public CourseListComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Ждёт, пока хотя бы одна карточка курсов станет видимой, затем оборачивает все найденные
     * элементы карточек в CourseCardComponent.
     */
    public void waitForReady() {
        List<WebElement> roots = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector(".course-card")
            ));

        this.allCards = roots.stream()
            .map(root -> new CourseCardComponent(driver, root))
            .collect(Collectors.toList());
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