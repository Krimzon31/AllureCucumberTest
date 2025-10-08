package org.example;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class connection {
    protected static WebDriver driver;
    private static String baseAddrsess = "http://217.74.37.176/?route=account/register&language=ru-ru";

    @BeforeAll
    public static void setUp() {
        try {
            // Настройка Selenium WebDriver
            DesiredCapabilities capabilities = new DesiredCapabilities();
            Map<String, Object> selenoidOptions = new HashMap<>();
            selenoidOptions.put("browserName", "chrome");
            selenoidOptions.put("browserVersion", "109.0");
            selenoidOptions.put("enableVNC", true);
            selenoidOptions.put("enableVideo", false);
            capabilities.setCapability("selenoid:options", selenoidOptions);

            try {
                driver = new RemoteWebDriver(
                        URI.create("http://applineselenoid.fvds.ru:4444/wd/hub/").toURL(),
                        capabilities
                );
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            driver.get(baseAddrsess);
            System.out.println("WebDriver инициализирован, открыта страница: " + baseAddrsess);

        } catch (Exception e) {
            System.err.println("Ошибка при инициализации теста: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDown() {
        // Закрытие WebDriver
        if (driver != null) {
            driver.quit();
            System.out.println("WebDriver закрыт");
        }
    }
}