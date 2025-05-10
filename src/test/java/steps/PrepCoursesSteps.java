package steps;

import io.cucumber.java.en.*;
import java.util.Comparator;
import pages.CourseCatalogPage;

public class PrepCoursesSteps {
    private final CourseCatalogPage catalog = InjectorFactory.getInjector()
                                        .getInstance(CourseCatalogPage.class);

    @When("Я перехожу в раздел {string}")
    public void openSubsection(String section) {
        catalog.openSection(section);  // например, clickMenuItem("Курсы", section)
    }

    @Then("В консоль выведены самый дорогой и самый дешёвый курсы")
    public void printMinMax() {
        var list = catalog.getPreparatoryCourseInfos(); // List<CourseInfo> с price
        CourseInfo min = list.stream().min(Comparator.comparing(CourseInfo::getPrice)).get();
        CourseInfo max = list.stream().max(Comparator.comparing(CourseInfo::getPrice)).get();
        System.out.printf("Cheapest: %s — %s%n", min.getName(), min.getPrice());
        System.out.printf("Most expensive: %s — %s%n", max.getName(), max.getPrice());
    }
}