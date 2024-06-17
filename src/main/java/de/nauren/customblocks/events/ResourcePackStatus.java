package de.nauren.customblocks.events;

import de.nauren.customblocks.util.FileManager;
import org.bukkit.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;


public class ResourcePackStatus implements Listener {

    private final FileManager fileManager;
    private final HashMap<UUID, Boolean> resourcePackLoading = new HashMap<>();

    public ResourcePackStatus(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        sendResourcePackRequest(player);
    }

    public void sendResourcePackRequest(Player player) {
        String url = fileManager.getDownloadLink();
        resourcePackLoading.put(player.getUniqueId(), true);
        player.setResourcePack(url);
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        PlayerResourcePackStatusEvent.Status status = event.getStatus();

        switch (status) {
            case SUCCESSFULLY_LOADED:
                resourcePackLoading.put(player.getUniqueId(), false);
                player.sendMessage("§7Resource Pack successfully loaded!");

                Bukkit.getScheduler().runTaskLater((Plugin) this, () -> player.setGameMode(GameMode.SURVIVAL), 20L);
                break;
            case DECLINED:
                resourcePackLoading.put(player.getUniqueId(), false);
                break;
            case FAILED_DOWNLOAD:
                resourcePackLoading.put(player.getUniqueId(), false);
                player.sendMessage("§7Failed to download Resource Packs.");

                Bukkit.getScheduler().runTaskLater((Plugin) this, () -> player.setGameMode(GameMode.SURVIVAL), 20L);
                break;
            case ACCEPTED:
                player.setGameMode(GameMode.SPECTATOR);
                resourcePackLoading.put(player.getUniqueId(), true);
                break;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (resourcePackLoading.getOrDefault(player.getUniqueId(), false)) {
            event.setTo(event.getFrom());
        }
    }
}
