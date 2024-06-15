package de.nauren.customblocks.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PistonMoveEvent implements Listener {

    private final Set<UUID> alreadyTeleported = new HashSet<>();

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        alreadyTeleported.clear();
        for (Block block : event.getBlocks()) {
            alreadyTeleported.add(handlePistonMove(block, event.getDirection(), alreadyTeleported));
        }
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        alreadyTeleported.clear();
        for (Block block : event.getBlocks()) {
            alreadyTeleported.add(handlePistonMove(block, event.getDirection(), alreadyTeleported));
        }
    }

    private UUID handlePistonMove(Block block, BlockFace blockFace, Set<UUID> alreadyTeleported) {
        Location blockLocation = block.getLocation().add(0.5, 0.5, 0.5);

        for (Entity entity : block.getChunk().getEntities()) {
            if (entity instanceof ItemDisplay) {
                ItemDisplay itemDisplay = (ItemDisplay) entity;
                UUID uuid = itemDisplay.getUniqueId();
                if (!alreadyTeleported.contains(uuid)) {
                    if (blockLocation.equals(itemDisplay.getLocation())) {
                        Location location = block.getRelative(blockFace).getLocation();
                        itemDisplay.teleport(location.add(0.5, 0.5, 0.5));
                        return uuid;
                    }
                }
            }
        }
        return null;
    }



}
