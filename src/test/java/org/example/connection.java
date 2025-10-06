package org.example;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class connection {
    protected static WebDriver driver;
    private static String baseAddress = "http://localhost:8080/food";
    protected static java.sql.Connection connection;
    protected static Statement statement;

    @BeforeAll
    public static void setUp() {

        try{
            /*System.setProperty("webdriver.chrome.driver",
                    "AllureCucumberTest\\src\\test\\resources\\chromedriver.exe");

            driver = new ChromeDriver(); // присваиваем полю класса

             */
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
            }
            catch (MalformedURLException e){
                throw new RuntimeException(e);
            }

            driver.get(baseAddress);


            connection = DriverManager.getConnection(
                    "jdbc:h2:tcp://localhost:9092/mem:testdb",
                    "user",
                    "pass");

            statement = connection.createStatement();

            System.out.println("Подключение к базе установлено");
        }
        catch (SQLException sqlExc){
            System.err.println("Не удалось установить подключение к базе данных: " + sqlExc.getMessage());
        }
        catch(Exception e) {
            System.err.println("Ошибка при инициализации теста: " + e.getMessage());
        }
    }
    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }


        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Подключение к базе закрыто");
                }
            } catch (SQLException sqlExc) {
                System.err.println("Ошибка при закрытии подключения к базе: " + sqlExc.getMessage());
            }
        }
    }
}
