package me.wang.hideAndSeek.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class config {
    public static void save(YamlConfiguration configuration, File file) {
        try {
            configuration.save(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
