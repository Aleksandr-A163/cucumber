package steps;

import com.google.inject.Injector;
import di.InjectorFactory;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import pages.CourseCatalogPage;
import pages.CourseInfo;

import java.util.Comparator;
import java.util.List;

public class PrepCoursesSteps {
    private CourseCatalogPage catalogPage;

    @Before
    public void init() {
        Injector injector = InjectorFactory.getInjector();
        catalogPage = injector.getInstance(CourseCatalogPage.class);
        catalogPage.open();
    }

    @When("Я перехожу в раздел {string}")
    public void openSection(String section) {
        catalogPage.openSection(section);
    }

    @Then("В консоль выведены самый дорогой и самый дешёвый курсы")
    public void printMinMax() {
        List<CourseInfo> list = catalogPage.getPreparatoryCourseInfos();
        CourseInfo min = list.stream().min(Comparator.comparing(CourseInfo::getPrice)).get();
        CourseInfo max = list.stream().max(Comparator.comparing(CourseInfo::getPrice)).get();
        System.out.printf("Cheapest: %s — %s%n", min.getName(), min.getPrice());
        System.out.printf("Most expensive: %s — %s%n", max.getName(), max.getPrice());
    }
}
