package com.dvir.gamemanager.model;

/**
 * answer of a game's question
 */
public class Answer {
    private long id;
    private String description;

    public Answer(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
