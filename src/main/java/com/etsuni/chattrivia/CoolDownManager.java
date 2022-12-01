package com.etsuni.chattrivia;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class CoolDownManager {

    public Boolean setCoolDown(Player player, Map<UUID, Long> coolDownMap, Integer cooldownTime){
        UUID playerUUID = player.getUniqueId();
        if(coolDownMap.containsKey(playerUUID)){
            if(coolDownMap.get(playerUUID) > System.currentTimeMillis()) {
                long timeLeft = (coolDownMap.get(playerUUID) - System.currentTimeMillis()) / 1000;
                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.GREEN + "TRIVIA" + ChatColor.GOLD + "] "
                + "You can not answer this question again for another " + timeLeft + " seconds!");
                return false;
            }
        }
        coolDownMap.put(playerUUID, System.currentTimeMillis() + (cooldownTime * 1000));
        return true;
    }
}
