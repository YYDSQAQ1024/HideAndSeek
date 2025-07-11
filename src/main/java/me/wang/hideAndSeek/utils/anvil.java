package me.wang.hideAndSeek.utils;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import static me.wang.hideAndSeek.HideAndSeek.plugin;
import static me.wang.hideAndSeek.utils.config.save;
import static me.wang.hideAndSeek.utils.create.open;
import static me.wang.hideAndSeek.utils.language.getLang;

public class anvil {
    public static void set(Player player, String path, String title, YamlConfiguration config, File file,Boolean isInteger){
        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> {
                    player.closeInventory();
                    open(player);
                })
                .onClick((slot, stateSnapshot) -> {
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String s = stateSnapshot.getText();

                    if (isInteger){
                        config.set(path,Integer.valueOf(s));
                    }else {
                        config.set(path,s);
                    }
                    save(config,file);
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                })
                .text(getLang("inventory.anvil.input"))
                .title(title)
                .plugin(plugin)
                .open(player);
    }


}
