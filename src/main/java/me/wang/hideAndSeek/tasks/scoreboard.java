package me.wang.hideAndSeek.tasks;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.time.formatSeconds;

public class scoreboard extends BukkitRunnable {

    private List<String> l = new ArrayList<>(Arrays.asList(
            "&e地图：%map%",
            "&r ",
            "$[WAITING]&f将在 %time% 后开始",
            "$[PLAYING]&f将在 %time% 后结束",
            "$[WAITINGJOIN]&f等待中...",
            "$[PLAYING]&r  ",
            "$[PLAYING]&3剩余寻找者：%seekers%",
            "$[PLAYING]&3剩余躲藏者：%hiders%",
            "&r   ",
            "躲猫猫 By lao_wang"
    ));




    @Override
    public void run() {
        for (Map.Entry<String,Scoreboard> entry:scoreboards.entrySet()){
            Scoreboard board =  entry.getValue();
            Objective objective = board.getObjective(entry.getKey());
            board.getEntries().forEach(board::resetScores);
            List<String> lines;

            if (run){
                lines = setting.getStringList("scoreboard.lines");
            }else {
                lines = l;
            }
            String a = "[WAITINGJOIN]";
            if (playing.contains(entry.getKey())){
                a = "[PLAYING]";
            }
            if (waiting_players.containsKey(entry.getKey())){
                a = "[WAITINGJOIN]";
            }
            if (ready.containsKey(entry.getKey())){
                a = "[WAITING]";
            }

            int maxLines = Math.min(lines.size(), 15);
            int lineIndex = maxLines;

            int t = 0;
            if (ready.containsKey(entry.getKey())){
                t = ready.get(entry.getKey());
                ready.put(entry.getKey(),t-1);
            }
            if (playing_time.containsKey(entry.getKey())){
                t = playing_time.get(entry.getKey());
                playing_time.put(entry.getKey(),t-1);
            }
            int seek = 0;
            if (seekers.containsKey(entry.getKey())){
                seek = seekers.get(entry.getKey()).size();
            }
            int hide = 0;
            if (hiders.containsKey(entry.getKey())){
                hide = hiders.get(entry.getKey()).size();
            }

            for (String entry1 : lines) {


                String formattedLine = ChatColor.translateAlternateColorCodes('&', entry1)
                        .replace("%map%", entry.getKey())
                        .replace("%time%", formatSeconds(t))
                        .replace("%seekers%", String.valueOf(seek))
                        .replace("%hiders%", String.valueOf(hide));

                if (entry1.startsWith("$")){
                    if (!entry1.startsWith("$"+a)){
                        continue;
                    }else {
                        formattedLine = formattedLine.replace("$"+a,"");
                    }
                }

                Score score = objective.getScore(formattedLine);
                score.setScore(lineIndex);
                lineIndex--;
                if (lineIndex < 0) break;
            }
        }
    }
}
