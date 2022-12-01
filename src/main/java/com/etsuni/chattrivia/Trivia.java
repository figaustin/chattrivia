package com.etsuni.chattrivia;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.util.*;

import static com.etsuni.chattrivia.ChatTrivia.plugin;

public class Trivia {

    private int loopId;

    private int askId;

    private int currentCount = 0;

    private File triviaJSON;

    public Trivia() {
        triviaJSON = new File(ChatTrivia.plugin.getDataFolder(), "trivia.json");
    }

    public void triviaLoop() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        loopId = scheduler.scheduleSyncRepeatingTask(ChatTrivia.plugin, new Runnable() {
            @Override
            public void run() {
                QuestionList.getInstance().newQuestion(makeQuestion());
                ask(QuestionList.getInstance().getQuestion());
            }
        },0, plugin.getCustomConfig().getInt("questions.time_between_questions")
                + plugin.getCustomConfig().getInt("questions.round_length") + 100);
    }


    public void ask(Question question) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        broadcast(ChatColor.GOLD + "[" + ChatColor.GREEN + "TRIVIA" + ChatColor.GOLD + "] "
                + question.getQuestion());

        broadcast(ChatColor.GREEN + "A - " + question.getA());
        broadcast(ChatColor.GREEN + "B - " + question.getB());
        broadcast(ChatColor.GREEN + "C - " + question.getC());
        broadcast(ChatColor.GREEN + "D - " + question.getD());


        askId = scheduler.scheduleSyncRepeatingTask(ChatTrivia.plugin, new Runnable() {
            @Override
            public void run() {
                if(question.getAnswered()) {
                    question.reset();
                    question.setAcceptingAnswers(false);
                    currentCount = 0;
                    scheduler.cancelTask(askId);
                }else if(currentCount >= 600){
                    question.reset();
                    Objects.requireNonNull(question).setAcceptingAnswers(false);
                    broadcast(ChatColor.GOLD + "[" + ChatColor.GREEN + "TRIVIA" + ChatColor.GOLD + "] " +
                            "No one answered the question correctly!");
                    currentCount = 0;
                    scheduler.cancelTask(askId);
                } else{
                    currentCount++;
                }
            }
        },0, 0);

    }

    public Question makeQuestion(){
        Question newQuestion = new Question();
        try(Reader reader = new FileReader(triviaJSON)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JsonArray questions = new JsonParser().parse(reader).getAsJsonArray();
            Random random = new Random();
            int max = questions.size();
            newQuestion = gson.fromJson(questions.get(random.nextInt(max)).getAsJsonObject(), Question.class);

        } catch (IOException e){
            e.printStackTrace();
        }

        return newQuestion;
    }

    public static void broadcast(String message) {
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

}
