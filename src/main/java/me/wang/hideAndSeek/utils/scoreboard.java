package me.wang.hideAndSeek.utils;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.MessageDigest;

import static me.wang.hideAndSeek.HideAndSeek.*;

public class scoreboard {

    public static Scoreboard init(World world){
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective(world.getName(),"dummy", ChatColor.AQUA+"躲猫猫");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        newScore(ChatColor.YELLOW+"地图："+world.getName(),10,obj);
        newScore(ChatColor.GREEN+"",9,obj);
        if (ready.containsKey(world.getName())){
            newScore(ChatColor.GREEN+"倒计时:"+ready.get(world.getName()),8,obj);
        }else {
            newScore(ChatColor.GREEN+"倒计时:等待中",8,obj);
        }
        return scoreboard;
    }

    public static void newScore(String s,int level,Objective obj){
        Score score = obj.getScore(s);
        score.setScore(level);
    }



    private static String readResponseContent(HttpURLConnection con) {
        StringBuilder response = new StringBuilder();
        try (InputStream inputStream = con.getInputStream();
             BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}
