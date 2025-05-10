package components;

import pages.CourseCardComponent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import java.util.List;

/**
 * Компонент для работы со списком курсов и меню.
 */
public class CourseListComponent {

    @FindBy(css = "nav .menu-item-courses")
    private WebElement coursesMenu;

    @FindBy(css = "nav .menu-item-courses .submenu li")
    private List<WebElement> subMenuItems;

    @FindBy(css = ".course-card")
    private List<CourseCardComponent> allCards;

    public void waitForReady() {
        // существующий код ожидания
    }

    public List<CourseCardComponent> getCardsWithDates() {
        return allCards; // замените на реальную логику
    }

    public List<String> getAllTitles() {
        return allCards.stream()
                .map(CourseCardComponent::getTitle)
                .collect(java.util.stream.Collectors.toList());
    }

    public void clickByName(String name) {
        allCards.stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Курс не найден: " + name))
                .click();
    }

    public List<CourseCardComponent> getAllCards() {
        return allCards;
    }

    public void hoverMainMenu() {
        new Actions(DriverHolder.get())
            .moveToElement(coursesMenu)
            .perform();
    }

    public List<WebElement> getSubMenuItems() {
        return subMenuItems;
    }
}
