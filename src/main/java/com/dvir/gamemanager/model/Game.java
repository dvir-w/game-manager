package com.dvir.gamemanager.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a game
 */
public class Game {

    private final long id;
    private Map<Long, Question> questionIdToQuestion = new HashMap<>();
    private Set<Player> players = new HashSet<>();
    private Map<Player, Set<PlayerAnswer>> playersAnswers = new HashMap<>();
    //player to his score
    private Map<Player, Long> leaderBoard = new ConcurrentHashMap<>();

    public Game(long id) {
        this.id = id;

    }

    public long getId() {
        return id;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<PlayerAnswer> getPlayerAnswers(Player player) {
        return playersAnswers.get(player);
    }

    public void setPlayerAnswers(Player player, Set<PlayerAnswer> playersAnswers) {
        this.playersAnswers.put(player, playersAnswers);
    }

    public Map<Long, Question> getQuestionIdToQuestion() {
        return questionIdToQuestion;
    }

    public void setQuestionIdToQuestion(Map<Long, Question> questionIdToQuestion) {
        this.questionIdToQuestion = questionIdToQuestion;
    }

    public Map<Player, Long> getLeaderBoard() {
        return leaderBoard;
    }
}
