package com.dvir.gamemanager.model.triviadb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * trivia DB root response
 */
public class TriviaRootResponse {
    private int response_code;
    @JsonProperty("results")
    private List<TriviaQuestionResponse> triviaQuestionResponseList;

    public TriviaRootResponse() {
    }

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public List<TriviaQuestionResponse> getTriviaQuestionResponseList() {
        return triviaQuestionResponseList;
    }

    public void setTriviaQuestionResponseList(List<TriviaQuestionResponse> triviaQuestionResponseList) {
        this.triviaQuestionResponseList = triviaQuestionResponseList;
    }
}
