package me.wang.hideAndSeek.events;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Map;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.language.getLang;

public class attack implements Listener {
    @EventHandler
    public void attack(PlayerInteractEvent e){
        Player player = e.getPlayer();

        File file = new File(plugin.getDataFolder()+"/maps/"+player.getWorld().getName()+".yml");
        if (!file.exists()){
            return;
        }
        if (e.getClickedBlock() == null){
            return;
        }
        if (!playing.contains(player.getWorld().getName())){
            return;
        }
        if (hiders.get(player.getWorld().getName()).contains(player)){
            return;
        }
        for (Map.Entry<Player, Location> fallingBlock:locationHashMap.entrySet()){
            if (fallingBlock.getValue().equals(e.getClickedBlock().getLocation())){
                fallingBlock.getValue().getBlock().setType(Material.AIR);
                Vector knockback = player.getLocation().getDirection().multiply(0).setY(0.5);
                fallingBlock.getKey().setVelocity(knockback);
                fallingBlock.getKey().playSound(fallingBlock.getKey(), Sound.BLOCK_ANVIL_USE,1.0f,1.0f);
                fallingBlock.getKey().setGameMode(GameMode.SURVIVAL);
                locationHashMap.remove(fallingBlock.getKey());
                player.attack(fallingBlock.getKey());
                player.sendMessage(getLang("attack.msg"));
                return;
            }
        }


    }



}
