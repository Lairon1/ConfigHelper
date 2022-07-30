package com.xlairon.plugins.confighelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigDataObject<T> {

    protected String path;
    protected T defaultValue;
    protected T value;
    protected String[] comment;

    public ConfigDataObject(String path, T defaultValue) {
        this(path, defaultValue, null);
    }

    public ConfigDataObject(String path, T defaultValue, String... comment) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.comment = comment;
    }

    public T getValue() {
        if (value == null) return defaultValue;
        return value;
    }

    public String setPlaceholdersString(HashMap<String, String> placeholders) {
        if (!(getValue() instanceof String))
            throw new IllegalArgumentException("This config field != String");
        String str = (String) getValue();
        for (String key : placeholders.keySet())
            str = str.replace(key, placeholders.get(key));
        return str;
    }


    public String setPlaceholdersString(String... placeholders) {
        if (!(getValue() instanceof String))
            throw new IllegalArgumentException("This config field != String");
        String str = (String) getValue();
        for (int i = 0; i < placeholders.length; i += 2)
            str = str.replace(placeholders[i], placeholders[i + 1]);
        return str;
    }


}
