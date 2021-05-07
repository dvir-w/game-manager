package com.dvir.gamemanager.restservice;

import com.dvir.gamemanager.component.GameService;
import com.dvir.gamemanager.model.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/**
 * Game Manger rest controller
 */
@RestController
public class GameController {
    private final static Logger logger = LoggerFactory.getLogger(GameController.class);

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * app health check
     *
     * @return
     */
    @GetMapping("/greeting")
    public String greeting() {
        logger.info("greeting");
        return "Helllo";
    }

    /**
     * create new game
     *
     * @return
     */
    @GetMapping("/game")
    public GameResponse createGame() {
        logger.info("createGame");
        return gameService.createGame();
    }

    /**
     * get game's leaderboard
     *
     * @return
     */
    @GetMapping("/games/{gameId}/leaderboard")
    public GameLeaderboardResponse getGameLeaderboard(@PathVariable Long gameId) {
        logger.info("getGameLeaderboard " + gameId);
        try {
            return gameService.getGameLeaderboard(gameId);
        } catch (GameException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getErrorMessage());
        }
    }

    /**
     * answer a game question
     *
     * @param answerQuestion answer question
     * @return answer question response
     */
    @PostMapping("/answer")
    public AnswerQuestionResponse answerQuestion(@RequestBody @Valid AnswerQuestion answerQuestion) {
        logger.info("answer of question " + answerQuestion.getQuestionId() + ", answer id " + answerQuestion.getAnswerId() + ", in game " + answerQuestion.getGameId() + ", by user " + answerQuestion.getUserName());
        try {
            return gameService.answer(answerQuestion);
        } catch (GameException e) {
            String errorMessage = e.getErrorMessage();
            HttpStatus status = HttpStatus.NOT_FOUND;
            if (gameService.PLAYER_ALREADY_ANSWERED_QUESTION.equals(errorMessage)) {
                status = HttpStatus.BAD_REQUEST;
            }
            throw new ResponseStatusException(status, errorMessage);
        }
    }
}
