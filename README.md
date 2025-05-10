# Автотесты для OTUS.ru

Этот проект содержит набор автоматизированных тестов для сайта OTUS.ru, реализованных с использованием Java, Selenium WebDriver, Google Guice и JUnit 5.

---

## 📦 Структура проекта

```
src/
├─ main/java/
│  ├─ components/    # Web‑компоненты (CourseListComponent, CookieBannerComponent, HeaderMenuComponent)
│  ├─ pages/         # Page Object классы (MainPage, CourseCatalogPage, CoursePage)
│  ├─ driver/        # Фабрика WebDriver (WebDriverProvider, WebDriverFactory)
│  ├─ di/            # Guice‑модуль (TestModule) и расширение (GuiceExtension)
│  └─ utils/         # Вспомогательные утилиты (HighlightingListener)
└─ test/java/
   └─ scenarios/     # Сценарии тестов (CourseSearchTest, CourseDateTest, CourseCategoryTest)
```

---

## 🚀 Быстрый старт

1. **Клонировать репозиторий**

   ```bash
   git clone <https://github.com/Aleksandr-A163/SeleniumHomeWork>
   ```

2. **Запустить все тесты**

   ```bash
   ./gradlew clean test
   ```

3. **Отчёт по тестам**
   После выполнения отчёта будет доступен по пути:

   ```
   build/reports/tests/test/index.html
   ```

---

## 📝 Реализованные задачи

1. **Dependency Injection** с помощью Google Guice:

   * Все Page/Component объекты создаются через `@Inject`.
2. **JUnit 5 Extension** вместо наследования от `BaseTest`:

   * Класс `GuiceExtension` управляет инъекцией и закрытием WebDriver.
3. **SpotBugs** и **Checkstyle** интеграция:

   * Конфигурации линтеров взяты из занятий и подключены в `build.gradle`.
4. **2‑уровневый тест-дизайн**:

   * Сценарии в `scenarios/`, Page Object в `pages/`, переиспользуемые компоненты в `components/`.
5. **Подсветка элементов** перед кликом/вводом:

   * `HighlightingListener` реализует `WebDriverListener`, обёртывается через `EventFiringDecorator`.
6. **Selenium WebDriver ≥ 4** (через WebDriverManager).
7. **Gradle** для сборки и запуска.
8. **Сценарии тестирования**:

   * Сценарий 1: Навигация на случайный курс из предзаданного списка.
   * Сценарий 2: Проверка самых ранних и поздних курсов по дате старта.
   * Сценарий 3: Выбор случайной категории через меню «Обучение» и проверка URL.

---

## 🔧 Конфигурация

* **Java 17**
* **ChromeDriver** автоматически управляется через WebDriverManager
* При необходимости можно добавить опции запуска в `driver/WebDriverProvider.java`

---

## 📈 Отчёты и логирование

* Отчёты по тестам: `build/reports/tests/test/index.html`
* Логи в консоли содержат информацию о подсветке и навигации

---


