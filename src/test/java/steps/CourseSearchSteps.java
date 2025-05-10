package steps;

import com.google.inject.Injector;
import di.InjectorFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import pages.CourseCatalogPage;
import pages.CoursePage;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseSearchSteps {
    private Injector injector;
    private CourseCatalogPage catalogPage;
    private CoursePage coursePage;
    private String selectedCourse;

    @Before
    public void initPages() {
        injector     = InjectorFactory.getInjector();
        catalogPage  = injector.getInstance(CourseCatalogPage.class);
        coursePage   = injector.getInstance(CoursePage.class);
    }

    @Given("Я открываю каталог курсов")
    public void openCatalog() {
        catalogPage.open();
    }

    @When("Я выбираю случайный курс из списка:")
    public void pickRandomCourse(DataTable table) {
        List<String> desired = table.asList();
        List<String> all = catalogPage.getAllCourseTitles();
        List<String> filtered = all.stream()
                                   .filter(desired::contains)
                                   .collect(Collectors.toList());
        selectedCourse = filtered.get(new Random().nextInt(filtered.size()));
        catalogPage.clickOnCourseByName(selectedCourse);
    }

    @Then("Открывается страница выбранного курса")
    public void verifyCoursePage() {
        assertTrue(
            coursePage.isCorrectCourseOpened(selectedCourse),
            "Открыт неверный курс. Ожидался: " + selectedCourse
        );
    }
}