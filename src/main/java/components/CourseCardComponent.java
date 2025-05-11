package components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Компонент-обёртка для карточки курса.
 */
public class CourseCardComponent {

    private final WebDriver driver;
    private final WebElement rootElement;

    @FindBy(css = ".course-card__title")
    private WebElement titleElement;

    @FindBy(css = ".course-card__price")
    private WebElement priceElement;

    @FindBy(css = ".course-card__date")
    private WebElement dateElement;

    @FindBy(css = ".course-card__category")
    private WebElement categoryElement;

    /**
     * Конструктор: сохраняет rootElement и инициализирует все @FindBy
     * относительно этого корня.
     */
    public CourseCardComponent(WebDriver driver, WebElement rootElement) {
        this.driver = driver;
        this.rootElement = rootElement;
        // инициализируем titleElement, priceElement, dateElement и categoryElement
        PageFactory.initElements(
            new DefaultElementLocatorFactory(rootElement),
            this
        );
    }

    /** Корневой элемент карточки */
    public WebElement getRootElement() {
        return rootElement;
    }

    /** Заголовок курса */
    public String getTitle() {
        return titleElement.getText().trim();
    }

    /** Цена курса */
    public BigDecimal getPrice() {
        String raw = priceElement.getText()
            .replaceAll("[^0-9,\\.]", "")
            .replace(",", ".");
        return new BigDecimal(raw);
    }

    /** Дата старта, если есть */
    public Optional<LocalDate> tryGetStartDate() {
        try {
            return Optional.of(LocalDate.parse(dateElement.getText().trim()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /** Проверка категории */
    public boolean isInCategory(String category) {
        return categoryElement.getText().trim()
                              .equalsIgnoreCase(category);
    }

    /** Клик на карточку */
    public void click() {
        rootElement.click();
    }
}
