package pages;

import com.google.inject.Inject;
import components.CookieBannerComponent;
import components.CourseListComponent;
import components.CourseCardComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object для страницы каталога курсов.
 */
public class CourseCatalogPage {

    private static final String URL = "https://otus.ru/catalog/courses";

    private final WebDriver driver;
    private final CookieBannerComponent cookieBanner;
    private final CourseListComponent courseList;

    /**
     * Конструктор с инъекцией зависимостей и инициализацией полей через PageFactory.
     */
    @Inject
    public CourseCatalogPage(WebDriver driver,
                             CookieBannerComponent cookieBanner,
                             CourseListComponent courseList) {
        this.driver = driver;
        this.cookieBanner = cookieBanner;
        this.courseList = courseList;
        PageFactory.initElements(driver, this);
    }

    /**
     * Открывает страницу каталога, принимает баннер и ждёт готовности.
     */
    public CourseCatalogPage open() {
        driver.get(URL);
        cookieBanner.acceptIfPresent();
        courseList.waitForReady();
        return this;
    }

    /**
     * Проверяет, что страница открыта.
     */
    public boolean isOpened() {
        courseList.waitForReady();
        return !courseList.getAllCards().isEmpty();
    }

    public List<String> getAllCourseTitles() {
        return courseList.getAllTitles();
    }

    public void clickOnCourseByName(String name) {
        courseList.clickByName(name);
    }

    public List<CourseCardComponent> getAllCourseCardsWithDates() {
        return courseList.getCardsWithDates();
    }


    // ---------------- Новые методы BDD ----------------

    public void openSection(String sectionName) {
        courseList.hoverMainMenu();
        courseList.getSubMenuItems().stream()
            .filter(item -> item.getText().trim().equalsIgnoreCase(sectionName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Секция не найдена: " + sectionName))
            .click();
    }

    public List<CourseInfo> getPreparatoryCourseInfos() {
        return courseList.getAllCards().stream()
            .filter(card -> card.isInCategory("Подготовительные курсы"))
            .map(card -> new CourseInfo(
                card.getTitle(),
                card.getPrice()
            ))
            .collect(Collectors.toList());
    }

    public List<CourseInfo> getCoursesStartingOnOrAfter(LocalDate date) {
        return getAllCourseCardsWithDates().stream()
            .map(c -> new CourseInfo(
                c.getTitle(),
                c.tryGetStartDate().orElseThrow()
            ))
            .filter(ci -> !ci.getStartDate().isBefore(date))
            .collect(Collectors.toList());
    }
}