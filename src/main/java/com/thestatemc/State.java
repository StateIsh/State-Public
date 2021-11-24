package com.thestatemc;

import com.thestatemc.commands.state.StateCommand;
import com.thestatemc.util.Database;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class State extends JavaPlugin {
    private static State plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        initializeDatabase();
    }

    private void initializeDatabase() {
        FileConfiguration config = getConfig();
        Database.asyncHandler = r -> Bukkit.getScheduler().runTaskAsynchronously(this, r);
        Database.HOST = config.getString("database.host");
        Database.PORT = config.getString("database.port");
        Database.DATABASE = config.getString("database.database");
        Database.USERNAME = config.getString("database.username");
        Database.PASSWORD = config.getString("database.password");
    }

    public static State getPlugin() {
        return plugin;
    }
}
