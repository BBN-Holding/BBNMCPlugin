package com.hax.bbnmcplugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Listener implements org.bukkit.event.Listener {

    HashMap<String, String> objectivesMap = new HashMap<>();

    Plugin plugin;
    JDA jda;

    public Listener(Plugin plugin, JDA jda) {
        this.plugin = plugin;
        this.jda = jda;
    }

    public void setup() {
        objectivesMap.put("sneaking", "minecraft.custom:minecraft.crouch_one_cm");
        objectivesMap.put("aviating", "minecraft.custom:minecraft.aviate_one_cm");
        objectivesMap.put("sprinting", "minecraft.custom:minecraft.sprint_one_cm");
        objectivesMap.put("walking", "minecraft.custom:minecraft.walk_one_cm");
        objectivesMap.put("climbing", "minecraft.custom:minecraft.climb_one_cm");
        objectivesMap.put("boating", "minecraft.custom:minecraft.boat_one_cm");
        objectivesMap.put("waunder", "minecraft.custom:minecraft.walk_under_water_one_cm");
        objectivesMap.put("wawater", "minecraft.custom:minecraft.walk_on_water_one_cm");
        objectivesMap.put("carting", "minecraft.custom:minecraft.minecart_one_cm");
        objectivesMap.put("swimming", "minecraft.custom:minecraft.swim_one_cm");
        objectivesMap.put("flying", "minecraft.custom:minecraft.fly_one_cm");
        objectivesMap.put("horsing", "minecraft.custom:minecraft.horse_one_cm");
        objectivesMap.put("pigging", "minecraft.custom:minecraft.pig_one_cm");
    }

    public void registerObjectives(Plugin plugin) {

        objectivesMap.keySet().forEach(objective -> {
            if (plugin.getServer().getScoreboardManager().getMainScoreboard().getObjective("BBN_" + objective) == null) {
                plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewObjective("BBN_" + objective, objectivesMap.get(objective), objective);
                plugin.getLogger().info("Registered Objective: " + "BBN_" + objective);
            }

            if (plugin.getServer().getScoreboardManager().getMainScoreboard().getObjective("BBN_total") == null) {
                plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewObjective("BBN_total", "dummy", "Bewegte Blöcke");
                plugin.getLogger().info("Registered Objective: " + "BBN_total");
            }
        });
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        int range = 0;
        for (Object objective : objectivesMap.keySet().toArray()) {
            range = range + event.getPlayer().getServer().getScoreboardManager().getMainScoreboard().getObjective("BBN_" + objective).getScore(event.getPlayer().getName()).getScore();
        }
        event.getPlayer().getServer().getScoreboardManager().getMainScoreboard().getObjective("BBN_total").getScore(event.getPlayer().getName()).setScore(range / 100);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        for (Player player : event.getEntity().getServer().getOnlinePlayers()) {
            if (!player.equals(event.getEntity())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        player.chat("F"), 20L);
                if (event.getEntity().getName().equals("LoguHD")) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            player.chat("Hugo ist so schlecht LUL"), 20L);
                }
            }
        }
        if (event.getEntity().getName().equals("LoguHD")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    event.getEntity().chat("WTF bin ich schlecht"), 20L);
        }
        event.getEntity().sendMessage("You did at: " + event.getEntity().getLocation().toString());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (jda != null) {
            String guildid = plugin.getConfig().getString("guildid");
            if (guildid != null) {
                int count = 0;
                Guild guild = jda.getGuildById(guildid);
                for (VoiceChannel vc : guild.getVoiceChannels()) {
                    count = count + vc.getMembers().size();
                }
                guild.retrieveMembers()
                        .thenApply((v) -> guild.getMemberCache())
                        .thenAccept((members) -> {
                            event.getPlayer().getServer().broadcastMessage(String.valueOf(members.size()));
                            for (Member member : members) {
                                for (Activity activity:member.getActivities()) {
                                    event.getPlayer().getServer().broadcastMessage(activity.getName());
                                }
                            }
                        });
                if (count == 0) {
                    if (event.getPlayer().getServer().getOnlinePlayers().size() == 1) {
                        event.getPlayer().getServer().broadcastMessage("§7[§4BBN§7] §6There are no members talking on Discord...");
                    } else {
                        event.getPlayer().getServer().broadcastMessage("§7[§4BBN§7] §6There are no members talking on Discord... Why dont you guys just join?");
                    }
                } else {
                    event.getPlayer().getServer().broadcastMessage("§7[§4BBN§7] §6There are " + count + " members talking on Discord...");
                }
            }
        }
    }

}
