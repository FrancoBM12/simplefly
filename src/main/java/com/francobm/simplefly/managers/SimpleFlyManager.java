package com.francobm.simplefly.managers;

import com.francobm.simplefly.SimpleFly;
import com.francobm.simplefly.events.ChangeFlyEvent;
import com.francobm.simplefly.utils.UtilsSF;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleFlyManager {
    private final SimpleFly plugin;
    private final Map<Player, Boolean> players;
    private BukkitTask bukkitTask;

    public SimpleFlyManager(SimpleFly plugin){
        this.plugin = plugin;
        players = new HashMap<>();
    }

    public void onActiveParticles(){
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : players.keySet()){
                    if(player != null) {
                        particles(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 10L, 2L);
    }

    private void particles(Player player){
        if (player.isFlying()) {
            Location location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                    if(location.equals(loc)){
                        players.replace(player, false);
                    }else{
                        players.replace(player, true);
                    }
                }
            }.runTaskLater(plugin, 10L);
            if(!players.get(player)) return;
            List<String> stringList = plugin.getConfig().getStringList("particle-permissions");
            for (String perm : stringList) {
                String[] strings = perm.split(";");
                if (player.hasPermission("simplefly.effects." + strings[0])) {
                    player.getWorld().playEffect(player.getLocation().subtract(0, 0.2, 0), Effect.valueOf(strings[1].toUpperCase()), 0);
                    return;
                }
            }
            player.getWorld().playEffect(player.getLocation().subtract(0, 0.2, 0), Effect.valueOf(plugin.getMessage("particle", false).toUpperCase()), 0);
        }
    }

    public void fly(Player player, boolean free){
        if(!free) {
            if (!player.hasPermission("simplefly.fly")) {
                player.sendMessage(plugin.pluginName + plugin.getMessage("no-permission", true));
                return;
            }
        }
        if(!players.containsKey(player)){
            ChangeFlyEvent changeFlyEvent = new ChangeFlyEvent(player, ChangeFlyEvent.Status.FLY_ON);
            plugin.getServer().getPluginManager().callEvent(changeFlyEvent);
            if(changeFlyEvent.isCancelled()) return;
            player.sendMessage(plugin.pluginName + plugin.getMessage("fly-on", true));
            players.put(player, true);
            player.setAllowFlight(true);
            return;
        }
        if(player.getGameMode() == GameMode.CREATIVE) {
            ChangeFlyEvent changeFlyEvent = new ChangeFlyEvent(player, ChangeFlyEvent.Status.FLY_OFF);
            plugin.getServer().getPluginManager().callEvent(changeFlyEvent);
            if(changeFlyEvent.isCancelled()) return;
            players.remove(player);
            player.sendMessage(plugin.pluginName + plugin.getMessage("fly-off", true));
            return;
        }
        ChangeFlyEvent changeFlyEvent = new ChangeFlyEvent(player, ChangeFlyEvent.Status.FLY_OFF);
        plugin.getServer().getPluginManager().callEvent(changeFlyEvent);
        if(changeFlyEvent.isCancelled()) return;
        players.remove(player);
        player.sendMessage(plugin.pluginName + plugin.getMessage("fly-off", true));
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public void fly(Player player, Player target){
        if(target == null){
            player.sendMessage(plugin.pluginName + plugin.getMessage("player-offline", true));
            return;
        }
        if(!player.hasPermission("simplefly.fly.others")){
            player.sendMessage(plugin.pluginName + plugin.getMessage("no-permission", true));
            return;
        }
        if(!players.containsKey(target)){
            ChangeFlyEvent changeFlyEvent = new ChangeFlyEvent(target, ChangeFlyEvent.Status.FLY_ON);
            plugin.getServer().getPluginManager().callEvent(changeFlyEvent);
            if(changeFlyEvent.isCancelled()) return;
            target.sendMessage(plugin.pluginName + plugin.getMessage("fly-on", true));
            players.put(target, true);
            target.setAllowFlight(true);
            target.setFlying(true);
            return;
        }
        ChangeFlyEvent changeFlyEvent = new ChangeFlyEvent(target, ChangeFlyEvent.Status.FLY_OFF);
        plugin.getServer().getPluginManager().callEvent(changeFlyEvent);
        if(changeFlyEvent.isCancelled()) return;
        players.remove(target);
        target.sendMessage(plugin.pluginName + plugin.getMessage("fly-off", true));
        target.setAllowFlight(false);
        target.setFlying(false);
    }

    public void fly(ConsoleCommandSender consoleCommandSender, Player target){
        if(target == null){
            consoleCommandSender.sendMessage(plugin.pluginName + plugin.getMessage("player-offline", true));
            return;
        }
        if(!players.containsKey(target)){
            ChangeFlyEvent changeFlyEvent = new ChangeFlyEvent(target, ChangeFlyEvent.Status.FLY_ON);
            plugin.getServer().getPluginManager().callEvent(changeFlyEvent);
            if(changeFlyEvent.isCancelled()) return;
            target.sendMessage(plugin.pluginName + plugin.getMessage("fly-on", true));
            players.put(target, true);
            target.setAllowFlight(true);
            target.setFlying(true);
            return;
        }
        ChangeFlyEvent changeFlyEvent = new ChangeFlyEvent(target, ChangeFlyEvent.Status.FLY_OFF);
        plugin.getServer().getPluginManager().callEvent(changeFlyEvent);
        if(changeFlyEvent.isCancelled()) return;
        players.remove(target);
        target.sendMessage(plugin.pluginName + plugin.getMessage("fly-off", true));
        target.setAllowFlight(false);
        target.setFlying(false);
    }

    public void reload(Player player){
        if(!player.hasPermission("simplefly.reload")){
            player.sendMessage(plugin.pluginName + plugin.getMessage("no-permission", true));
            return;
        }
        getBukkitTask().cancel();
        plugin.getLogger().info(UtilsSF.ChatColor("SimpleFly >> Plugin reloading.."));
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        plugin.pluginName = plugin.getMessage("prefix", true);
        player.sendMessage(UtilsSF.ChatColor(plugin.pluginName + plugin.getMessage("reload", true)));
        onActiveParticles();
        plugin.getLogger().info("SimpleFly " + plugin.getDescription().getVersion() + " has successfully reloaded!");
    }

    public void reload(){
        getBukkitTask().cancel();
        plugin.getLogger().info(UtilsSF.ChatColor("SimpleFly >> Plugin reloading.."));
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        plugin.pluginName = plugin.getMessage("prefix", true);
        onActiveParticles();
        plugin.getLogger().info("SimpleFly " + plugin.getDescription().getVersion() + " has successfully reloaded!");
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }
}
