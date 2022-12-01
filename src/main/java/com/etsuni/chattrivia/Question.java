package com.etsuni.chattrivia;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.etsuni.chattrivia.ChatTrivia.getEconomy;
import static com.etsuni.chattrivia.ChatTrivia.plugin;


public class Question implements Listener {

    public String question;
    public String A;
    public String B;
    public String C;
    public String D;
    public String answer;

    private Boolean answered;
    private Boolean acceptingAnswers;

    private Map<UUID, Long> coolDownMap;
    private CoolDownManager coolDownManager;
    private String possibleAnswers[];

    public Question() {
        answered = false;
        acceptingAnswers = true;
        coolDownMap = new HashMap<>();
        coolDownManager = new CoolDownManager();
        possibleAnswers = new String[4];
        possibleAnswers[0] = "a";
        possibleAnswers[1] = "b";
        possibleAnswers[2] = "c";
        possibleAnswers[3] = "d";
    }


    @EventHandler
    public void onAnswer(AsyncPlayerChatEvent event) {
        if(!QuestionList.getInstance().getQuestion().getAcceptingAnswers()){
            return;
        }
        String msg = event.getMessage();
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getCustomConfig();
        Boolean foundChar = false;
        for(String str : possibleAnswers) {
            if(str.equalsIgnoreCase(msg)) {
                foundChar = true;
            }
        }
        if(!foundChar) {
            return;
        }

        if(!msg.equalsIgnoreCase(QuestionList.getInstance().getQuestion().getAnswer())) {
            if(config.getInt("cooldown") > -1) {
                coolDownManager.setCoolDown(player, coolDownMap, config.getInt("cooldown"));

            }
        }
        if(msg.equalsIgnoreCase(QuestionList.getInstance().getQuestion().getAnswer()) &&
                QuestionList.getInstance().getQuestion().getAcceptingAnswers()) {
            if(config.getInt("cooldown") > -1) {
                if(coolDownManager.setCoolDown(player, coolDownMap, config.getInt("cooldown"))) {
                    QuestionList.getInstance().getQuestion().setAcceptingAnswers(false);
                    QuestionList.getInstance().getQuestion().setAnswered(true);
                    giveRewards(player);
                }

            }
            else if(config.getInt("cooldown") == -1) {
                QuestionList.getInstance().getQuestion().setAcceptingAnswers(false);
                QuestionList.getInstance().getQuestion().setAnswered(true);
                giveRewards(player);
            }

        }

    }


    public void giveRewards(Player player) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        //This is here purely to make the broadcast message come after the player's message, without it, it will
        //appear before the message in the chat... don't know why.
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                FileConfiguration config = plugin.getCustomConfig();
                ItemStack item = config.getItemStack("rewards.win_amount_item");
                int winAmount = config.getInt("rewards.win_amount_money") > 0 ? config.getInt("rewards.win_amount_money"): 1;
                if(config.getBoolean("rewards.win_item") && config.getBoolean("rewards.win_money")) {
                    Trivia.broadcast(ChatColor.GOLD + "[" + ChatColor.GREEN + "TRIVIA" + ChatColor.GOLD + "] " +
                            ChatColor.RESET + player.getDisplayName() + ChatColor.GOLD + " has answered correctly and won $"
                            + winAmount + " and " + item.getAmount() + " " + item.getType().toString() + "!");
                    player.getInventory().addItem(item);
                    getEconomy().depositPlayer(player, winAmount);
                }
                else if(config.getBoolean("rewards.win_money") && !config.getBoolean("rewards.win_item")){
                    Trivia.broadcast(ChatColor.GOLD + "[" + ChatColor.GREEN + "TRIVIA" + ChatColor.GOLD + "] " +
                            ChatColor.RESET + player.getDisplayName() + ChatColor.GOLD + " has answered correctly and won $"
                            + winAmount + "!");
                    getEconomy().depositPlayer(player, winAmount);
                }
                else if(!config.getBoolean("rewards.win_money") && config.getBoolean("rewards.win_item")) {
                    Trivia.broadcast(ChatColor.GOLD + "[" + ChatColor.GREEN + "TRIVIA" + ChatColor.GOLD + "] " +
                            ChatColor.RESET + player.getDisplayName() + ChatColor.GOLD + " has answered correctly and won " +
                            item.getAmount() + " " + item.getType().toString() + "!");
                    player.getInventory().addItem(item);
                } else {
                    Trivia.broadcast(ChatColor.GOLD + "[" + ChatColor.GREEN + "TRIVIA" + ChatColor.GOLD + "] " +
                            ChatColor.RESET + player.getDisplayName() + ChatColor.GOLD + " has answered correctly!");
                }
            }
        }, 1);

    }

    public void reset() {
        coolDownMap.clear();
    }

    public Map<UUID, Long> getCoolDownMap() {
        return coolDownMap;
    }

    public void setCoolDownMap(Map<UUID, Long> coolDownMap) {
        this.coolDownMap = coolDownMap;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    public Boolean getAcceptingAnswers() {
        return acceptingAnswers;
    }

    public void setAcceptingAnswers(Boolean acceptingAnswers) {
        this.acceptingAnswers = acceptingAnswers;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
