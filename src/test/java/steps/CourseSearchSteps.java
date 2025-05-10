package steps;

import com.google.inject.Injector;
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
        // Инициализируем через Guice один раз перед каждым сценарием
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
        // Получаем все названия курсов с текущей страницы
        List<String> all = catalogPage.getAllCourseTitles();
        // Оставляем только те, что заданы в фиче
        List<String> filtered = all.stream()
                                   .filter(desired::contains)
                                   .collect(Collectors.toList());
        // Выбираем случайный
        selectedCourse = filtered.get(new Random().nextInt(filtered.size()));
        // Кликаем по нему
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
