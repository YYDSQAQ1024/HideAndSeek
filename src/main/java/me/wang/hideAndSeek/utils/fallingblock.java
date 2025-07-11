package me.wang.hideAndSeek.utils;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import static me.wang.hideAndSeek.HideAndSeek.*;

public class fallingblock {
    public static void followPlayerWithBlockDisplay(Player player) {
        Location playerLocation1 = player.getLocation().clone();


        double playerX = playerLocation1.getX();
        double playerY = playerLocation1.getY();
        double playerZ = playerLocation1.getZ();

        Location playerLocation = new Location(player.getWorld(), playerX - 0.5, playerY, playerZ - 0.5);

        BlockDisplay blockDisplay;
        if (player.hasMetadata("floatingBlock")) {
            blockDisplay = (BlockDisplay) player.getMetadata("floatingBlock").get(0).value();
            if (locationHashMap.containsKey(player)){
                blockDisplay.remove();
                player.removeMetadata("floatingBlock", plugin);
            }else {
                BlockData blockData = blockDisplay.getBlock();
                if (blockData instanceof Directional) {
                    Directional directional = (Directional) blockData;
                    BlockFace playerFacing = player.getFacing();
                    directional.setFacing(playerFacing);
                    blockDisplay.setBlock(blockData);
                }
                blockDisplay.teleport(playerLocation);
            }
        } else {
            if (locationHashMap.containsKey(player)){
                return;
            }
            if (!materials.containsKey(player)){
                return;
            }
            BlockData blockData = materials.get(player).createBlockData();
            if (blockData instanceof Directional) {
                Directional directional = (Directional) blockData;
                BlockFace playerFacing = player.getFacing();
                directional.setFacing(playerFacing);
            }
            blockDisplay = player.getWorld().spawn(playerLocation, BlockDisplay.class);
            blockDisplay.setBlock(blockData);
            blockDisplay.setGravity(false);

            player.setMetadata("floatingBlock", new FixedMetadataValue(plugin, blockDisplay));
        }
    }

    public static void removePlayerFallingBlock(Player player) {
        if (player.hasMetadata("floatingBlock")) {
            BlockDisplay blockDisplay = (BlockDisplay) player.getMetadata("floatingBlock").get(0).value();
            blockDisplay.remove();
            player.removeMetadata("floatingBlock", plugin);
            materials.remove(player);
        }
    }
}
