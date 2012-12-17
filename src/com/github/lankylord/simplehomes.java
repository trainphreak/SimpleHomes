package com.github.lankylord;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class simplehomes extends JavaPlugin{
static final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        logger.info("SimpleHomes Enabled!");
        PluginManager pm = this.getServer().getPluginManager();
        pm.addPermission(new Permissions().canUseHomes);
        saveConfig();
    }

    @Override
    public void onDisable() {
        logger.info("SimpleHomes Disabled!");
        saveConfig();
        getServer().getPluginManager().removePermission(new Permissions().canUseHomes);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
    String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (commandLabel.equalsIgnoreCase("sethome")) {
                if (sender.hasPermission(new Permissions().canUseHomes)) {
                    getConfig().set(player.getName() + ".x",
                            player.getLocation().getBlockX());
                    getConfig().set(player.getName() + ".y",
                            player.getLocation().getBlockY());
                    getConfig().set(player.getName() + ".z",
                            player.getLocation().getBlockZ());
                    saveConfig();
                    sender.sendMessage(ChatColor.YELLOW + "Home set.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Sorry! You don't have permission to do that!");
                }
            } else if(commandLabel.equalsIgnoreCase("home")) {
                if(args.length == 0) {
                    if (sender.hasPermission(new Permissions().canUseHomes)) {
                        int x = getConfig().getInt(player.getName() + ".x"), y = getConfig()
                                .getInt(player.getName() + ".y"), z = getConfig()
                                .getInt(player.getName() + ".z");
                        player.teleport(new Location(player.getWorld(), x, y, z));
                        sender.sendMessage(ChatColor.YELLOW + "Teleported.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Sorry! You don't have permission to do that!");
                    }
                } else if(args.length == 1) {
                    if (sender.hasPermission(new Permissions().canTeleportOther)) {
                        int x = getConfig().getInt(args[0] + ".x"), y = getConfig()
                                .getInt(args[0] + ".y"), z = getConfig()
                                .getInt(args[0] + ".z");
                        player.teleport(new Location(player.getWorld(), x, y, z));
                        sender.sendMessage(ChatColor.YELLOW + "Teleported to " + args[0] + "'s home.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Sorry! You don't have permission to do that!");
                    }
                }
            }
        }
        return false;
    }
}


