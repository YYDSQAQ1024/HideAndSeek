package me.wang.hideAndSeek.tasks;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.fallingblock.followPlayerWithBlockDisplay;


public class teleport extends BukkitRunnable {
    @Override
    public void run() {
        for (Map.Entry entry:hiders.entrySet()){
            List<Player> players = (List<Player>) entry.getValue();
            for (Player player:players){
                if (locationHashMap.containsKey(player)){
                    player.sendBlockChange(player.getLocation().getBlock().getLocation(), Material.AIR.createBlockData());
                    if (player.getGameMode() == GameMode.SPECTATOR){
                        player.setGameMode(GameMode.SURVIVAL);
                        player.setCollidable(false);
                    }
                    if (player.hasMetadata("floatingBlock")) {
                        BlockDisplay blockDisplay = (BlockDisplay) player.getMetadata("floatingBlock").get(0).value();
                        blockDisplay.remove();
                        player.removeMetadata("floatingBlock", plugin);
                    }
                    continue;
                }
                followPlayerWithBlockDisplay(player);
            }
        }

    }

}
