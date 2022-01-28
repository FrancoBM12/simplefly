package com.francobm.simplefly.commands;

import com.francobm.simplefly.SimpleFly;
import com.francobm.simplefly.utils.UtilsSF;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
public class Command implements CommandExecutor {
    private final SimpleFly plugin;

    public Command(SimpleFly plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender){
            if(args.length >= 1){
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.getSimpleFlyManager().reload();
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                plugin.getSimpleFlyManager().fly((ConsoleCommandSender) sender, target);
                return true;
            }
        }
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                plugin.getSimpleFlyManager().fly(player, plugin.getConfig().getBoolean("free-fly"));
            }
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.getSimpleFlyManager().reload(player);
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                plugin.getSimpleFlyManager().fly(player, target);
            }
        }
        return true;
    }
}
