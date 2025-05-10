package steps;

import com.google.inject.Injector;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import pages.CourseCatalogPage;
import pages.CourseInfo;

import java.time.LocalDate;
import java.util.List;

public class CourseStartDateSteps {
    private CourseCatalogPage catalogPage;

    @Before
    public void init() {
        Injector injector = InjectorFactory.getInjector();
        catalogPage = injector.getInstance(CourseCatalogPage.class);
        catalogPage.open();
    }

    @When("Я фильтрую курсы по дате старта {string}")
    public void filterByStartDate(String isoDate) {
        LocalDate date = LocalDate.parse(isoDate);
        List<CourseInfo> list = catalogPage.getCoursesStartingOnOrAfter(date);
        list.forEach(c -> System.out.printf("%s — %s%n", c.getName(), c.getStartDate()));
    }

    @Then("В консоль выведены названия и даты этих курсов")
    public void thenNothing() {
    }
}
