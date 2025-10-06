package org.example;

import io.cucumber.java.ru.И;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.connection.driver;
import static org.example.connection.statement;

public class test {
    int beginId;

    String querySelectLastId = "SELECT MAX(FOOD_ID) AS MAXID FROM FOOD";

    @И("получение идентификатора последнего добавленного товара")
    @Description("Получает идентификатор последнего добавленного товара из таблицы FOOD")
    @Owner("Krim")
    public void получение_идентификатора_последнего_добавленного_товара() {
        beginId = getLastId();
    }

    @И("проверка открыта ли форма добавления")
    @Description("Проверяет, открыта ли форма добавления товара, и открывает её при необходимости")
    @Owner("Krim")
    public void проверка_открыта_ли_форма_добавления() {
        WebElement dialog = driver.findElement(By.id("editModal"));

        if (!dialog.isDisplayed()) {
            buttonAdd();
        }
    }

    @И("поля Название Экзотический Тип заполняются данными {string}, {string}, {string}")
    @Description("Заполняет поля Название, Экзотический и Тип данными из сценария")
    @Owner("Krim")
    public void поля_название_экзотический_тип_заполняются_данными(String name, String isExotic, String type) {
        boolean exotic = Boolean.parseBoolean(isExotic);
        WebElement inputName = driver.findElement(By.xpath("//input[@placeholder='Наименование']"));
        WebElement checkBoxExotic = driver.findElement(By.xpath("//input[@name='exotic']"));
        WebElement selectElement = driver.findElement(By.xpath("//select[@id='type']"));
        Select select = new Select(selectElement);

        inputName.clear();
        inputName.sendKeys(name);

        if (exotic) {
            if (!checkBoxExotic.isSelected()) {
                checkBoxExotic.click();
            }
        }
        else {
            if (checkBoxExotic.isSelected()) {
                checkBoxExotic.click();
            }
        }
        select.selectByVisibleText(type);
    }

    @И("нажимается кнопка Сохранить")
    @Description("Нажимает кнопку Сохранить в форме добавления товара")
    @Owner("Krim")
    public void нажимается_кнопка_Сохранить() {
        WebElement buttonSave = driver.findElement(By.xpath("//button[text()='Сохранить']"));
        buttonSave.click();
        sleep(500);
    }

    @И("проверка разости идентификаторов после теста")
    @Description("Проверяет, что идентификатор последнего элемента изменился после добавления нового товара")
    @Owner("Krim")
    public void проверка_разности_идентификаторов_после_теста(){
        Assert.assertNotEquals(beginId, getLastId());
    }

    @И("проверка эквивалентности идентификаторов последних элементов до после теста")
    @Description("Проверяет, что идентификатор последнего элемента не изменился после операции")
    @Owner("Krim")
    public void проверка_эквивалентности_идентификаторов_последних_элементов_до_после_теста(){
        Assert.assertEquals(beginId, getLastId());
    }

    public int getLastId(){
        int id = 0;

        try (ResultSet resultSet = statement.executeQuery(querySelectLastId)) {
            if (resultSet.next()) {
                id = resultSet.getInt("MAXID");
            }
            return id;

        } catch (SQLException sqlExc) {
            System.err.println("Не удалось получить данные о последнем ID: " + sqlExc.getMessage());
            return id;
        }
    }

    public void sleep(int timeOfSleep){
        try {
            Thread.sleep(timeOfSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void buttonAdd(){
        WebElement buttonAdd = driver.findElement(By.xpath("//button[text()='Добавить']"));
        buttonAdd.click();
        sleep(500);
    }
}
