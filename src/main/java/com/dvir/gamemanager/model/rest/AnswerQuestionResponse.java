package com.dvir.gamemanager.model.rest;

/**
 * Answer Question Response
 */
public class AnswerQuestionResponse {
    private AnswerStatusEnum answerStatus;
    private int points;

    public AnswerQuestionResponse(AnswerStatusEnum answerStatus, int points) {
        this.answerStatus = answerStatus;
        this.points = points;
    }

    public AnswerStatusEnum getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(AnswerStatusEnum answerStatus) {
        this.answerStatus = answerStatus;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
