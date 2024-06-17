package de.nauren.customblocks.events;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
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
                BlockData blockLocationData = blockLocation.getBlock().getBlockData();
                Location itemDisplayLocation = blockLocation.add(0.5, 0.5, 0.5);

                if (blockLocationData instanceof Directional) {
                    Directional directional = (Directional) blockLocationData;
                    itemDisplayLocation.setYaw(blockFaceToYaw(directional.getFacing()));
                } else {
                    itemDisplayLocation.setYaw(0.0f);
                }
                itemDisplayLocation.setPitch(0.0f);


                // Item Stack vorbereiten
                ItemStack itemStack = item.clone();
                itemStack.setAmount(1);


                ItemDisplay itemDisplay = event.getPlayer().getWorld().spawn(itemDisplayLocation, ItemDisplay.class);

                itemDisplay.setItemStack(itemStack);
                itemDisplay.setBrightness(new Display.Brightness(5, 10));
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
                itemDisplay.teleport(itemDisplayLocation);
                itemDisplay.setDisplayHeight(0.5f);
            }
        }
    }
    private float blockFaceToYaw(BlockFace face) {
        switch (face) {
            case NORTH:
                return 180.0f;
            case EAST:
                return -90.0f;
            case WEST:
                return 90.0f;
            default:
                return 0.0f;
        }
    }
}