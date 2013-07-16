/*
 * Copyright (c) 2013 cedeel.
 * All rights reserved.
 * 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * The name of the author may not be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.lankylord.simplehomes.commands;

import java.util.List;
import net.lankylord.simplehomes.SimpleHomes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author cedeel
 */
public class OtherHomeCommand extends SimpleHomesCommand {

    public OtherHomeCommand(SimpleHomes plugin) {
        super(plugin);
        this.setName("SimpleHomes: Delete Home");
        this.setCommandUsage("/home delete [HomeName]");
        this.setArgRange(0, 1);
        this.addKey("home delete");
        this.addKey("delhome");
        this.setPermission("simplehomes.homes", "Allows this user access to basic home commands", PermissionDefault.TRUE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (sender instanceof Player && args.size() >= 1) {
            Player p = (Player) sender;
            String home = "default";
            if (args.size() == 2)
                home = args.get(1).toLowerCase();

            String target = args.get(0).toLowerCase();

            if (plugin.getHomeFileManager().getHomes().contains(target + "." + home)) {
                ConfigurationSection homes = plugin.getHomeFileManager().getHomes().getConfigurationSection(target + "." + home);
                String w = homes.getString("world");
                int x = homes.getInt("x"),
                        y = homes.getInt("y"),
                        z = homes.getInt("z");
                p.teleport(new Location(Bukkit.getWorld(w), x, y, z));
                p.sendMessage(ChatColor.YELLOW + "Teleported to " + target + "'s home.");
            }
        }
    }
}
