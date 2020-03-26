package com.hax.bbnmcplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class MLGCommand implements CommandExecutor {

    Plugin plugin;

    public MLGCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 0) {
                sender.sendMessage("False Arguments! Use /mlg [Player1] [Player2] [Player3] ...");
            } else {
                for (String playername : args) {
                    if (sender.getServer().getPlayer(playername) != null) {
                        Player player = sender.getServer().getPlayer(playername);
                        ItemStack[] orig = player.getInventory().getContents();
                        Location oldlocation = player.getLocation();

                        player.getInventory().setContents(new ItemStack[0]);
                        player.getInventory().setItemInMainHand(new ItemStack(Material.WATER_BUCKET, 1));
                        player.updateInventory();

                        player.teleport(new Location(sender.getServer().getWorld("world"), player.getLocation().getX(),
                                player.getLocation().getY() + 100, player.getLocation().getZ()));

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            player.getInventory().setContents(orig);
                            player.updateInventory();
                            player.teleport(oldlocation);
                        }, 300L);
                    }
                }
            }
            return true;
        } else return false;
    }
}
