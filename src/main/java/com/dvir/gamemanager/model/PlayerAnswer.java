package com.dvir.gamemanager.model;

/**
 * player's answer to a question
 */
public class PlayerAnswer {
    private Player player;
    private Question question;
    private Answer answer;

    public PlayerAnswer(Player player, Question question, Answer answer) {
        this.player = player;
        this.question = question;
        this.answer = answer;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerAnswer that = (PlayerAnswer) o;

        if (!player.equals(that.player)) return false;
        return question.equals(that.question);
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + question.hashCode();
        return result;
    }
}
