package pages;

import com.google.inject.Inject;
import components.CookieBannerComponent;
import components.CourseListComponent;
import components.CourseCardComponent;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
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

    @Inject
    public CourseCatalogPage(WebDriver driver,
                             CookieBannerComponent cookieBanner,
                             CourseListComponent courseList) {
        this.driver = driver;
        this.cookieBanner = cookieBanner;
        this.courseList = courseList;
    }

    public void open() {
        driver.get(URL);
        cookieBanner.acceptIfPresent();
        courseList.waitForReady();
    }

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

    public LocalDate getEarliestCourseDate() {
        return getAllCourseCardsWithDates().stream()
            .map(c -> c.tryGetStartDate().orElseThrow())
            .min(LocalDate::compareTo)
            .orElseThrow();
    }

    public LocalDate getLatestCourseDate() {
        return getAllCourseCardsWithDates().stream()
            .map(c -> c.tryGetStartDate().orElseThrow())
            .max(LocalDate::compareTo)
            .orElseThrow();
    }

    public List<String> getCourseTitlesByDate(LocalDate date) {
        return getAllCourseCardsWithDates().stream()
            .filter(c -> c.tryGetStartDate().orElseThrow().equals(date))
            .map(CourseCardComponent::getTitle)
            .distinct()
            .collect(Collectors.toList());
    }

    public void waitForCoursesToBeVisible() {
        courseList.waitForReady();
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
