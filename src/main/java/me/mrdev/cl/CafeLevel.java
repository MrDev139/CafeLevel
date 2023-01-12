package me.mrdev.cl;

import me.mrdev.cl.commands.CafeCmd;
import me.mrdev.cl.listeners.Test;
import me.mrdev.cl.placeholders.CafeExpansion;
import me.mrdev.cl.profile.ProfileManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class CafeLevel extends JavaPlugin {

    private ProfileManager manager;
    private String prefix;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix"));
        try {
            manager = new ProfileManager(this);
        } catch (SQLException | ClassNotFoundException e) {
           getLogger().warning("Failed to load profiles from database! (check if the database details are correct) \n Disabling the plugin!");
           e.printStackTrace();
           getServer().getPluginManager().disablePlugin(this);
        }
        getServer().getPluginManager().registerEvents(new Test(this), this);
        getCommand("cafexp").setExecutor(new CafeCmd(this));
        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CafeExpansion(this).register();
        }else {
            getLogger().info("Couldn't find PlaceholderAPI! placeholders are not loaded!");
        }
    }

    @Override
    public void onDisable() {

    }

    public String getPrefix() {
        return prefix;
    }

    public ProfileManager getManager() {
        return manager;
    }
}
