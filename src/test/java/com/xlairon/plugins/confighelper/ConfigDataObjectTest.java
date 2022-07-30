package com.xlairon.plugins.confighelper;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ConfigDataObjectTest {

    private ConfigDataObject<String> data = new ConfigDataObject<>("test", "Вы не можете присоединится к серверу {server} так как у вас нет" +
            "доната {donate}. Но вы можете приобрести его на нашем сайте {site}.");

    @Test
    void setPlaceholdersStringMap() {
        HashMap<String, String> placeholders = new HashMap<>(){{
            put("{server}", "Анархия");
            put("{donate}", "MVP+");
            put("{site}", "minecraft.ru");
        }};
        String result = data.setPlaceholdersString(placeholders);
        assertEquals(result, "Вы не можете присоединится к серверу Анархия так как у вас нет" +
                "доната MVP+. Но вы можете приобрести его на нашем сайте minecraft.ru.");
    }

    @Test
    void setPlaceholdersStringArray() {
        String[] placeholders = {
                "{server}", "Анархия",
                "{donate}", "MVP+",
                "{site}", "minecraft.ru"
        };
        String result = data.setPlaceholdersString(placeholders);
        assertEquals(result, "Вы не можете присоединится к серверу Анархия так как у вас нет" +
                "доната MVP+. Но вы можете приобрести его на нашем сайте minecraft.ru.");
    }
}