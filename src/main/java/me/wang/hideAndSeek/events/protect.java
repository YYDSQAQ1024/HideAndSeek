package me.wang.hideAndSeek.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.io.File;

import static me.wang.hideAndSeek.HideAndSeek.playing;
import static me.wang.hideAndSeek.HideAndSeek.plugin;
import static me.wang.hideAndSeek.utils.language.getLang;

public class protect implements Listener {
    @EventHandler
    public void breakblock(BlockBreakEvent e){
        Player player = e.getPlayer();

        File file = new File(plugin.getDataFolder()+"/maps/"+player.getWorld().getName()+".yml");
        if (!file.exists()){
            return;
        }
        if (!playing.contains(player.getWorld().getName())){
            if (!player.isOp()){
                e.setCancelled(true);
                player.sendMessage(getLang("common.BreakBlock"));
            }
            return;
        }
        e.setCancelled(true);
        player.sendMessage(getLang("common.BreakBlock"));
    }

    @EventHandler
    public void drop(PlayerDropItemEvent e){
        Player player = e.getPlayer();

        File file = new File(plugin.getDataFolder()+"/maps/"+player.getWorld().getName()+".yml");
        if (!file.exists()){
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        File file = new File(plugin.getDataFolder()+"/maps/"+e.getBlock().getLocation().getWorld()+".yml");
        if (!file.exists()){
            return;
        }

        Material blockMaterial = e.getBlock().getType();


        if (blockMaterial == Material.SAND || blockMaterial == Material.GRAVEL || blockMaterial == Material.ANVIL) {
            e.setCancelled(true);
        }
    }
}
