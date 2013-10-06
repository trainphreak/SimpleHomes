package net.lankylord.simplehomes;

import net.lankylord.simplehomes.commands.*;
import net.lankylord.simplehomes.listeners.GatewayListener;
import net.lankylord.simplehomes.managers.ConfigManager;
import net.lankylord.simplehomes.managers.HomeFileManager;
import net.lankylord.simplehomes.managers.HomeManager;
import net.lankylord.simplehomes.managers.languages.LanguageFileManager;
import net.lankylord.simplehomes.managers.languages.LanguageManager;
import net.lankylord.simplehomes.util.Updater;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SimpleHomes extends JavaPlugin {

    private HomeFileManager homeFileManager;
    private HomeManager homeManager;

    private boolean updateAvailable = false;

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();
        this.homeFileManager = new HomeFileManager(this);
        this.homeManager = new HomeManager(homeFileManager);
        LanguageFileManager languageFileManager = new LanguageFileManager(this);
        languageFileManager.saveLanguages();
        new LanguageManager(languageFileManager);
        new ConfigManager(this);
        homeFileManager.saveHomes();
        loadCommands();
        loadListeners();
        if (getConfig().getBoolean("AutoUpdater.Enabled", true)) {
            loadUpdater();
        }
        getLogger().info("SimpleHomes Enabled!");
    }

    @Override
    public void onDisable() {
        homeFileManager.saveHomes();
        getLogger().log(Level.INFO, "SimpleHomes Disabled!");
    }

    private void loadUpdater() {
        if (getConfig().getBoolean("AutoUpdater.Enabled")) {
            String mode = getConfig().getString("AutoUpdater.Mode");
            Updater.UpdateType updateType;
            switch (mode.toLowerCase()) {
                case "update":
                    //Update the plugin automatically
                    updateType = Updater.UpdateType.DEFAULT;
                    getLogger().log(Level.INFO, "AutoUpdater Enabled: Update");
                    break;
                case "notify":
                    //Notify admins of available updates
                    updateType = Updater.UpdateType.NO_DOWNLOAD;
                    getLogger().log(Level.INFO, "AutoUpdater Enabled: Notify");
                    break;
                default:
                    //Default to notification mode
                    updateType = Updater.UpdateType.NO_DOWNLOAD;
                    getLogger().log(Level.INFO, "AutoUpdater Enabled: Invalid Mode - Defaulting to Notify");
                    break;
            }
            Updater updater = new Updater(this, this.getFile(), updateType);
            Updater.UpdateResult updateResult = updater.getResult();
            switch (updateResult) {
                case SUCCESS:
                    //Update detected and downloaded
                    getLogger().log(Level.INFO, "Plugin will be updated at next restart");
                    updateAvailable = true;
                    break;
                case FAIL_DOWNLOAD:
                case FAIL_DBO:
                case FAIL_NOVERSION:
                    //Fail to get updates
                    getLogger().log(Level.WARNING, "Plugin failed to check for updates");
                    break;
                case UPDATE_AVAILABLE:
                    //Update detected but not downloaded
                    getLogger().log(Level.INFO, "There is an update available on BukkitDev");
                    updateAvailable = true;
                    break;
                case NO_UPDATE:
                    //No update found
                    getLogger().log(Level.INFO, "No update found.");
                    break;
            }
        }
    }

    private void loadCommands() {
        this.getCommand("delhome").setExecutor(new DeleteHomeCommand(this));
        this.getCommand("home").setExecutor(new HomeCommand(this));
        this.getCommand("homelist").setExecutor(new HomeListCommand(this));
        this.getCommand("otherhome").setExecutor(new OtherHomeCommand(this));
        this.getCommand("sethome").setExecutor(new SetHomeCommand(this));
    }

    public HomeFileManager getHomeFileManager() {
        return homeFileManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    private void loadListeners() {
        this.getServer().getPluginManager().registerEvents(new GatewayListener(this), this);
    }

    /**
     * Check whether there is an update available
     *
     * @return If there is an update available on BukkitDev
     */
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }
}
