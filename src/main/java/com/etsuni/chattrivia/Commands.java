package com.etsuni.chattrivia;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import static com.etsuni.chattrivia.ChatTrivia.plugin;


public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("trivia")) {
                if(args[0].equalsIgnoreCase("reload")) {
                    if(!plugin.getCustomConfigFile().exists()) {
                        return false;
                    }
                    plugin.setCustomConfig(YamlConfiguration.loadConfiguration(plugin.getCustomConfigFile()));
                    sender.sendMessage(ChatColor.GOLD + "Reloaded ChatTrivia's configuration!");
                }
            }
        }

        return false;
    }


}
