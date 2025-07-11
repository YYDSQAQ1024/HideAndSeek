package me.wang.hideAndSeek;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;


import static me.wang.hideAndSeek.HideAndSeek.plugin;

public class papi extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "HideAndSee";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("version")){
            return plugin.getDescription().getVersion();
        }



        return null;
    }
}
