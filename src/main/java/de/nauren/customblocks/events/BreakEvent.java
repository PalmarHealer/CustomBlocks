package de.nauren.customblocks.events;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import de.nauren.customblocks.util.FileManager;

public class BreakEvent implements Listener {
    private final FileManager fileManager;

    public BreakEvent(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        handleBlockBreak(event.getBlock(), event.getPlayer().getGameMode(), event);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        handleBlockBreak(event.getBlock(), null, null);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            handleBlockBreak(block, null, null);
        }
    }

    private void handleBlockBreak(Block block, GameMode gameMode, BlockBreakEvent event) {
        Location blockLocation = block.getLocation().add(0.5, 0.5, 0.5);

        for (Entity entity : block.getChunk().getEntities()) {
            if (entity instanceof ItemDisplay) {
                ItemDisplay itemDisplay = (ItemDisplay) entity;
                Location itemDisplayLocation = itemDisplay.getLocation();

                itemDisplayLocation.setYaw(blockLocation.getYaw());
                itemDisplayLocation.setPitch(blockLocation.getPitch());

                if (blockLocation.equals(itemDisplayLocation)) {
                    ItemStack displayedItem = itemDisplay.getItemStack();
                    assert displayedItem != null;
                    ItemMeta meta = displayedItem.getItemMeta();
                    assert meta != null;

                    String itemName = fileManager.getItemName(meta.getCustomModelData(), displayedItem.getType());

                    meta.setDisplayName(itemName);
                    displayedItem.setItemMeta(meta);

                    if (gameMode == GameMode.SURVIVAL &&
                            event.isDropItems() &&
                            block.getType().equals(displayedItem.getType())) {

                        // Drop the custom item
                        block.getWorld().dropItem(blockLocation, displayedItem);
                        event.setDropItems(false);

                    }

                    itemDisplay.remove();
                    block.setType(Material.AIR);
                }
            }
        }
    }
}
