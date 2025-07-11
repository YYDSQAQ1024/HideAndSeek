package me.wang.hideAndSeek.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.wang.hideAndSeek.HideAndSeek.plugin;
import static me.wang.hideAndSeek.utils.language.getLang;

public class create {
    public static List<Integer> bk = new ArrayList<>(Arrays.asList(
            1,3,5,9,10,11,12,13,14,15,16,17,45,46,47,48,49,50,51,52,53
    ));
    public static void open(Player player){
        World world = player.getWorld();
        File file = new File(plugin.getDataFolder()+"/maps/"+world.getName()+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Inventory inventory = Bukkit.createInventory(null,54, getLang("inventory.title").replace("{0}",world.getName()));
        ItemStack b = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE );
        ItemMeta bm = b.getItemMeta();
        bm.setDisplayName(ChatColor.RED+"");
        b.setItemMeta(bm);
        for (int i=0;i<54;i++){
            if (bk.contains(i)){
                inventory.setItem(i,b);
            }
        }
        ItemStack total = new ItemStack(Material.ARMOR_STAND);
        ItemMeta total_meta = total.getItemMeta();
        total_meta.setDisplayName(getLang("inventory.inv.total").replace("{0}",String.valueOf(config.getInt("total"))));
        total.setItemMeta(total_meta);
        inventory.setItem(0,total);

        ItemStack see = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta see_meta = see.getItemMeta();
        see_meta.setDisplayName(getLang("inventory.inv.see").replace("{0}",String.valueOf(config.getInt("see"))));
        see.setItemMeta(see_meta);
        inventory.setItem(2,see);

        ItemStack hide = new ItemStack(Material.MINECART);
        ItemMeta hide_meta = hide.getItemMeta();
        hide_meta.setDisplayName(getLang("inventory.inv.hide").replace("{0}",String.valueOf(config.getInt("hide"))));
        hide.setItemMeta(hide_meta);
        inventory.setItem(4,hide);

        ItemStack time = new ItemStack(Material.CLOCK);
        ItemMeta time_meta = time.getItemMeta();
        int t = config.getInt("time");
        time_meta.setDisplayName(getLang("inventory.inv.time").replace("{0}",String.valueOf(t)));
        time.setItemMeta(time_meta);
        inventory.setItem(6,time);

        ItemStack type;
        if (config.get("enable") == null ||! config.getBoolean("enable")){
            type = new ItemStack(Material.RED_CONCRETE);
        }else {
            type = new ItemStack(Material.GREEN_CONCRETE);
        }
        ItemMeta type_meta = type.getItemMeta();
        type_meta.setDisplayName(getLang("inventory.inv.enable").replace("{0}",String.valueOf(config.get("enable"))));
        type.setItemMeta(type_meta);
        inventory.setItem(8,type);

        ItemStack tc = new ItemStack(Material.REPEATER);
        ItemMeta tc_meta = tc.getItemMeta();
        tc_meta.setDisplayName(getLang("inventory.inv.tc").replace("{0}",String.valueOf(config.getInt("tc"))));
        tc.setItemMeta(tc_meta);
        inventory.setItem(7,tc);

        List stackList = config.getList("blocks");
        int n = 1;
        for (int l=18;l<45;l++){
            if (stackList == null){
                break;
            }
            if (n > stackList.size() ){
                break;
            }
            ItemStack itemStack = (ItemStack) stackList.get(l-18);
            inventory.setItem(l,itemStack);
            n++;
        }
        player.openInventory(inventory);
    }
}
