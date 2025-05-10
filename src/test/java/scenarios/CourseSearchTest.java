package scenarios;

import com.google.inject.Inject;
import di.GuiceExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.CourseCatalogPage;
import pages.CoursePage;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Проверка перехода на случайную страницу курса из предзаданных названий")
@Tag("search")
@ExtendWith(GuiceExtension.class)
public class CourseSearchTest {

    @Inject
    private WebDriver driver;

    @Inject
    private CourseCatalogPage catalogPage;

    @Inject
    private CoursePage coursePage;

    @Test
    void randomCourseNavigation() {
        // Открываем каталог
        catalogPage.open();

        // Предзаданные названия курсов
        List<String> predefinedCourses = List.of(
            "Геймдизайн и левел-дизайн",
            "Разработка ядра Linux",
            "PHP Developer. Professional"
        );
        String courseName = predefinedCourses.get(
            new Random().nextInt(predefinedCourses.size())
        );

        // Кликаем по выбранному курсу
        catalogPage.clickOnCourseByName(courseName);

        // Проверяем, что открыт правильный курс
        assertTrue(
            coursePage.isCorrectCourseOpened(courseName),
            "Открыт неверный курс. Ожидался: " + courseName
        );
    }
}