package com.dvir.gamemanager.model.rest;

/**
 * General game exception wrapper
 */
public class GameException extends Exception {

    private String errorMessage;

    public GameException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * get exception description
     *
     * @return exception description
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * set exception description
     *
     * @param errorMessage description
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
