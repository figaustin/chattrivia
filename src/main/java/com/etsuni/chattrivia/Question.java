package com.etsuni.chattrivia;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


import static com.etsuni.chattrivia.ChatTrivia.currentQuestion;

public class Question implements Listener {

    public String question;
    public String A;
    public String B;
    public String C;
    public String D;
    public String answer;

    public Boolean answered;
    public Boolean acceptingAnswers;

    public Question() {
        answered = false;
        acceptingAnswers = true;
        Bukkit.broadcastMessage("Made new question");
    }


    @EventHandler
    public void onAnswer(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();
        Bukkit.broadcastMessage("Listening to player chat - message = " + msg);
        Bukkit.broadcastMessage("Listening to player chat - correctAnswer = " + currentQuestion.getAnswer());
        Bukkit.broadcastMessage("Accepting answers?: " + currentQuestion.getAcceptingAnswers().toString());
        Bukkit.broadcastMessage("Answered?" + currentQuestion.getAnswered().toString());

        if(msg.equalsIgnoreCase(currentQuestion.getAnswer()) && acceptingAnswers) {
            acceptingAnswers = false;
            currentQuestion.setAnswered(true);
            giveRewards(event.getPlayer());
        }

    }


    public void giveRewards(Player player) {

        Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.GREEN + "TRIVIA" + ChatColor.GOLD + "] " +
                ChatColor.RESET + player.getDisplayName() + ChatColor.GOLD + " has answered correctly and won "
        + ChatTrivia.plugin.getCustomConfig().getString("win_amount_money"));
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
