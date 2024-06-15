package de.nauren.customblocks.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    private File configYmlFile;
    private FileConfiguration configYml;

    public FileManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createConfigYml();
        createCustomConfig("blockNames.yml");
    }

    private void createConfigYml() {
        configYmlFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configYmlFile.exists()) {
            configYmlFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        configYml = YamlConfiguration.loadConfiguration(configYmlFile);
    }

    private void createCustomConfig(String fileName) {
        File customConfigFile = new File(plugin.getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }

        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        configs.put(fileName, customConfig);
    }

    public FileConfiguration getCustomConfig(String fileName) {
        return configs.get(fileName);
    }

    public void saveCustomConfig(String fileName) {
        File customConfigFile = new File(plugin.getDataFolder(), fileName);
        try {
            if (configs.containsKey(fileName)) {
                configs.get(fileName).save(customConfigFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig(String fileName) {
        File customConfigFile = new File(plugin.getDataFolder(), fileName);
        if (configs.containsKey(fileName)) {
            configs.put(fileName, YamlConfiguration.loadConfiguration(customConfigFile));
        }
    }

    public String getItemName(int customModelData, Material material) {
        String path = material.toString() + "." + customModelData;
        FileConfiguration blockNamesConfig = getCustomConfig("blockNames.yml");
        if (blockNamesConfig.contains(path)) {
            return "§r" + blockNamesConfig.getString(path, "Unknown Item");
        } else {
            blockNamesConfig.set(path, "Unknown Item");
            return "§rUnknown Item";
        }
    }

    public String getDownloadLink() {
        return configYml.getString("downloadLink", "https://example.com/CustomBlocks.zip");
    }

    public void setDownloadLink(String link) {
        configYml.set("downloadLink", link);
        saveConfigYml();
    }

    private void saveConfigYml() {
        try {
            configYml.save(configYmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfigYml() {
        configYml = YamlConfiguration.loadConfiguration(configYmlFile);
    }
}