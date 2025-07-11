package me.wang.hideAndSeek.tasks;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;

import static me.wang.hideAndSeek.HideAndSeek.*;
import static me.wang.hideAndSeek.utils.fallingblock.removePlayerFallingBlock;
import static me.wang.hideAndSeek.utils.language.getLang;

public class end extends BukkitRunnable {
    @Override
    public void run() {
        for (Map.Entry entry:playing_time.entrySet()){
            String s = entry.getKey().toString();
            int t = (int) entry.getValue();
            if (t == 99999){
                World world = Bukkit.getWorld(s);
                for (Player player:world.getPlayers()){
                    Firework firework = world.spawn(player.getLocation(), Firework.class);
                    FireworkMeta meta = firework.getFireworkMeta();
                    FireworkEffect effect = FireworkEffect.builder()
                            .withColor(Color.RED)
                            .withFade(Color.RED)
                            .with(FireworkEffect.Type.BALL)
                            .build();

                    meta.addEffect(effect);
                    meta.setPower(2);
                    firework.setFireworkMeta(meta);
                }
            }
            if (t > 0){
                continue;
            }
            World world = Bukkit.getWorld(s);
            String win;
            if (hiders.get(s).size() < 1){
                win = getLang("end.seeker");
            }else {
                win = getLang("end.hider");
            }

            Team team = teams.get(s);

            for (Player player:world.getPlayers()){
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                player.sendMessage(getLang("end.end"));
                player.sendMessage(getLang("end.winner").replace("{0}",win));
                player.sendMessage(getLang("end.close"));
                player.getInventory().clear();
                team.removeEntry(player.getName());
            }
            for (Player player:hiders.get(world.getName())){
                removePlayerFallingBlock(player);
            }

            for (Player player1:playing_players.get(world.getName())){
                player1.setInvisible(false);
            }

            playing_time.put(s,99999);
            playing.remove(s);
            hiders.remove(s);
            seekers.remove(s);
            spectators.remove(s);
            Scoreboard scoreboard = scoreboards.get(s);

            Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            obj.unregister();
            scoreboards.remove(s);
            team.unregister();
            playing_players.remove(s);
            new BukkitRunnable() {
                @Override
                public void run() {
                    playing_time.remove(s);
                    for (Player player:world.getPlayers()){
                        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                    }
                }
            }.runTaskLater(plugin, 15*20);
        }
    }
}
