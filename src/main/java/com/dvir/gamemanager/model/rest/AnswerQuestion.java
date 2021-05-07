package com.dvir.gamemanager.model.rest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Represent an answer to question in a game
 */
public class AnswerQuestion {
    @NotNull(message = "Game ID is mandatory")
    private Long gameId;
    @NotBlank(message = "Name is mandatory")
    private String userName;
    @NotNull(message = "Answer ID is mandatory")
    private Long answerId;
    @NotNull(message = "Question ID is mandatory")
    private Long questionId;

    public AnswerQuestion() {
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
