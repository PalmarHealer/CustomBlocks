package de.nauren.customblocks;

import de.nauren.customblocks.events.*;
import de.nauren.customblocks.util.CommandManager;
import de.nauren.customblocks.util.FileManager;
import de.nauren.customblocks.util.RegisterRecipes;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileManager fileManager = new FileManager(this);

        //Register custom block placement and break functions
        getServer().getPluginManager().registerEvents(new PlaceEvent(), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(fileManager), this);
        getServer().getPluginManager().registerEvents(new PistonMoveEvent(), this);

        //Register resource pack functions
        getServer().getPluginManager().registerEvents(new ResourcePackStatus(fileManager), this);

        //Register recipes
        RegisterRecipes.RegisterStonecutterRecipes(fileManager, this);
        //Register commands
        CommandManager commandManager = new CommandManager(fileManager);
        Objects.requireNonNull(this.getCommand("cb")).setExecutor(commandManager);
        Objects.requireNonNull(this.getCommand("cb")).setTabCompleter(commandManager);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
