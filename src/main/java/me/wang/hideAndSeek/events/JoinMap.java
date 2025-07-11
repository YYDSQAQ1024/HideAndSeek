package me.wang.hideAndSeek.events;

import net.kyori.adventure.platform.facet.Facet;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.language.getLang;
import static me.wang.hideAndSeek.utils.scoreboard.init;

public class JoinMap implements Listener {
    @EventHandler
    public void joinWaitingMap(PlayerChangedWorldEvent e){
        Player player = e.getPlayer();
        player.setInvisible(false);
        File dir = new File(plugin.getDataFolder()+"/maps");
        File[] files = dir.listFiles();
        List<String> list = new ArrayList<>();
        for (File file:files){
            if (!file.isDirectory()){
                list.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
            }
        }
        if (!list.contains(player.getWorld().getName())){
            return;
        }
        World world = player.getWorld();
        File file = new File(plugin.getDataFolder()+"/maps/"+world.getName()+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.getBoolean("enable")){
            return;
        }
        if (playing.contains(world.getName())){
            return;
        }
        if (!waiting_players.containsKey(world.getName())){
            waiting_players.put(world.getName(),new ArrayList<>(Arrays.asList(player)));
        }else {
            List<Player> players = waiting_players.get(world.getName());
            players.add(player);
            waiting_players.put(world.getName(),players);
        }
        player.teleport(config.getLocation("loc.wait"));
        List<Player> players = waiting_players.get(world.getName());

        world.setDifficulty(Difficulty.PEACEFUL);

        for (Player player1:players){
            player1.sendTitle(getLang("join.msg").replace("{0}",player.getDisplayName()),"",10,40,10);
        }
        int min = config.getInt("see")+config.getInt("hide");
        int max = config.getInt("total");

        if (players.size() >= min){
            if (!ready.containsKey(world.getName())){
                if (run){
                    ready.put(world.getName(),setting.getInt("wait.less"));
                }else {
                    ready.put(world.getName(),60);
                }

            }
        }
        if (players.size() >= max){
            if (run){
                ready.put(world.getName(),setting.getInt("wait.full"));
            }else {
                ready.put(world.getName(),10);
            }

        }
        if (!scoreboards.containsKey(world.getName())){
            Scoreboard scoreboard = init(world);
            Team team = scoreboard.registerNewTeam("Collidable");
            team.setPrefix(ChatColor.GRAY+"["+ ChatColor.YELLOW+world.getName()+ChatColor.GRAY+"]"+ChatColor.RESET);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            team.setCanSeeFriendlyInvisibles(false);
            teams.put(world.getName(),team);
            scoreboards.put(world.getName(),scoreboard);
        }
        Scoreboard scoreboard = scoreboards.get(world.getName());
        Team team = teams.get(world.getName());
        for (Player player1:world.getPlayers()){
            player1.setScoreboard(scoreboard);
            team.addEntry(player1.getName());
        }
    }

    @EventHandler
    public void joinWorld(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.setInvisible(false);
        File dir = new File(plugin.getDataFolder()+"/maps");
        File[] files = dir.listFiles();
        List<String> list = new ArrayList<>();
        for (File file:files){
            if (!file.isDirectory()){
                list.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
            }
        }
        if (!list.contains(player.getWorld().getName())){
            return;
        }
        World world = player.getWorld();
        File file = new File(plugin.getDataFolder()+"/maps/"+world.getName()+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.getBoolean("enable")){
            return;
        }
        if (playing.contains(world.getName())){
            return;
        }
        if (!waiting_players.containsKey(world.getName())){
            waiting_players.put(world.getName(),new ArrayList<>(Arrays.asList(player)));
        }else {
            List<Player> players = waiting_players.get(world.getName());
            players.add(player);
            waiting_players.put(world.getName(),players);
        }
        player.teleport(config.getLocation("loc.wait"));
        List<Player> players = waiting_players.get(world.getName());

        world.setDifficulty(Difficulty.PEACEFUL);

        for (Player player1:players){
            player1.sendTitle(getLang("join.msg").replace("{0}",player.getDisplayName()),"",10,40,10);
        }
        int min = config.getInt("see")+config.getInt("hide");
        int max = config.getInt("total");

        if (players.size() >= min){
            if (!ready.containsKey(world.getName())){
                if (run){
                    ready.put(world.getName(),setting.getInt("wait.less"));
                }else {
                    ready.put(world.getName(),60);
                }

            }
        }
        if (players.size() >= max){
            if (run){
                ready.put(world.getName(),setting.getInt("wait.full"));
            }else {
                ready.put(world.getName(),10);
            }

        }
        if (!scoreboards.containsKey(world.getName())){
            Scoreboard scoreboard = init(world);
            Team team = scoreboard.registerNewTeam("Collidable");
            team.setPrefix(ChatColor.GRAY+"["+ ChatColor.YELLOW+world.getName()+ChatColor.GRAY+"]"+ChatColor.RESET);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            team.setCanSeeFriendlyInvisibles(false);
            teams.put(world.getName(),team);
            scoreboards.put(world.getName(),scoreboard);
        }
        Scoreboard scoreboard = scoreboards.get(world.getName());
        Team team = teams.get(world.getName());
        for (Player player1:world.getPlayers()){
            player1.setScoreboard(scoreboard);
            team.addEntry(player1.getName());
        }
    }
}
