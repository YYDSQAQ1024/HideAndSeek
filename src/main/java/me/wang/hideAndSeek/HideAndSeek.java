package me.wang.hideAndSeek;

import me.wang.hideAndSeek.events.*;
import me.wang.hideAndSeek.utils.Metrics;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import me.wang.hideAndSeek.tasks.*;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.*;


public final class HideAndSeek extends JavaPlugin {

    public static Plugin plugin;

    public static List<String> playing = new ArrayList<>();

    public static Map<String,List<Player>> playing_players = new HashMap<>();

    public static Map<String,List<Player>> waiting_players = new HashMap<>();

    public static Map<String,Integer> ready = new HashMap<>();

    public static Map<String,Integer> playing_time = new HashMap<>();

    public static Map<String,List<Player>> seekers = new HashMap<>();

    public static Map<String,List<Player>> spectators = new HashMap<>();

    public static Map<String,List<Player>> hiders = new HashMap<>();

    public static Map<Player, Material> materials = new HashMap<>();

    public static List<Player> death = new ArrayList<>();

    public static HashMap<Player, Location> back = new HashMap<>();

    public static ScoreboardManager manager;

    public static HashMap<String, Scoreboard > scoreboards = new HashMap<>();

    public static HashMap<String, Team > teams = new HashMap<>();

    public static HashMap<Player, Location> locationHashMap = new HashMap<>();

    public static YamlConfiguration setting;

    public static YamlConfiguration lang;

    public static Map<UUID, Location> playerLastLocation = new HashMap<>();
    public static Map<UUID, Long> playerLastMoveTime = new HashMap<>();



    @Override
    public void onEnable() {
        // Plugin startup logic
        Metrics metrics = new Metrics(this, 23221);
        plugin = HideAndSeek.getPlugin(HideAndSeek.class);
        manager = getServer().getScoreboardManager();
        new papi().register();




        File lang_dir = new File(getDataFolder()+"/language");
        if (!lang_dir.exists()){
            lang_dir.mkdir();
        }

        getCommand("hideandsee").setExecutor( new command());
        saveResource("config.yml",false);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        File setting_file = new File(getDataFolder()+"/setting.yml");
        saveResource("setting.yml", false);
        YamlConfiguration set = YamlConfiguration.loadConfiguration(setting_file);
        try {
            set.save(setting_file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setting = set;

        File lang_file = new File(getDataFolder()+"/language/"+setting.getString("lang")+".yml");
        saveResource("language/"+setting.getString("lang")+".yml", false);
        YamlConfiguration language = YamlConfiguration.loadConfiguration(lang_file);
        try {
            language.save(lang_file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lang = language;
        Bukkit.getPluginManager().registerEvents(new Join(),this);
        Bukkit.getPluginManager().registerEvents(new editmap(),this);
        Bukkit.getPluginManager().registerEvents(new JoinMap(),this);
        Bukkit.getPluginManager().registerEvents(new leave(),this);
        Bukkit.getPluginManager().registerEvents(new turntoblock(),this);
        Bukkit.getPluginManager().registerEvents(new attack(),this);
        Bukkit.getPluginManager().registerEvents(new protect(),this);
        Bukkit.getPluginManager().registerEvents(new GameSelect(),this);


        BukkitTask time = new time().runTaskTimer(this,0,20L);
        BukkitTask score = new scoreboard().runTaskTimer(this,0,20L);
        BukkitTask end = new end().runTaskTimer(this,0,20L);
        BukkitTask play = new playing().runTaskTimer(this,0,10L);
        BukkitTask teleport = new teleport().runTaskTimer(this,0,1L);
        BukkitTask turnblock = new turntoblock().runTaskTimer(this,0,20L);


        File players = new File(getDataFolder()+"/players");
        if (!players.exists()){
            players.mkdir();
        }
        File maps = new File(getDataFolder()+"/maps");
        if (!maps.exists()){
            maps.mkdir();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
