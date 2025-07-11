package me.wang.hideAndSeek.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.wang.hideAndSeek.HideAndSeek.plugin;

public class GameSelecter {

    public static void open(Player player){
        Inventory inventory = Bukkit.createInventory(null,54, ChatColor.AQUA+"加入游戏");

        File dir = new File(plugin.getDataFolder()+"/maps");
        File[] files = dir.listFiles();

        int i = 0;
        for (File file:files){
            if (!file.isDirectory()){
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                if (!config.getBoolean("enable")){
                    continue;
                }
                if (Bukkit.getWorld(name).getPlayers().size() >= config.getInt("total")){
                    continue;
                }
                int players = Bukkit.getWorld(name).getPlayers().size();
                int max = config.getInt("total");
                ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(name);
                itemMeta.setLore(new ArrayList<>(Arrays.asList(
                        ChatColor.AQUA+"点击加入游戏",
                        ChatColor.YELLOW+"在线人数："+players+"/"+max
                )));
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(i,itemStack);
                i++;
            }
        }

        player.openInventory(inventory);
    }
}
