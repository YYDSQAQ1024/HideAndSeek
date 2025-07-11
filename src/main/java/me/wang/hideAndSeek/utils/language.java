package me.wang.hideAndSeek.utils;

import org.bukkit.ChatColor;

import static me.wang.hideAndSeek.HideAndSeek.lang;

public class language {
    public static String getLang(String path){
        return ChatColor.translateAlternateColorCodes('&', lang.getString(path));
    }
}
