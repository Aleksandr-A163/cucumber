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
 * Делегирует логику работы с баннером и списком карточек соответствующим компонентам.
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

    /** Открывает страницу каталога и ждёт загрузки карточек */
    public void open() {
        driver.get(URL);
        cookieBanner.acceptIfPresent();
        courseList.waitForReady();
    }

    /** Проверяет, что на странице отображены карточки курсов */
    public boolean isOpened() {
        courseList.waitForReady();
        return !courseList.getAllCards().isEmpty();
    }

    /** Возвращает все заголовки курсов */
    public List<String> getAllCourseTitles() {
        return courseList.getAllTitles();
    }

    /** Кликает по курсу с указанным названием */
    public void clickOnCourseByName(String name) {
        courseList.clickByName(name);
    }

    /** Возвращает список карточек, у которых есть дата старта */
    public List<CourseCardComponent> getAllCourseCardsWithDates() {
        return courseList.getCardsWithDates();
    }

    /** Находит самую раннюю дату старта среди карточек */
    public LocalDate getEarliestCourseDate() {
        return getAllCourseCardsWithDates().stream()
            .map(c -> c.tryGetStartDate().orElseThrow())
            .min(LocalDate::compareTo)
            .orElseThrow();
    }

    /** Находит самую позднюю дату старта среди карточек */
    public LocalDate getLatestCourseDate() {
        return getAllCourseCardsWithDates().stream()
            .map(c -> c.tryGetStartDate().orElseThrow())
            .max(LocalDate::compareTo)
            .orElseThrow();
    }

    /** Возвращает заголовки курсов, которые стартуют в заданную дату */
    public List<String> getCourseTitlesByDate(LocalDate date) {
        return getAllCourseCardsWithDates().stream()
            .filter(c -> c.tryGetStartDate().orElseThrow().equals(date))
            .map(CourseCardComponent::getTitle)
            .distinct()
            .collect(Collectors.toList());
    }

    /** Ждёт, пока карточки станут доступны в DOM */
    public void waitForCoursesToBeVisible() {
        courseList.waitForReady();
    }

    // ---------------- Новые методы BDD ----------------

    /**
     * Открывает в меню "Курсы" подраздел с именем sectionName.
     */
    public void openSection(String sectionName) {
        // Наводим на главный пункт меню "Курсы"
        courseList.hoverMainMenu("Курсы");
        // Выбираем и кликаем нужный пункт
        courseList.getSubMenuItems().stream()
            .filter(item -> item.getText().trim().equalsIgnoreCase(sectionName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Секция не найдена: " + sectionName))
            .click();
    }

    /**
     * Возвращает информацию по подготовительным курсам (название + цена).
     */
    public List<CourseInfo> getPreparatoryCourseInfos() {
        return courseList.getAllCards().stream()
            .filter(card -> card.isInCategory("Подготовительные курсы"))
            .map(card -> new CourseInfo(
                card.getTitle(),
                card.getPrice()  // BigDecimal или нужный тип цены
            ))
            .collect(Collectors.toList());
    }
}
