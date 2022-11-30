package com.etsuni.chattrivia;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.util.*;

import static com.etsuni.chattrivia.ChatTrivia.currentQuestion;

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
        Bukkit.broadcastMessage("ENTERING LOOP");

        loopId = scheduler.scheduleSyncRepeatingTask(ChatTrivia.plugin, new Runnable() {
            @Override
            public void run() {
                if(currentQuestion == null) {
                    makeQuestion();
                    ask(currentQuestion);
                }
            }
        },0, 100);
    }


    public void ask(Question question) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.GREEN + "TRIVIA" + ChatColor.GOLD + "] "
                + question.getQuestion());

        Bukkit.broadcastMessage(ChatColor.GREEN + "A - " + question.getA());
        Bukkit.broadcastMessage(ChatColor.GREEN + "B - " + question.getB());
        Bukkit.broadcastMessage(ChatColor.GREEN + "C - " + question.getC());
        Bukkit.broadcastMessage(ChatColor.GREEN + "D - " + question.getD());


        askId = scheduler.scheduleSyncRepeatingTask(ChatTrivia.plugin, new Runnable() {
            @Override
            public void run() {
                if(currentQuestion != null && currentQuestion.getAnswered()) {
                    currentQuestion.setAcceptingAnswers(false);
                    scheduler.cancelTask(askId);
                    delay();
                }else if(currentCount >= 600){
                    Objects.requireNonNull(currentQuestion).setAcceptingAnswers(false);
                    Bukkit.broadcastMessage("NO ONE ANSWERED CORRECTLY");
                    currentCount = 0;
                    delay();
                    scheduler.cancelTask(askId);
                } else{
                    currentCount++;
                }
            }
        },0, 0);

    }

    public void delay() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        scheduler.scheduleSyncDelayedTask(ChatTrivia.plugin, new Runnable() {
            @Override
            public void run() {
                currentQuestion = null;
            }
        }, 600);
    }

    public void makeQuestion(){

        try(Reader reader = new FileReader(triviaJSON)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JsonArray questions = new JsonParser().parse(reader).getAsJsonArray();
            Random random = new Random();
            int max = questions.size();
            currentQuestion = gson.fromJson(questions.get(random.nextInt(max)).getAsJsonObject(), Question.class);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
