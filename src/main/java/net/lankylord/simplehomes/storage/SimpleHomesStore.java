/*
 * Copyright (c) 2014, Scott Kendall
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

package net.lankylord.simplehomes.storage;

import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

public interface SimpleHomesStore {

    /**
     * Checks whether a player has reached the maximum amount of homes.
     *
     * @param uuid The UUID of the player.
     * @return True if the player has reached the maximum amount of homes. False if not.
     */
    public boolean reachedMaxHomes(UUID uuid);

    /**
     * Gets the amount of homes owned by a player.
     *
     * @param uuid The UUID of the player.
     * @return Amount of homes owned by the player.
     */
    public int getHomeAmount(UUID uuid);

    /**
     * Saves a home to storage. This does not add the home to the cache.
     *
     * @param uuid The UUID of the player.
     * @param homeName The name of the home.
     * @param location The location of the home.
     */
    public void saveHome(UUID uuid, String homeName, Location location);

    /**
     * Remove a home from storage. This does not remove the home from the cache.
     *
     * @param uuid The UUID of the player.
     * @param homeName The name of the home.
     */
    public void deleteHome(UUID uuid, String homeName);

    /**
     * Load a home into the cache.
     *
     * @param uuid The UUID of the player.
     * @param homeName The name of the home.
     * @param location The location of the home.
     */
    public void addHomeToCache(UUID uuid, String homeName, Location location);

    /**
     * Remove a home from the cache.
     *
     * @param uuid The UUID of the player.
     * @param homeName The name of the home.
     */
    public void removeHomeFromCache(UUID uuid, String homeName);

    /**
     * Load all of a player's homes into the cache.
     *
     * @param uuid The UUID of the player.
     */
    public void loadPlayerHomesIntoCache(UUID uuid);

    /**
     * Remove all of a player's homes from the cache.
     *
     * @param uuid The UUID of the player.
     */
    public void unloadPlayerHomesFromCache(UUID uuid);

    /**
     * Gets a player's home
     *
     * @param uuid The UUID of the player.
     * @param homeName The name of the home.
     * @return The Location of the home
     */
    public Location getHome(UUID uuid, String homeName);

    /**
     * Gets all of a player's homes
     * 
     * @param uuid The UUID of the player.
     * @return Map containing home names and locations.
     */
    public Map<String, Location> getPlayerHomes(UUID uuid);

    /**
     * Gets the home cache.
     *
     * @return The cache.
     */
    public Map<UUID, Map<String, Location>> getHomeCache();
}
