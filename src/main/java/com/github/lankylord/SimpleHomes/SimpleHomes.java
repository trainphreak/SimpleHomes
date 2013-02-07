package com.github.lankylord.SimpleHomes;

import com.github.lankylord.SimpleHomes.commands.HomeCommand;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleHomes extends JavaPlugin {

    static final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        logger.info("SimpleHomes Enabled!");
        saveConfig();
        saveHomes();
        
        getCommand("home").setExecutor(new HomeCommand(this));
    }

    @Override
    public void onDisable() {
        logger.info("SimpleHomes Disabled!");
        saveConfig();
        saveHomes();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "sethome":
                    if (args.length == 0) {
                        if (sender.hasPermission("simplehomes.homes")) {
                            getHomes().set(player.getName() + ".world",
                                    player.getWorld().getName());
                            getHomes().set(player.getName() + ".x",
                                    player.getLocation().getBlockX());
                            getHomes().set(player.getName() + ".y",
                                    player.getLocation().getBlockY());
                            getHomes().set(player.getName() + ".z",
                                    player.getLocation().getBlockZ());
                            saveHomes();
                            sender.sendMessage(ChatColor.YELLOW + "Home set.");
                            break;
                        }
                    } else if (args.length == 1) {
                        if (sender.hasPermission("simplehomes.homes")) {
                            getHomes().set(player.getName() + "." + args[0] + ".world",
                                    player.getWorld().getName());
                            getHomes().set(player.getName() + "." + args[0] + ".x",
                                    player.getLocation().getBlockX());
                            getHomes().set(player.getName() + "." + args[0] + ".y",
                                    player.getLocation().getBlockY());
                            getHomes().set(player.getName() + "." + args[0] + ".z",
                                    player.getLocation().getBlockZ());
                            saveHomes();
                            sender.sendMessage(ChatColor.YELLOW + "Home set.");
                            break;
                        }
                    }

                case "otherhome":
                    if (args.length == 1) {
                        if (sender.hasPermission("simplehomes.otherhomes")) {
                            String w = getHomes().getString(args[0] + ".world");
                            int x = getHomes().getInt(args[0] + ".x"),
                                    y = getHomes().getInt(args[0] + ".y"),
                                    z = getHomes().getInt(args[0] + ".z");
                            player.teleport(new Location(Bukkit.getWorld(w), x, y, z));
                            sender.sendMessage(ChatColor.YELLOW + "Teleported to " + args[0] + "'s home.");
                        }
                    } else if (args.length == 2) {
                        if (sender.hasPermission("simplehomes.otherhomes")) {
                            String w = getHomes().getString(args[0] + "." + args[1] + ".world");
                            int x = getHomes().getInt(args[0] + "." + args[1] + ".x"),
                                    y = getHomes().getInt(args[0] + "." + args[1] + ".y"),
                                    z = getHomes().getInt(args[0] + "." + args[1] + ".z");
                            player.teleport(new Location(Bukkit.getWorld(w), x, y, z));
                            sender.sendMessage(ChatColor.YELLOW + "Teleported to " + args[0] + "'s home.");
                        }
                    }
            }
        }
        return false;
    }
    private FileConfiguration Homes = null;
    private File HomesFile = null;

    public void reloadHomes() {
        if (HomesFile == null) {
            HomesFile = new File(getDataFolder(), "Homes.yml");
        }
        Homes = YamlConfiguration.loadConfiguration(HomesFile);

        InputStream defHomes = this.getResource("Homes.yml");
        if (defHomes != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defHomes);
            Homes.setDefaults(defConfig);
        }
    }

    public FileConfiguration getHomes() {
        if (Homes == null) {
            this.reloadHomes();
        }
        return Homes;
    }

    public void saveHomes() {
        if (Homes == null || HomesFile == null) {
            return;
        }
        try {
            getHomes().save(HomesFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + HomesFile, ex);
        }
    }
}