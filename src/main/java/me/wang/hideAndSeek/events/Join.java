package me.wang.hideAndSeek.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.List;

import static me.wang.hideAndSeek.HideAndSeek.*;

public class Join implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if (plugin.getConfig().getLocation("lobby") != null){
            player.teleport(plugin.getConfig().getLocation("lobby"));
        }

        if (player.hasPermission("has.plus.join")&&run){
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder()+"/players/"+player.getName()+".yml"));
            List<String> list = config.getStringList("join");
            for (String s:list){
                s = ChatColor.translateAlternateColorCodes('&',s);
                for (Player player1 :Bukkit.getOnlinePlayers()){
                    player1.sendMessage(s);
                }
            }
        }
    }
}
