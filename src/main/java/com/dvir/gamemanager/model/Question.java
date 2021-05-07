package com.dvir.gamemanager.model;

import java.util.Map;

/**
 * Game's question
 */
public class Question {
    private long id;
    private String description;
    private int points;
    private Map<Long, Answer> answerIdToCorrectAnswer;
    private Map<Long, Answer> answerIdToIncorrectAnswers;

    public static final int INCORRECT_ANSWER_POINTS = 0;

    public Question(String description, int points) {
        this.description = description;
        this.points = points;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Long, Answer> getAnswerIdToCorrectAnswer() {
        return answerIdToCorrectAnswer;
    }

    public void setAnswerIdToCorrectAnswer(Map<Long, Answer> answerIdToCorrectAnswer) {
        this.answerIdToCorrectAnswer = answerIdToCorrectAnswer;
    }

    public Map<Long, Answer> getAnswerIdToIncorrectAnswers() {
        return answerIdToIncorrectAnswers;
    }

    public void setAnswerIdToIncorrectAnswers(Map<Long, Answer> answerIdToIncorrectAnswers) {
        this.answerIdToIncorrectAnswers = answerIdToIncorrectAnswers;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
