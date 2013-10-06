/*
* Copyright (c) 2013, LankyLord
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* * Redistributions of source code must retain the above copyright notice, this
* list of conditions and the following disclaimer.
* * Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package net.lankylord.simplehomes.listeners;

import net.lankylord.simplehomes.SimpleHomes;
import net.lankylord.simplehomes.managers.HomeManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GatewayListener implements Listener {

    private SimpleHomes simpleHomes;
    private HomeManager homeManager;

    public GatewayListener(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
        this.homeManager = simpleHomes.getHomeManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        homeManager.loadPlayerHomes(event.getPlayer().getName().toLowerCase());
        Player player = event.getPlayer();
        if (player.hasPermission("simplehomes.notify")) {
            if (simpleHomes.isUpdateAvailable()) {
                player.sendMessage(ChatColor.YELLOW + "[SimpleHomes] There is an update available on BukkitDev!");
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        homeManager.unloadPlayerHomes(event.getPlayer().getName().toLowerCase());
    }
}
