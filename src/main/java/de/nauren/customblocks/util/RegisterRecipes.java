package de.nauren.customblocks.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class RegisterRecipes {

    public static void RegisterStonecutterRecipes(FileManager fileManager, JavaPlugin plugin) {
        FileConfiguration blockNamesConfig = fileManager.getCustomConfig("blockNames.yml");

        // Iteriere über alle Materialien in der blockNames.yml
        Set<String> materialKeys = blockNamesConfig.getKeys(false);
        for (String materialKey : materialKeys) {
            Material material = Material.matchMaterial(materialKey);
            if (material != null) {
                ConfigurationSection materialSection = blockNamesConfig.getConfigurationSection(materialKey);
                if (materialSection != null) {
                    // Iteriere über alle Custom Model Data Einträge für das Material
                    Set<String> customModelDataKeys = materialSection.getKeys(false);

                    // Erlaube wieder das "default" item zu machen
                    NamespacedKey keyBackToOriginal = new NamespacedKey(plugin, material.name().toLowerCase());
                    StonecuttingRecipe craftBackToOriginal = new StonecuttingRecipe(keyBackToOriginal, new ItemStack(material), material);
                    plugin.getServer().addRecipe(craftBackToOriginal);

                    for (String customModelDataKey : customModelDataKeys) {
                        int customModelData = Integer.parseInt(customModelDataKey);
                        String itemName = fileManager.getItemName(customModelData, material);

                        // Erstelle den ItemStack für das Rezept
                        ItemStack resultItem = new ItemStack(material);
                        ItemMeta itemMeta = resultItem.getItemMeta();
                        assert itemMeta != null;
                        itemMeta.setCustomModelData(customModelData);
                        itemMeta.setDisplayName(itemName);
                        resultItem.setItemMeta(itemMeta);

                        // Erstelle einen neuen Schlüssel für das Rezept
                        NamespacedKey key = new NamespacedKey(plugin, material.name().toLowerCase() + "_" + customModelData);

                        // Erstelle das Stonecutting-Rezept
                        StonecuttingRecipe recipe = new StonecuttingRecipe(key, resultItem, material);
                        plugin.getServer().addRecipe(recipe);

                    }
                }
            }
        }
        plugin.getLogger().info("Stonecutter recipes successfully added");
    }
}
