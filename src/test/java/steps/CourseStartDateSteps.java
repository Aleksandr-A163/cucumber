package steps;

import com.google.inject.Injector;
import di.InjectorFactory;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import pages.CourseCatalogPage;
import pages.CourseInfo;

import java.time.LocalDate;
import java.util.List;

/**
 * Шаги Cucumber для проверки дат начала курсов.
 */
public class CourseStartDateSteps {
    private CourseCatalogPage catalogPage;
    private List<CourseInfo> filteredCourses;
    private LocalDate filterDate;

    @Before
    public void init() {
        Injector injector = InjectorFactory.getInjector();
        catalogPage = injector.getInstance(CourseCatalogPage.class);
        catalogPage.open();
    }

    @When("Я фильтрую курсы по дате старта {string}")
    public void filterByStartDate(String isoDate) {
        filterDate = LocalDate.parse(isoDate);
        filteredCourses = catalogPage.getCoursesStartingOnOrAfter(filterDate);
    }

    @Then("В консоль выведены названия и даты этих курсов")
    public void thenCoursesListed() {
        Assertions.assertNotNull(filteredCourses, "Список курсов не был инициализирован");
        Assertions.assertFalse(filteredCourses.isEmpty(),
            String.format("Нет курсов, стартующих с %s или позже", filterDate)
        );
        for (CourseInfo ci : filteredCourses) {
            Assertions.assertFalse(
                ci.getStartDate().isBefore(filterDate),
                String.format("Курс %s стартует раньше фильтра: %s",
                    ci.getName(), ci.getStartDate())
            );
            System.out.printf("%s — %s%n", ci.getName(), ci.getStartDate());
        }
    }
}
