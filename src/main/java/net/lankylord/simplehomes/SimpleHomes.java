package net.lankylord.simplehomes;

import net.lankylord.simplehomes.util.Updater;
import net.lankylord.simplehomes.commands.DeleteHomeCommand;
import net.lankylord.simplehomes.commands.OtherHomeCommand;
import net.lankylord.simplehomes.commands.SetHomeCommand;
import net.lankylord.simplehomes.commands.HomeListCommand;
import net.lankylord.simplehomes.commands.HomeCommand;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lankylord.simplehomes.managers.HomeFileManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

public class SimpleHomes extends JavaPlugin {

    static final Logger logger = Logger.getLogger("Minecraft");
    private HomeFileManager homeFileManager;

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
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("otherhome").setExecutor(new OtherHomeCommand(this));
        getCommand("homelist").setExecutor(new HomeListCommand(this));
        getCommand("delhome").setExecutor(new DeleteHomeCommand(this));
    }

    public HomeFileManager getHomeFileManager() {
        return homeFileManager;
    }
}