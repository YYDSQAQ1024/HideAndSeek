package me.wang.hideAndSeek;

import me.wang.hideAndSeek.utils.GameSelecter;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.LicenseManager.buildLicense;
import static me.wang.hideAndSeek.utils.config.save;
import static me.wang.hideAndSeek.utils.create.open;
import static me.wang.hideAndSeek.utils.language.getLang;
import static me.wang.hideAndSeek.utils.scoreboard.a;

public class command implements CommandExecutor, TabExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!command.getName().equalsIgnoreCase("hideandsee")){
            return false;
        }
        if (strings.length == 0){
            commandSender.sendMessage("麻烦自己去论坛看帮助");
            return true;
        }
        if (!(commandSender instanceof Player)){
            if (strings[0].equalsIgnoreCase("active")){
                if (strings.length > 3){
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        plugin.getLogger().info(lang.getString("command.active.verify"));
                        if(a(strings[1],strings[2],strings[3]).equalsIgnoreCase("Success")){
                            buildLicense(strings[1]);
                        }else {
                            plugin.getLogger().severe(lang.getString("command.active.filed"));
                        }
                    });

                }
            }else {
                commandSender.sendMessage(lang.getString("command.active.console"));
            }

            return false;
        }
        Player player = (Player) commandSender;
        if (strings[0].equalsIgnoreCase("lobby")){
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }

        if (strings[0].equalsIgnoreCase("join")){
            if (strings.length < 2){
                GameSelecter.open(player);
                return true;
            }

            File file = new File(plugin.getDataFolder()+"/maps/"+strings[1]+".yml");
            if (!file.exists()){
                player.sendMessage(getLang("command.common.FileNotExist"));
                return true;
            }



            World world = Bukkit.getWorld(strings[1]);
            if (world==null){
                player.sendMessage(getLang("command.common.WorldNotExist"));
                return true;
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (!config.getBoolean("enable")){
                player.sendMessage(getLang("command.join.disable"));
                return true;
            }
            if (playing.contains(strings[1])){
                player.sendMessage(getLang("command.join.playing"));
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(world.getSpawnLocation());
                playing_players.get(strings[1]).add(player);
                return true;
            }
            if (world.getPlayers().size() >= config.getInt("total")){
                player.sendMessage(getLang("command.join.full"));
                return true;
            }
            player.teleport(world.getSpawnLocation());
        }

        if (strings[0].equalsIgnoreCase("admin") && player.hasPermission("has.cmd.admin")){
            if (strings[1].equalsIgnoreCase("setHider")){
                World world = player.getWorld();
                File file = new File(plugin.getDataFolder()+"/maps/"+world.getName()+".yml");
                if (!file.exists()) {
                    player.sendMessage(getLang("command.common.FileNotExist"));
                    return true;
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("loc.hide",player.getLocation());
                save(config,file);
                player.sendMessage(getLang("command.common.Success"));
            }
            if (strings[1].equalsIgnoreCase("setWait")){
                World world = player.getWorld();
                File file = new File(plugin.getDataFolder()+"/maps/"+world.getName()+".yml");
                if (!file.exists()) {
                    player.sendMessage(getLang("command.common.FileNotExist"));
                    return true;
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("loc.wait",player.getLocation());
                save(config,file);
                player.sendMessage(getLang("command.common.Success"));
            }
            if (strings[1].equalsIgnoreCase("setSeeker")){
                World world = player.getWorld();
                File file = new File(plugin.getDataFolder()+"/maps/"+world.getName()+".yml");
                if (!file.exists()) {
                    player.sendMessage(getLang("command.common.FileNotExist"));
                    return true;
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("loc.see",player.getLocation());
                save(config,file);
                player.sendMessage(getLang("command.common.Success"));
            }
            if (strings[1].equalsIgnoreCase("setLobby")){
                File file = new File(plugin.getDataFolder()+"/setting.yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("lobby",player.getLocation());
                save(config,file);
                player.sendMessage(getLang("command.common.Success"));
            }
            if (strings[1].equalsIgnoreCase("edit")) {
                World world = Bukkit.getWorld(strings[2]);
                if (world == null) {
                    player.sendMessage(getLang("command.common.WorldNotExist"));
                    return true;
                }
                player.teleport(world.getSpawnLocation());
                File file = new File(plugin.getDataFolder() + "/maps/" + strings[2] + ".yml");
                if (!file.exists()){
                    player.sendMessage(getLang("command.edit.NotExist"));
                }
                open(player);
            }
            if (strings[1].equalsIgnoreCase("create")){
                World world = Bukkit.getWorld(strings[2]);
                if (world==null){
                    player.sendMessage(getLang("command.common.WorldNotExist"));
                    return true;
                }
                player.teleport(world.getSpawnLocation());
                File file = new File(plugin.getDataFolder()+"/maps/"+strings[2]+".yml");
                if (file.exists()){
                    player.sendMessage(getLang("command.create.exist"));
                    return true;
                }else {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("enable",false);
                config.set("total",0);
                config.set("see",0);
                config.set("hide",0);
                config.set("time",5);
                config.set("tc",30);
                List<ItemStack> stackList = new ArrayList<>(Arrays.asList(
                        new ItemStack(Material.GRASS_BLOCK),
                        new ItemStack(Material.OAK_PLANKS),
                        new ItemStack(Material.STONE)
                ));
                config.set("blocks",stackList);
                save(config,file);
                open(player);

            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
