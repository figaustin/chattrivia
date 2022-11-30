package com.etsuni.chattrivia;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("trivia")) {
                if(args[0].equalsIgnoreCase("start")) {
                    Trivia trivia = new Trivia();
                    trivia.triviaLoop();
                }
            }
        }

        return false;
    }
}
