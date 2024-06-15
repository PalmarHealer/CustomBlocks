package de.nauren.customblocks.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final FileManager fileManager;

    public CommandManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    //Config reload command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("reload")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("customblocks.admin")) {
                    fileManager.reloadConfig("config.yml");
                    fileManager.reloadConfig("blockNames.yml");
                    player.sendMessage("Configuration reloaded successfully!");
                } else {
                    player.sendMessage("§cNo permission.");
                }
            } else {
                fileManager.reloadConfig("config.yml");
                fileManager.reloadConfig("blockNames.yml");
                sender.sendMessage("Configuration reloaded successfully!");
            }
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("load") && args[1].equalsIgnoreCase("texturepack")) {
            if (sender instanceof Player) {

                Player player = (Player) sender;
                // Erstelle die Nachricht
                TextComponent message = new TextComponent("§7Wenn du CustomBlocks sehen willst dann oder lade dir ");

                // Klickbarer Text für den manuellen Download
                TextComponent manualDownload = new TextComponent("§nhier das Pack manuell runter.");
                manualDownload.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, fileManager.getDownloadLink()));
                message.addExtra(manualDownload);

                // Sende die Nachricht an den Spieler
                player.spigot().sendMessage(message);

            } else {
                sender.sendMessage("This command can only be run by a player.");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("config".startsWith(args[0].toLowerCase())) {
                completions.add("config");
            } else if ("load".startsWith(args[0].toLowerCase())) {
                completions.add("load");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("config")) {
            if ("reload".startsWith(args[1].toLowerCase())) {
                completions.add("reload");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("load")) {
            if ("texturepack".startsWith(args[1].toLowerCase())) {
                completions.add("texturepack");
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
