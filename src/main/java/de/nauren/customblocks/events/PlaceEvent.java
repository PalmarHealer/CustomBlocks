package de.nauren.customblocks.events;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PlaceEvent implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            if (meta.hasCustomModelData()) {

                // Logik für das Spawnen eines ItemDisplay in der Mitte des Blocks
                Location blockLocation = event.getBlock().getLocation();
                Location itemDisplayLocation = blockLocation.add(0.5, 0.5, 0.5);

                // Item Stack vorbereiten
                ItemStack itemStack = item.clone();
                itemStack.setAmount(1);


                ItemDisplay itemDisplay = event.getPlayer().getWorld().spawn(itemDisplayLocation, ItemDisplay.class);

                itemDisplay.setItemStack(itemStack);
                itemDisplay.setBrightness(new Display.Brightness(0, 10));
                itemDisplay.setInvulnerable(true);
                itemDisplay.setSilent(true);
                itemDisplay.setShadowStrength(0);
                itemDisplay.setShadowRadius(0);
                itemDisplay.setTransformation(new Transformation(
                        new Vector3f(0.0f, 0.0f, 0.0f),  // Translation
                        new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f),  // Left Rotation
                        new Vector3f(1.002f, 1.002f, 1.002f),  // Scale
                        new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f)   // Right Rotation
                ));
                itemDisplay.setDisplayHeight(0.5f);
            }
        }
    }
}