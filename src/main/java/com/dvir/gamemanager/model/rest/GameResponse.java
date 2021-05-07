package com.dvir.gamemanager.model.rest;

import java.util.List;

/**
 * Represents game exposed to client
 */
public class GameResponse {

    private final long gameId;
    private List<QuestionResponse> questions;

    public GameResponse(long gameId) {
        this.gameId = gameId;
    }

    public long getGameId() {
        return gameId;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }
}
