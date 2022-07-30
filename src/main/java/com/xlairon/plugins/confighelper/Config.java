package com.xlairon.plugins.confighelper;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Config {

    private final Plugin plugin;
    private final String filePath;
    private File file;
    private FileConfiguration config;
    private ArrayList<ConfigDataObject> data = new ArrayList<>();

    public Config(Plugin plugin, String filePath) {
        this(plugin, filePath, null);
    }

    public Config(Plugin plugin, String filePath, Consumer<Config> consumer) {
        this.plugin = plugin;
        this.filePath = filePath;
        if (consumer != null) consumer.accept(this);
    }

    private void reloadConfiguration() throws IOException, InvalidConfigurationException {
        boolean save = false;
        if (file == null)
            file = new File(plugin.getDataFolder() + File.separator + filePath);
        if (!file.exists()) {
            plugin.saveResource(filePath, true);
            save = true;
        }
        if (config == null)
            config = YamlConfiguration.loadConfiguration(file);
        else
            config.load(file);
        if (save) {
            for (ConfigDataObject dataObject : data)
                config.set(dataObject.path, dataObject.defaultValue);
            config.save(file);
        }
    }

    public void reload() throws IOException, InvalidConfigurationException {
        reloadConfiguration();
        boolean save = false;
        for (ConfigDataObject configDataObject : data) {
            configDataObject.value = config.get(configDataObject.path);
            if (configDataObject.value == null) {
                config.set(configDataObject.path, configDataObject.defaultValue);
                if (configDataObject.comment != null) {
                    config.setComments(configDataObject.path, List.of(configDataObject.comment));
                }
                save = true;
            }
        }
        if (save)
            config.save(file);
    }

    public void registerDataField(ConfigDataObject configDataObject) {
        if (data
                .stream()
                .findFirst()
                .filter(configDataObject1 -> configDataObject1.path.equals(configDataObject.path))
                .orElse(null) != null)
            throw new IllegalArgumentException("This path is already registered");
        data.add(configDataObject);
    }

    public void registerDataFields(Class aClass) throws IllegalAccessException {
        for (Field field : aClass.getFields()) {
            if (!(field.get(null) instanceof ConfigDataObject dataObject)) return;
            registerDataField(dataObject);
        }
    }

}
