package com.francobm.simplefly;

import com.francobm.simplefly.commands.Command;
import com.francobm.simplefly.listeners.PlayerListener;
import com.francobm.simplefly.managers.SimpleFlyManager;
import com.francobm.simplefly.utils.UtilsSF;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleFly extends JavaPlugin {
    public String pluginName;
    private SimpleFlyManager simpleFlyManager;
    @Override
    public void onEnable() {
        super.onEnable();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        pluginName = getMessage("prefix", true);
        simpleFlyManager = new SimpleFlyManager(this);
        registerCommands();
        registerListeners();
        simpleFlyManager.onActiveParticles();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void registerCommands(){
        getCommand("fly").setExecutor(new Command(this));
    }

    public void registerListeners(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
    }

    public String getMessage(String path, boolean live){
        if(live){
            if(getConfig().getString("messages."+path) == null){
                return UtilsSF.ChatColor("SimpleFly &cError path Message Not Found");
            }else{
                return UtilsSF.ChatColor(getConfig().getString("messages."+path));
            }
        }else{
            if(getConfig().getString(path) == null) {
                return UtilsSF.ChatColor("SimpleFly &cError path Not Found");
            }else{
                return UtilsSF.ChatColor(getConfig().getString(path));
            }
        }
    }

    public SimpleFlyManager getSimpleFlyManager() {
        return simpleFlyManager;
    }
}
