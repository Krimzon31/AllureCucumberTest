package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * Класс для управления пропертями
 */
public class testPropManager {

    /**
     * Переменна для хранения данных считанных из файла properties и переданных пользователем
     * Т.е. переменная для хранения пользовательских properties
     *
     * @see Properties - реализован на основе {@link java.util.Hashtable}
     */
    private final Properties properties = new Properties();


    /**
     * Переменна для хранения объекта TestPropManager
     */
    private static testPropManager INSTANCE = null;


    /**
     * Конструктор специально был объявлен как private (singleton паттерн)
     * Происходит загрузка содержимого файла application.properties в переменную {@link #properties}
     *
     * @see testPropManager#getTestPropManager()
     */
    private testPropManager() {
        loadApplicationProperties();
        loadCustomProperties();
    }


    /**
     * Метод ленивой инициализации TestPropManager
     *
     * @return TestPropManager - возвращает TestPropManager
     */
    public static testPropManager getTestPropManager() {
        if (INSTANCE == null) {
            INSTANCE = new testPropManager();
        }
        return INSTANCE;
    }


    /**
     * Метод подгружает содержимого файла application.properties в переменную {@link #properties}
     * Либо из файла переданного пользователем через настройку -DpropFile={nameFile}
     *
     * @see testPropManager#testPropManager()
     */
    private void loadApplicationProperties() {
        try {
            properties.load(new FileInputStream(
                    new File("src/test/resources/" +
                            System.getProperty("propFile", "application") + ".properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Метод заменяет значение содержащиеся в ключах переменной {@link #properties}
     * Заменяет на те значения, что передал пользователь через maven '-D{name.key}={value.key}'
     * Замена будет происходить только в том случае если пользователь передаст совпадающий key из application.properties
     *
     * @see testPropManager#testPropManager()
     */
    private void loadCustomProperties() {
        properties.forEach((key, value) -> System.getProperties()
                .forEach((customUserKey, customUserValue) -> {
                    if (key.toString().equals(customUserKey.toString()) &&
                            !value.toString().equals(customUserValue.toString())) {
                        properties.setProperty(key.toString(), customUserValue.toString());
                    }
                }));
    }


    /**
     * Метод возвращает либо значение записанное в ключе в переменной {@link #properties},
     * либо defaultValue при отсутствие ключа в переменной {@link #properties}
     *
     * @param key          - ключ, значения которого хотите получить
     * @param defaultValue - значение по умолчанию которое хотите получить если отсутствует ключ в {@link #properties}
     * @return String - возвращает системное св-во либо переданное default значение
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }


    /**
     * Метод возвращает значения записанное в ключе в переменной {@link #properties}, если нет переменной вернет null
     *
     * @param key - ключ, значения которого хотите получить
     * @return String - строка со значением ключа
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
