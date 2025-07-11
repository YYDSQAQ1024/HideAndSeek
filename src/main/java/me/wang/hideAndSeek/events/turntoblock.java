package me.wang.hideAndSeek.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.language.getLang;

public class turntoblock extends BukkitRunnable implements Listener {
    @Override
    public void run() {
        for (List<Player> list:hiders.values()){
            long currentTime = System.currentTimeMillis();
            for (Player player : list) {
                UUID playerId = player.getUniqueId();
                Location lastLocation = playerLastLocation.get(playerId);
                long lastMoveTime = playerLastMoveTime.getOrDefault(playerId, currentTime);

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
}
