package me.wang.hideAndSeek.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

import static me.wang.hideAndSeek.HideAndSeek.plugin;
import static me.wang.hideAndSeek.utils.language.getLang;

public class GameSelect implements Listener {

    @EventHandler
    public void select(InventoryClickEvent e){
        if (!e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.AQUA+"加入游戏")){
            return;
        }
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ItemStack itemStack = e.getInventory().getItem(e.getSlot());
        String name = itemStack.getItemMeta().getDisplayName();
        File file = new File(plugin.getDataFolder()+"/maps/"+name+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.getBoolean("enable")){
            player.sendMessage(getLang("command.join.full"));
            return;
        }
        if (Bukkit.getWorld(name).getPlayers().size() >= config.getInt("total")){
            player.sendMessage(getLang("command.join.full"));
            return;
        }
        player.closeInventory();
        World world = Bukkit.getWorld(name);
        player.teleport(world.getSpawnLocation());
    }
}
