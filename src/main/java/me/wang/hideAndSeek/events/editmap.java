package me.wang.hideAndSeek.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static me.wang.hideAndSeek.HideAndSeek.plugin;
import static me.wang.hideAndSeek.utils.anvil.set;
import static me.wang.hideAndSeek.utils.config.save;
import static me.wang.hideAndSeek.utils.create.open;
import static me.wang.hideAndSeek.utils.language.getLang;

public class editmap implements Listener {
    @EventHandler
    public void click(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        World world = player.getWorld();
        int slot = e.getSlot();
        if (player.getOpenInventory().getTitle().equalsIgnoreCase(getLang("inventory.title").replace("{0}",world.getName()))){
            File file = new File(plugin.getDataFolder()+"/maps/"+world.getName()+".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (slot > 17 && slot < 45){
                List<ItemStack> stackList = new ArrayList<>();
                for (int i=18;i < 45;i++){
                    ItemStack itemStack = e.getInventory().getItem(i);
                    if (itemStack == null || itemStack.getType() == Material.AIR){
                        continue;
                    }
                    stackList.add(itemStack);
                }
                config.set("blocks",stackList);
                save(config,file);
                return;
            }
            if (slot > 53 || e.getClickedInventory().getType() == InventoryType.PLAYER){
                return;
            }

            e.setCancelled(true);
            if (slot == 0){
                set(player,"total",getLang("inventory.anvil.total"),config,file,true);
            }
            if (slot == 2){
                set(player,"see",getLang("inventory.anvil.see"),config,file,true);
            }
            if (slot == 4){
                set(player,"hide",getLang("inventory.anvil.hide"),config,file,true);
            }
            if (slot == 6){
                set(player,"time",getLang("inventory.anvil.time"),config,file,true);
            }
            if (slot == 7){
                set(player,"tc",getLang("inventory.anvil.tc"),config,file,true);
            }
            if (slot == 8){
                config.set("enable",!config.getBoolean("enable"));
                save(config,file);
                player.closeInventory();
                open(player);
            }
            List<ItemStack> stackList = new ArrayList<>();
            for (int i=18;i < 45;i++){
                ItemStack itemStack = e.getInventory().getItem(i);
                if (itemStack == null || itemStack.getType() == Material.AIR){
                    continue;
                }
                stackList.add(itemStack);
            }
            config.set("blocks",stackList);
            save(config,file);
        }
    }

    @EventHandler
    public void close(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        World world = player.getWorld();
        if (player.getOpenInventory().getTitle().equalsIgnoreCase(getLang("inventory.title").replace("{0}",world.getName()))) {
            File file = new File(plugin.getDataFolder() + "/maps/" + world.getName() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<ItemStack> stackList = new ArrayList<>();
            for (int i=18;i < 45;i++){
                ItemStack itemStack = e.getInventory().getItem(i);
                if (itemStack == null || itemStack.getType() == Material.AIR){
                    continue;
                }
                stackList.add(itemStack);
            }
            config.set("blocks",stackList);
            save(config,file);
        }
    }
}
