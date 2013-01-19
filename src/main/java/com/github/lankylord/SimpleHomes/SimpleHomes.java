package com.github.lankylord.SimpleHomes;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleHomes extends JavaPlugin{
    static final Logger logger = Logger.getLogger("Minecraft");


    @Override
    public void onEnable() {
        logger.info("SimpleHomes Enabled!");
        saveConfig();
    }

    @Override
    public void onDisable() {
        logger.info("SimpleHomes Disabled!");
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName().toLowerCase()) {
            case "sethome":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if(args.length == 0) {
                        if (sender.hasPermission("simplehomes.homes")) {
                            getConfig().set(player.getName() + ".x",
                                    player.getLocation().getBlockX());
                            getConfig().set(player.getName() + ".y",
                                    player.getLocation().getBlockY());
                            getConfig().set(player.getName() + ".z",
                                    player.getLocation().getBlockZ());
                            saveConfig();
                            sender.sendMessage(ChatColor.YELLOW + "Home set.");
                            break;
                        }
                    } else if(args.length == 1) {
                        if (sender.hasPermission("simplehomes.homes")) {
                            getConfig().set(player.getName() + "." + args[0] + ".x",
                                    player.getLocation().getBlockX());
                            getConfig().set(player.getName() + "." + args[0] + ".y",
                                    player.getLocation().getBlockY());
                            getConfig().set(player.getName() + "." + args[0] + ".z",
                                    player.getLocation().getBlockZ());
                            saveConfig();
                            sender.sendMessage(ChatColor.YELLOW + "Home set.");
                            break;
                        }
                    }
                }
            case "home":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if(args.length == 0) {
                        if (sender.hasPermission("simplehomes.homes")) {
                            int x = getConfig().getInt(player.getName() + ".x"), y = getConfig()
                                    .getInt(player.getName() + ".y"), z = getConfig()
                                    .getInt(player.getName() + ".z");
                            player.teleport(new Location(player.getWorld(), x, y, z));
                            sender.sendMessage(ChatColor.YELLOW + "Teleported.");
                        }
                    } else if(args.length == 1) {
                        if (sender.hasPermission("simplehomes.multihomes")) {
                            int x = getConfig().getInt(player.getName() + "." + args[0] + ".x"), y = getConfig()
                                    .getInt(player.getName() + "." + args[0] + ".y"), z = getConfig()
                                    .getInt(player.getName() + "." + args[0] + ".z");
                            player.teleport(new Location(player.getWorld(), x, y, z));
                            sender.sendMessage(ChatColor.YELLOW + "Teleported.");
                            break;
                        }
                    }
                }
            case "otherhome":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if(args.length == 1) {
                        if (sender.hasPermission("simplehomes.otherhomes")) {
                            int x = getConfig().getInt(args[0] + ".x"), y = getConfig()
                                    .getInt(args[0] + ".y"), z = getConfig()
                                    .getInt(args[0] + ".z");
                            player.teleport(new Location(player.getWorld(), x, y, z));
                            sender.sendMessage(ChatColor.YELLOW + "Teleported to " + args[0] + "'s home.");
                        }
                    } else if(args.length == 2) {
                        if (sender.hasPermission("simplehomes.otherhomes")) {
                            int x = getConfig().getInt(args[0] + "." + args[1] + ".x"), y = getConfig()
                                    .getInt(args[0] + "." + args[1] + ".y"), z = getConfig()
                                    .getInt(args[0] + "." + args[1] + ".z");
                            player.teleport(new Location(player.getWorld(), x, y, z));
                            sender.sendMessage(ChatColor.YELLOW + "Teleported to " + args[0] + "'s home.");
                        }
                    }
                }
        } return false;
    }
}