package me.wang.hideAndSeek.tasks;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.*;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.fallingblock.followPlayerWithBlockDisplay;
import static me.wang.hideAndSeek.utils.language.getLang;

public class time extends BukkitRunnable {
    @Override
    public void run() {
        for (Map.Entry entry:ready.entrySet()){
            int t = (int) entry.getValue();
            ready.put((String) entry.getKey(),t);

            if (t < 6 && t != 0){
                List<Player> players = waiting_players.get(entry.getKey());
                for (Player player:players){
                    player.playSound(player,Sound.UI_BUTTON_CLICK,1.0f,1.0f);
                    player.sendTitle(ChatColor.YELLOW+String.valueOf(t),"",10,20,10);
                }
            }
            if (t <= 0){
                String s = (String) entry.getKey();
                File file = new File(plugin.getDataFolder()+"/maps/"+s+".yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                ready.remove(entry.getKey());
                playing.add(s);
                playing_players.put(s,waiting_players.get(s));
                waiting_players.remove(s);
                List<Player> players = playing_players.get(s);
                for (Player player:players){
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    player.sendTitle(getLang("wait.start"),"",10,40,10);
                }
                World world = Bukkit.getWorld(s);
                world.setTime(1000);
                world.setStorm(false);
                world.setThundering(false);
                world.setWeatherDuration(0);
                world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
                world.setGameRule(GameRule.DO_WEATHER_CYCLE,false);

                playing_time.put((String) entry.getKey(),config.getInt("time")*60);

                List<Player> see = createRandoms(players,config.getInt("see"));
                List<ItemStack> blocks = (List<ItemStack>) config.get("blocks");

                List<Player> seeker = new ArrayList<>();
                List<Player> hider = new ArrayList<>();

                for (Player player:players){
                    player.getInventory().clear();
                    if (see.contains(player)){
                        player.teleport(config.getLocation("loc.see"));
                        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
                        axe.addEnchantment(Enchantment.getByKey(NamespacedKey.minecraft("sharpness")),3);
                        player.getInventory().addItem(axe);
                        player.getInventory().addItem(new ItemStack(Material.BOW));
                        player.getInventory().addItem(new ItemStack(Material.ARROW,64));

                        seeker.add(player);
                        continue;
                    }
                    if (setting.getBoolean("game.GiveHiderArms")){
                        player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
                    }
                    player.teleport(config.getLocation("loc.hide"));
                    followPlayerWithBlockDisplay(player);
                    hider.add(player);
                }

                seekers.put(s,seeker);
                hiders.put(s,hider);

                for (Player player1:hider){
                    player1.setInvisible(true);
                }

                for (Player player:hider){
                    Random random = new Random();
                    ItemStack block = blocks.get(random.nextInt(blocks.size()));
                    materials.put(player,block.getType());

                }
                /*
                Scoreboard scoreboard = scoreboards.get(world.getName());
                Objective obj = scoreboard.getObjective(world.getName());

                Score k7 = obj.getScore(ChatColor.GREEN+"");
                k7.setScore(7);

                Score r = obj.getScore(ChatColor.AQUA+"躲藏者："+hider.size());
                r.setScore(6);

                Score k5 = obj.getScore(ChatColor.GREEN+"");
                k5.setScore(5);

                Score c = obj.getScore(ChatColor.AQUA+"寻找者："+seeker.size());
                c.setScore(4);

                 */

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!playing.contains(s)){
                            cancel();
                            return;
                        }
                        if (!seekers.containsKey(s)){
                            cancel();
                            return;
                        }
                        if (seekers.get(s) == null){
                            cancel();
                            return;
                        }
                        for (Player player:see){
                            if (!seekers.get(s).contains(player)){
                                continue;
                            }
                            player.teleport(config.getLocation("loc.hide"));
                        }
                        for (Player player:players){
                            player.sendMessage(getLang("wait.seeker"));
                        }
                    }
                }.runTaskLater(plugin, config.getInt("tc")*20);
            }
        }
    }

    private List createRandoms(List list, int n) {
        Map<Integer,String> map = new HashMap();
        List news = new ArrayList();
        if (list.size() <= n) {
            return list;
        } else {
            while (map.size() < n) {
                int random = (int)(Math.random() * list.size());
                if (!map.containsKey(random)) {
                    map.put(random, "");
                    news.add(list.get(random));
                }
            }
            return news;
        }
    }

}
