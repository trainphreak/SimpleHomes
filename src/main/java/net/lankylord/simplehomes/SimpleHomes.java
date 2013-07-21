package net.lankylord.simplehomes;

import com.pneumaticraft.commandhandler.CommandHandler;
import net.lankylord.simplehomes.commands.*;
import net.lankylord.simplehomes.managers.HomeFileManager;
import net.lankylord.simplehomes.util.PermissionsModule;
import net.lankylord.simplehomes.util.Updater;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleHomes extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private final HomeFileManager homeFileManager;

    public SimpleHomes() {
        this.homeFileManager = new HomeFileManager(this);
    }

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        loadCommands();
        saveDefaultConfig();
        homeFileManager.saveHomes();
        saveConfig();
        loadMetrics();
        if (getConfig().getBoolean("AutoUpdater.Enabled", true))
            loadUpdater();
        logger.info("[SimpleHomes] SimpleHomes Enabled!");
    }

    @Override
    public void onDisable() {
        homeFileManager.saveHomes();
        logger.log(Level.INFO, "SimpleHomes Disabled!");
    }

    private void loadMetrics() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to submit stats.");
        }
    }

    private void loadUpdater() {
        Updater updater = new Updater(this, "simplehomes", this.getFile(), Updater.UpdateType.DEFAULT, true);
        logger.log(Level.INFO, "[SimpleHomes] AutoUpdater Enabled.");
    }

    private void loadCommands() {
        PermissionsModule pm = new PermissionsModule();
        CommandHandler commandHandler = new CommandHandler(this, pm);
        commandHandler.registerCommand(new DeleteHomeCommand(this));
        commandHandler.registerCommand(new HomeCommand(this));
        commandHandler.registerCommand(new OtherHomeCommand(this));
        commandHandler.registerCommand(new SetHomeCommand(this));
        commandHandler.registerCommand(new HomeListCommand(this));
    }

    public HomeFileManager getHomeFileManager() {
        return homeFileManager;
    }
}