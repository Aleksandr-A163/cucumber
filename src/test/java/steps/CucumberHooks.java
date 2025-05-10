package steps;

import driver.WebDriverProvider;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class CucumberHooks {
    @Before
    public void beforeScenario() {
        // Driver will be created by WebDriverProvider on first access
    }

    @After
    public void afterScenario() {
        WebDriverProvider.quitDriver();
    }
}
