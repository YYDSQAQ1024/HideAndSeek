package me.wang.hideAndSeek.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.language.getLang;

public class turntoblock extends BukkitRunnable implements Listener {
    @Override
    public void run() {
        for (List<Player> list:playing_players.values()){
            for (Player player : list) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,1,1));
            }
        }

        for (List<Player> list:hiders.values()){
            long currentTime = System.currentTimeMillis();
            for (Player player : list) {
                UUID playerId = player.getUniqueId();
                Location lastLocation = playerLastLocation.get(playerId);
                long lastMoveTime = playerLastMoveTime.getOrDefault(playerId, currentTime);

                player.getInventory().setItem(8,new ItemStack(materials.get(player)));

                if (lastLocation != null && lastMoveTime + setting.getInt("game.Blocking") <= currentTime) {
                    if (player.getLocation().getBlock().getType() == Material.AIR  && !locationHashMap.containsKey(player)){

                        player.setGameMode(GameMode.SPECTATOR);
                        Block block = player.getLocation().getBlock();
                        block.setType(materials.get(player));

                        if (block.getBlockData() instanceof Directional) {
                            Directional directional = (Directional) block.getBlockData();
                            BlockFace playerFacing = player.getFacing();
                            directional.setFacing(playerFacing);
                            block.setBlockData(directional);
                        }

                        player.sendBlockChange(block.getLocation(),Material.AIR.createBlockData());

                        locationHashMap.put(player,player.getLocation().getBlock().getLocation());

                        player.sendMessage(getLang("block.TurnIntoBlock"));
                    }else {
                        if (!locationHashMap.containsKey(player)){
                            player.sendMessage(getLang("block.exist"));
                        }
                    }
                }
            }
        }


    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            if (locationHashMap.containsKey(player)){
                locationHashMap.get(player).getBlock().setType(Material.AIR);
                locationHashMap.remove(player);
                player.setGameMode(GameMode.SURVIVAL);
                player.setCollidable(true);
                player.sendMessage(getLang("block.restore"));
            }
            playerLastLocation.put(playerId, to);
            playerLastMoveTime.put(playerId, System.currentTimeMillis());
        }
    }

    /*
    嘲讽功能
     */

    @EventHandler
    public void interact(PlayerInteractEvent e){
        File file = new File(plugin.getDataFolder()+"/maps/"+e.getPlayer().getWorld().getName()+".yml");
        if (!file.exists()){
            return;
        }
        if (!playing.contains(e.getPlayer().getWorld().getName())){
            return;
        }
        if (e.getItem() == null){
            return;
        }
        if (e.getItem().getType() != Material.FIREWORK_ROCKET){
            return;
        }
        if (licenseInfo == null){
            e.getPlayer().sendMessage(ChatColor.RED+"抱歉，免费版暂不支持使用嘲讽技能！");
            e.setCancelled(true);
            return;
        }
        String name = materials.get(e.getPlayer()).name().replace("_"," ").toLowerCase(Locale.ROOT);
        for (Player player : seekers.get(e.getPlayer().getWorld().getName())){
            player.sendMessage(ChatColor.YELLOW+String.format("<%s> 有本事来抓我呀！我是%s",e.getPlayer().getDisplayName(),name));
        }
    }
}
