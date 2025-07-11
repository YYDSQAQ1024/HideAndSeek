package me.wang.hideAndSeek.events;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.List;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.fallingblock.removePlayerFallingBlock;
import static me.wang.hideAndSeek.utils.language.getLang;

public class leave implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        File file = new File(plugin.getDataFolder()+"/maps/"+entity.getWorld().getName()+".yml");
        if (!file.exists()){
            return;
        }
        if (entity instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void leave(PlayerChangedWorldEvent e){
        Player player = e.getPlayer();
        File file = new File(plugin.getDataFolder()+"/maps/"+e.getFrom().getName()+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        int min = config.getInt("see")+config.getInt("hide");
        if (file.exists()){
            if (playing_players.containsKey(e.getFrom().getName())){
                List<Player> players = playing_players.get(e.getFrom().getName());
                players.remove(player);
                playing_players.put(e.getFrom().getName(),players);
            }
            if (waiting_players.containsKey(e.getFrom().getName())){
                List<Player> players = waiting_players.get(e.getFrom().getName());
                players.remove(player);
                waiting_players.put(e.getFrom().getName(),players);
                if (players.size() < min){
                    ready.remove(e.getFrom().getName());
                }
            }
            if (seekers.containsKey(e.getFrom().getName())){
                if (seekers.get(e.getFrom().getName()).contains(player)){
                    List<Player> players = seekers.get(e.getFrom().getName());
                    players.remove(player);
                    seekers.put(e.getFrom().getName(),players);
                }
            }
            if (hiders.containsKey(e.getFrom().getName())){
                if (hiders.get(e.getFrom().getName()).contains(player)){
                    List<Player> players = hiders.get(e.getFrom().getName());
                    players.remove(player);
                    hiders.put(e.getFrom().getName(),players);
                }
            }
            removePlayerFallingBlock(player);
            for (Player player1:e.getFrom().getPlayers()){
                player1.sendMessage(getLang("leave.quit").replace("{0}",player.getDisplayName()));
            }
            Scoreboard scoreboard = manager.getNewScoreboard();
            player.setScoreboard(scoreboard);
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        File file = new File(plugin.getDataFolder()+"/maps/"+player.getWorld().getName()+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        int min = config.getInt("see")+config.getInt("hide");
        if (file.exists()){
            if (playing_players.containsKey(player.getWorld().getName())){
                List<Player> players = playing_players.get(player.getWorld().getName());
                players.remove(player);
                playing_players.put(player.getWorld().getName(),players);
            }
            if (waiting_players.containsKey(player.getWorld().getName())){
                List<Player> players = waiting_players.get(player.getWorld().getName());
                players.remove(player);
                waiting_players.put(player.getWorld().getName(),players);
                if (players.size() < min){
                    ready.remove(player.getWorld().getName());
                }
            }
            if (seekers.containsKey(player.getWorld().getName())){
                if (seekers.get(player.getWorld().getName()).contains(player)){
                    List<Player> players = seekers.get(player.getWorld().getName());
                    players.remove(player);
                    seekers.put(player.getWorld().getName(),players);
                }
            }
            if (hiders.containsKey(player.getWorld().getName())){
                if (hiders.get(player.getWorld().getName()).contains(player)){
                    List<Player> players = hiders.get(player.getWorld().getName());
                    players.remove(player);
                    hiders.put(player.getWorld().getName(),players);
                }
            }
            removePlayerFallingBlock(player);
            for (Player player1:player.getWorld().getPlayers()){
                player1.sendMessage(getLang("leave.quit").replace("{0}",player.getDisplayName()));
            }
            Scoreboard scoreboard = manager.getNewScoreboard();
            player.setScoreboard(scoreboard);
        }
        Scoreboard scoreboard = manager.getNewScoreboard();
        player.setScoreboard(scoreboard);
    }

    @EventHandler
    public void die(PlayerDeathEvent e){
        Player player = e.getEntity();
        File file = new File(plugin.getDataFolder()+"/maps/"+player.getWorld().getName()+".yml");
        if (!file.exists()){
            return;
        }
        e.setKeepInventory(true);
        player.getInventory().clear();
        World world = player.getWorld();
        player.sendMessage(getLang("leave.die"));
        removePlayerFallingBlock(player);
        if (playing_players.containsKey(world.getName())){
            List<Player> players = playing_players.get(world.getName());
            players.remove(player);
            playing_players.put(world.getName(),players);
        }
        if (waiting_players.containsKey(world.getName())){
            List<Player> players = waiting_players.get(world.getName());
            players.remove(player);
            waiting_players.put(world.getName(),players);
        }
        if (seekers.containsKey(world.getName())){
            List<Player> players = seekers.get(world.getName());
            players.remove(player);
            seekers.put(world.getName(),players);
        }
        if (hiders.containsKey(world.getName())){
            List<Player> players = hiders.get(world.getName());
            players.remove(player);
            hiders.put(world.getName(),players);
        }
        death.add(player);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (setting.getBoolean("game.ResurrectionAsSeeker")){
            back.put(player,config.getLocation("loc.hide"));
        }else {
            back.put(player,player.getLocation());
        }

        player.setInvisible(false);
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        if (!death.contains(player)){
            return;
        }
        if (!back.containsKey(player)){
            return;
        }
        File file = new File(plugin.getDataFolder()+"/maps/"+player.getWorld().getName()+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        player.setInvisible(false);
        e.setRespawnLocation(back.get(player));
        back.remove(player);
        death.remove(player);
        if (setting.getBoolean("game.ResurrectionAsSeeker")){
            player.sendTitle(getLang("leave.die"),getLang("leave.ResurrectionAsSeeker"));
            player.getInventory().clear();
            ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
            axe.addEnchantment(Enchantment.getByKey(NamespacedKey.minecraft("sharpness")),3);
            player.getInventory().addItem(axe);
            player.getInventory().addItem(new ItemStack(Material.BOW));
            player.getInventory().addItem(new ItemStack(Material.ARROW,64));

            seekers.get(player.getWorld().getName()).add(player);
        }else {
            player.sendTitle(getLang("leave.die"),"");
            player.setGameMode(GameMode.SPECTATOR);
        }


    }
}
