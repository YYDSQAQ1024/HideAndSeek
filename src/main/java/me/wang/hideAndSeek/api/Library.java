package me.wang.hideAndSeek.api;

import org.bukkit.plugin.java.JavaPlugin;

public class Library {
    private static JavaPlugin plugin;


    public static JavaPlugin getPlugin() {
        return plugin;
    }


    public static void setPlugin(final JavaPlugin plugin) {
        Library.plugin = plugin;
    }
}
