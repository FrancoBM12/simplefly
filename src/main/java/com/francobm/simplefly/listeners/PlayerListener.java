package com.francobm.simplefly.listeners;

import com.francobm.simplefly.SimpleFly;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final SimpleFly plugin;

    public PlayerListener(SimpleFly plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(plugin.getConfig().getBoolean("fly-on-join")) {
            if(!player.getAllowFlight()) {
                plugin.getSimpleFlyManager().fly(player, true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if(plugin.getConfig().getBoolean("fly-on-join")) {
            Player player = event.getPlayer();
            if (player.getAllowFlight()) {
                plugin.getSimpleFlyManager().fly(player, true);
            }
        }
    }
}
