package com.etsuni.chattrivia;

import java.util.ArrayList;
import java.util.List;

public class QuestionList {

    private Question question = new Question();
    private static QuestionList instance = new QuestionList();

    private QuestionList() {

    }

    public static QuestionList getInstance() {
        return instance;
    }

    public void newQuestion(Question newQ) {
        this.question = newQ;
    }

    public Question getQuestion() {
        return this.question;
    }


}
