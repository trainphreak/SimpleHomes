/*
 * Copyright (c) 2014, LankyLord
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.lankylord.simplehomes.homes;

import net.lankylord.simplehomes.configuration.ConfigManager;
import net.lankylord.simplehomes.storage.HomeFileManager;
import net.lankylord.simplehomes.storage.SimpleHomesStore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager implements SimpleHomesStore {

    private final HomeFileManager fileManager;
    private final Map<UUID, Map<String, Location>> loadedHomes;

    public HomeManager(HomeFileManager fileManager) {
        this.fileManager = fileManager;
        this.loadedHomes = new HashMap<>();
    }

    @Override
    public boolean reachedMaxHomes(UUID uuid) {
        return getHomeAmount(uuid) == ConfigManager.getMaxHomes();
    }

    @Override
    public int getHomeAmount(UUID uuid) {
        if (fileManager.getHomes().contains(uuid.toString())) {
            return fileManager.getHomes().getConfigurationSection(uuid.toString()).getKeys(false).size();
        }
        return 0;
    }

    @Override
    public void saveHome(UUID uuid, String homeName, Location location) {
        ConfigurationSection home = fileManager.getHomes().getConfigurationSection(uuid.toString() + "." +
                                                                                   homeName.toLowerCase());
        if (home == null) {
            home = fileManager.getHomes().createSection(uuid.toString() + "." + homeName.toLowerCase());
        }

        home.set("world", location.getWorld().getName());
        home.set("x", location.getBlockX());
        home.set("y", location.getBlockY());
        home.set("z", location.getBlockZ());

        fileManager.saveHomes();
    }

    @Override
    public void deleteHome(UUID uuid, String homeName) {
        ConfigurationSection home = fileManager.getHomes().getConfigurationSection(uuid.toString());
        home.set(homeName.toLowerCase(), null);
        fileManager.saveHomes();
    }

    @Override
    public void addHomeToCache(UUID uuid, String homeName, Location location) {
        Map<String, Location> homeLocation = loadedHomes.get(uuid);
        if (homeLocation == null) {
            homeLocation = new HashMap<>();
        }
        homeLocation.put(homeName.toLowerCase(), location);
        loadedHomes.put(uuid, homeLocation);
    }

    @Override
    public void removeHomeFromCache(UUID uuid, String homeName) {
        Map homeLocations = loadedHomes.get(uuid);
        if (homeLocations != null) {
            homeLocations.remove(homeName.toLowerCase());
            loadedHomes.put(uuid, homeLocations);
        }
    }

    @Override
    public void loadPlayerHomesIntoCache(UUID uuid) {
        ConfigurationSection homes = fileManager.getHomes().getConfigurationSection(uuid.toString());
        if (homes != null) {
            Map<String, Location> homeLocation = new HashMap<>();

            for (String homeName : homes.getKeys(false)) {
                ConfigurationSection home = homes.getConfigurationSection(homeName);

                String world = home.getString("world", null);
                int x = home.getInt("x", Integer.MIN_VALUE);
                int y = home.getInt("y", Integer.MIN_VALUE);
                int z = home.getInt("z", Integer.MIN_VALUE);

                if (!(world == null || x == Integer.MIN_VALUE || y == Integer.MIN_VALUE || z == Integer.MIN_VALUE)) {
                    homeLocation.put(homeName.toLowerCase(), new Location(Bukkit.getWorld(world), x, y, z));
                } else {
                    System.out.println("Error in home, not loaded.");
                }
            }
            loadedHomes.put(uuid, homeLocation);
        }
    }

    @Override
    public void unloadPlayerHomesFromCache(UUID uuid) {
        loadedHomes.remove(uuid);
    }

    @Override
    public Location getHome(UUID uuid, String homeName) {
        Map<String, Location> homeLocations = loadedHomes.get(uuid);
        if (homeLocations != null) {
            return homeLocations.get(homeName.toLowerCase());
        } else {
            ConfigurationSection homes = fileManager.getHomes().getConfigurationSection(uuid.toString());
            Map<String, Location> homeLocation = new HashMap<>();
            if (homes != null) {
                for (String home : homes.getKeys(false)) {
                    ConfigurationSection homeSection = homes.getConfigurationSection(home);
                    String world = homeSection.getString("world");
                    int x = homeSection.getInt("x");
                    int y = homeSection.getInt("y");
                    int z = homeSection.getInt("z");

                    homeLocation.put(home.toLowerCase(), new Location(Bukkit.getWorld(world), x, y, z));
                }
                return homeLocation.get(homeName.toLowerCase());
            }
        }
        return null;
    }

    @Override
    public Map<String, Location> getPlayerHomes(UUID uuid) {
        Map<String, Location> playerHomes = loadedHomes.get(uuid);
        if (playerHomes == null) {
            ConfigurationSection homes = fileManager.getHomes().getConfigurationSection(uuid.toString());
            if (homes != null) {
                for (String homeName : homes.getKeys(false)) {
                    ConfigurationSection home = homes.getConfigurationSection(homeName);

                    String world = home.getString("world", null);
                    int x = home.getInt("x", Integer.MIN_VALUE);
                    int y = home.getInt("y", Integer.MIN_VALUE);
                    int z = home.getInt("z", Integer.MIN_VALUE);

                    if (!(world == null || x == Integer.MIN_VALUE || y == Integer.MIN_VALUE || z == Integer.MIN_VALUE)) {
                        playerHomes.put(homeName.toLowerCase(), new Location(Bukkit.getWorld(world), x, y, z));
                    } else {
                        System.out.println("Error in home, not loaded.");
                    }
                }
            }
        }
        return playerHomes;
    }

    @Override
    public Map<UUID, Map<String, Location>> getHomeCache() {
        return loadedHomes;
    }
}