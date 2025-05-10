package components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Компонент-обёртка для карточки курса.
 */
public class CourseCardComponent {

    @FindBy(css = ".course-card__title")
    private WebElement titleElement;

    @FindBy(css = ".course-card__price")
    private WebElement priceElement;

    @FindBy(css = ".course-card__date")
    private WebElement dateElement;

    @FindBy(css = ".course-card__category")
    private WebElement categoryElement;

    public String getTitle() {
        return titleElement.getText().trim();
    }

    public BigDecimal getPrice() {
        String raw = priceElement.getText().replaceAll("[^0-9,\\.]","")
                             .replace(",", ".");
        return new BigDecimal(raw);
    }

    public Optional<LocalDate> tryGetStartDate() {
        return Optional.of(LocalDate.parse(dateElement.getText().trim()));
    }

    public boolean isInCategory(String category) {
        return categoryElement.getText().trim().equalsIgnoreCase(category);
    }

    public void click() {
        titleElement.click();
    }
}
