package com.dvir.gamemanager.model.rest;

import java.util.List;

/**
 * Game's leaderboard response
 */
public class GameLeaderboardResponse {

    private long gameId;
    private List<PlayerScore> leaderboard;


    public GameLeaderboardResponse() {
    }

    public List<PlayerScore> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<PlayerScore> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public static class PlayerScore {
        private String player;
        private Long score;

        public PlayerScore(String player, Long score) {
            this.player = player;
            this.score = score;
        }

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        public Long getScore() {
            return score;
        }

        public void setScore(Long score) {
            this.score = score;
        }
    }
}
