package com.dvir.gamemanager.model.rest;

import com.dvir.gamemanager.model.Answer;

import java.util.List;

/**
 * Represents Question exposed to client
 */
public class QuestionResponse {
    private final Long questionId;
    private String description;
    private int points;
    private List<Answer> answers;

    public QuestionResponse(Long questionId) {
        this.questionId = questionId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
