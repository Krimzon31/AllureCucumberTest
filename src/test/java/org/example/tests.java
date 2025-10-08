package org.example;

import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.То;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

import static org.example.connection.driver;

public class tests {
    private String registrationPageUrl = "http://217.74.37.176/?route=account/register&language=ru-ru";
    private String logoutUrl = "http://217.74.37.176/?route=account/logout&language=ru-ru";
    private Random random = new Random();

    @Дано("пользователь находится на странице регистрации")
    @Description("Открывает страницу регистрации")
    @Owner("Admin")
    public void пользователь_находится_на_странице_регистрации() {
        driver.get(registrationPageUrl);
        sleep(1000);
        // Проверяем, что мы на правильной странице
        Assert.assertTrue("Должны быть на странице регистрации",
                driver.getCurrentUrl().contains("register") ||
                        driver.findElements(By.xpath("//h1[contains(text(),'Регистрация')]")).size() > 0);
    }

    @И("заполняются поля регистрации случайным email")
    @Description("Заполняет поля формы регистрации со случайным email")
    @Owner("Tom Hanks")
    public void заполняются_поля_регистрации_случайным_email() {
        String randomEmail = generateRandomEmail();
        String firstName = "Иван";
        String lastName = "Петров";
        String password = "password123";

        WebElement inputFirstName = driver.findElement(By.xpath("//input[@name='firstname']"));
        WebElement inputLastName = driver.findElement(By.xpath("//input[@name='lastname']"));
        WebElement inputEmail = driver.findElement(By.xpath("//input[@name='email']"));
        WebElement inputPassword = driver.findElement(By.xpath("//input[@name='password']"));

        inputFirstName.clear();
        inputFirstName.sendKeys(firstName);

        inputLastName.clear();
        inputLastName.sendKeys(lastName);

        inputEmail.clear();
        inputEmail.sendKeys(randomEmail);

        inputPassword.clear();
        inputPassword.sendKeys(password);

        System.out.println("Использован случайный email: " + randomEmail);
    }

    @И("выход из профиля и возврат на страницу регистрации")
    @Description("Выходит из профиля и возвращается на страницу регистрации")
    @Owner("Admin")
    public void выход_из_профиля_и_возврат_на_страницу_регистрации() {
        driver.get(logoutUrl);
        sleep(1000);

        // После выхода возвращаемся на страницу регистрации
        driver.get(registrationPageUrl);
        sleep(1000);
        System.out.println("Вернулись на страницу регистрации");
    }

    @И("принимаются условия политики конфиденциальности")
    @Description("Активирует чекбокс согласия с политикой конфиденциальности")
    @Owner("Natalie Portman")
    public void принимаются_условия_политики_конфиденциальности() {
        WebElement privacyCheckbox = driver.findElement(By.xpath("//input[@name='agree']"));
        if (!privacyCheckbox.isSelected()) {
            privacyCheckbox.click();
        }
    }

    @И("нажимается кнопка Продолжить")
    @Description("Нажимает кнопку Продолжить для завершения регистрации")
    @Owner("Scarlett Johansson")
    public void нажимается_кнопка_Продолжить() {
        WebElement continueButton = driver.findElement(By.xpath("//button[contains(text(),'Продолжить')] | //input[@type='submit']"));
        continueButton.click();
        sleep(2000);
    }

    @То("проверка успешной регистрации по переходу на другую страницу")
    @Description("Проверяет успешную регистрацию по переходу на другую страницу")
    @Owner("Brad Pitt")
    public void проверка_успешной_регистрации_по_переходу_на_другую_страницу() {
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Текущий URL после регистрации: " + currentUrl);

        // Проверяем, что произошел переход с страницы регистрации
        Assert.assertFalse("Должен быть переход на другую страницу",
                currentUrl.contains("register"));

        // Проверяем, что мы не остались на странице регистрации
        Assert.assertFalse("Не должны остаться на странице регистрации",
                currentUrl.equals(registrationPageUrl));

        System.out.println("Регистрация успешна - произошел переход на: " + currentUrl);
    }

    @То("проверка что остались на странице регистрации")
    @Description("Проверяет, что остались на странице регистрации при ошибке")
    @Owner("Meryl Streep")
    public void проверка_что_остались_на_странице_регистрации() {
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Текущий URL: " + currentUrl);

        // Проверяем, что остались на странице регистрации или вернулись на нее
        boolean isOnRegistrationPage = currentUrl.contains("register") ||
                driver.findElements(By.xpath("//h1[contains(text(),'Регистрация')]")).size() > 0;

        Assert.assertTrue("Должны остаться на странице регистрации при ошибке", isOnRegistrationPage);
        System.out.println("Остались на странице регистрации - ошибка обработана корректно");
    }

    @То("проверка ошибки валидации поля {string}")
    @Description("Проверяет отображение ошибки валидации для конкретного поля")
    @Owner("Robert Downey Jr.")
    public void проверка_ошибки_валидации_поля(String fieldName) {
        try {
            String errorId = "";
            String expectedText = "";

            switch (fieldName) {
                case "Имя":
                    errorId = "error-firstname";
                    expectedText = "Имя должно быть от 1 до 32 символов!";
                    break;
                case "Фамилия":
                    errorId = "error-lastname";
                    expectedText = "Фамилия должна быть от 1 до 32 символов!";
                    break;
                case "E-Mail":
                    errorId = "error-email";
                    expectedText = "E-Mail введен неправильно!";
                    break;
                case "Пароль":
                    errorId = "error-password";
                    expectedText = "В пароле должно быть от 4 до 20 символов!";
                    break;
            }

            WebElement fieldError = driver.findElement(By.id(errorId));
            Assert.assertTrue("Должна отображаться ошибка для поля " + fieldName, fieldError.isDisplayed());
            String actualText = fieldError.getText();
            Assert.assertTrue("Текст ошибки должен содержать: " + expectedText + ", а получили: " + actualText,
                    actualText.contains(expectedText));
            System.out.println("Ошибка валидации для поля " + fieldName + ": " + actualText);

        } catch (Exception e) {
            Assert.fail("Не найдена ошибка валидации для поля: " + fieldName + ". " + e.getMessage());
        }
    }

    @То("проверка ошибки политики конфиденциальности")
    @Description("Проверяет отображение ошибки о необходимости принятия политики конфиденциальности")
    @Owner("Chris Evans")
    public void проверка_ошибки_политики_конфиденциальности() {
        try {
            // Ищем alert toast сообщение
            WebElement alertError = driver.findElement(By.xpath(
                    "//div[contains(@class,'alert-danger')]//*[contains(text(),'Privacy Policy')] | " +
                            "//div[contains(@class,'alert-danger')]//*[contains(text(),'политикой')] | " +
                            "//*[contains(text(),'Privacy Policy')] | " +
                            "//*[contains(text(),'политикой')]"
            ));
            Assert.assertTrue("Должна отображаться ошибка политики конфиденциальности", alertError.isDisplayed());
            System.out.println("Обнаружена ошибка политики конфиденциальности: " + alertError.getText());

        } catch (Exception e) {
            // Если не нашли конкретное сообщение, ищем любой alert
            try {
                WebElement alertContainer = driver.findElement(By.xpath("//div[contains(@class,'alert-danger')]"));
                Assert.assertTrue("Должен отображаться alert об ошибке", alertContainer.isDisplayed());
                System.out.println("Обнаружен alert об ошибке: " + alertContainer.getText());
            } catch (Exception ex) {
                Assert.fail("Не найдена ошибка политики конфиденциальности");
            }
        }
    }

    @То("проверка ошибки существующего email")
    @Description("Проверяет отображение ошибки о уже существующем email")
    @Owner("Chris Hemsworth")
    public void проверка_ошибки_существующего_email() {
        try {
            // Ищем сообщение о существующем email
            WebElement emailError = driver.findElement(By.xpath(
                    "//div[contains(@class,'alert-danger')]//*[contains(text(),'уже')] | " +
                            "//div[contains(@class,'alert-danger')]//*[contains(text(),'существует')] | " +
                            "//div[contains(@class,'alert-danger')]//*[contains(text(),'already')] | " +
                            "//*[contains(text(),'E-Mail уже зарегистрирован')]"
            ));
            Assert.assertTrue("Должна отображаться ошибка существующего email", emailError.isDisplayed());
            System.out.println("Обнаружена ошибка существующего email: " + emailError.getText());

        } catch (Exception e) {
            // Если не нашли конкретное сообщение, проверяем общую ошибку email
            проверка_ошибки_валидации_поля("E-Mail");
        }
    }
    @И("поле {string} заполняется значением {string}")
    @Description("Заполняет конкретное поле указанным значением")
    @Owner("Mark Ruffalo")
    public void поле_заполняется_значением(String fieldName, String value) {
        WebElement field = null;

        switch (fieldName) {
            case "Имя":
                field = driver.findElement(By.xpath("//input[@name='firstname']"));
                break;
            case "Фамилия":
                field = driver.findElement(By.xpath("//input[@name='lastname']"));
                break;
            case "E-Mail":
                field = driver.findElement(By.xpath("//input[@name='email']"));
                break;
            case "Пароль":
                field = driver.findElement(By.xpath("//input[@name='password']"));
                break;
        }

        if (field != null) {
            field.clear();
            if (!value.equals("пусто")) {
                field.sendKeys(value);
            }
        }
    }

    @И("заполняются поля регистрации данными {string}, {string}, {string}, {string}")
    @Description("Заполняет поля формы регистрации")
    @Owner("Tom Hanks")
    public void заполняются_поля_регистрации_данными(String firstName, String lastName, String email, String password) {
        WebElement inputFirstName = driver.findElement(By.xpath("//input[@name='firstname']"));
        WebElement inputLastName = driver.findElement(By.xpath("//input[@name='lastname']"));
        WebElement inputEmail = driver.findElement(By.xpath("//input[@name='email']"));
        WebElement inputPassword = driver.findElement(By.xpath("//input[@name='password']"));

        inputFirstName.clear();
        inputFirstName.sendKeys(firstName);

        inputLastName.clear();
        inputLastName.sendKeys(lastName);

        inputEmail.clear();
        inputEmail.sendKeys(email);

        inputPassword.clear();
        inputPassword.sendKeys(password);
    }

    @И("чекбокс политики конфиденциальности не активирован")
    @Description("Оставляет чекбокс политики конфиденциальности неактивным")
    @Owner("Jeremy Renner")
    public void чекбокс_политики_конфиденциальности_не_активирован() {
        WebElement privacyCheckbox = driver.findElement(By.xpath("//input[@name='agree']"));
        if (privacyCheckbox.isSelected()) {
            privacyCheckbox.click();
        }
    }

    @И("все поля остаются пустыми")
    @Description("Оставляет все поля формы пустыми")
    @Owner("Paul Rudd")
    public void все_поля_остаются_пустыми() {
        // Очищаем все поля
        driver.findElement(By.xpath("//input[@name='firstname']")).clear();
        driver.findElement(By.xpath("//input[@name='lastname']")).clear();
        driver.findElement(By.xpath("//input[@name='email']")).clear();
        driver.findElement(By.xpath("//input[@name='password']")).clear();
        System.out.println("Все поля очищены");
    }

    // Метод для генерации случайного email
    private String generateRandomEmail() {
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "mail.ru", "yandex.ru"};
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder username = new StringBuilder();

        // Генерируем случайное имя пользователя длиной 8-12 символов
        int usernameLength = 8 + random.nextInt(5);
        for (int i = 0; i < usernameLength; i++) {
            username.append(chars.charAt(random.nextInt(chars.length())));
        }

        // Выбираем случайный домен
        String domain = domains[random.nextInt(domains.length)];

        return username.toString() + "@" + domain;
    }

    public void sleep(int timeOfSleep) {
        try {
            Thread.sleep(timeOfSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}