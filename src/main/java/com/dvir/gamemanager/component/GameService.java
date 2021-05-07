package com.dvir.gamemanager.component;

import com.dvir.gamemanager.model.*;
import com.dvir.gamemanager.model.rest.*;
import com.dvir.gamemanager.model.triviadb.TriviaQuestionResponse;
import com.dvir.gamemanager.model.triviadb.TriviaRootResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Main model class
 * <br/> answer questions and leaderboards are done here
 */
@Service
public class GameService {
    private final static Logger logger = LoggerFactory.getLogger(GameService.class);

    public static final String GAME_NOT_FOUND = "Game not found";
    public static final String QUESTION_NOT_FOUND = "Question not found";
    public static final String ANSWER_NOT_FOUND = "Answer not found";
    public static final String PLAYER_ALREADY_ANSWERED_QUESTION = "Player already answered question";


    private ConcurrentHashMap<Long, Game> gameIdToGameMap;
    private ConcurrentHashMap<String, Player> userNameToPlayerMap;
    private AtomicLong gamesIdGenerator;
    private AtomicLong answerQuestionIdGenerator;
    private Random random = new Random();


    @PostConstruct
    public void init() {
        logger.info("GameService#init");
        gameIdToGameMap = new ConcurrentHashMap<>();
        userNameToPlayerMap = new ConcurrentHashMap<>();
        gamesIdGenerator = new AtomicLong(1);
        answerQuestionIdGenerator = new AtomicLong(1);
    }

    /**
     * answers a question by a user in game
     * <br/> finds or creates the game
     * <br/> finds or creates the user
     * <br/> answers a question
     *
     * @param answerQuestion answerQuestionData
     * @return answer question response
     * @throws GameException GAME_NOT_FOUND
     */
    public AnswerQuestionResponse answer(AnswerQuestion answerQuestion) throws GameException {
        Game game = gameIdToGameMap.get(answerQuestion.getGameId());
        if (game == null) {
            throw new GameException(GAME_NOT_FOUND);
        }
        String userName = answerQuestion.getUserName();
        Player player = userNameToPlayerMap.computeIfAbsent(userName, newPlayer -> new Player(userName));
        return answer(answerQuestion, game, player);
    }

    /**
     * find the question, answer it and return answer response
     *
     * @param answerQuestion answerQuestionData
     * @param game           game
     * @param player         player
     * @return answer question response
     * @throws GameException QUESTION_NOT_FOUND, ANSWER_NOT_FOUND
     */
    private AnswerQuestionResponse answer(AnswerQuestion answerQuestion, Game game, Player player) throws GameException {
        Long questionId = answerQuestion.getQuestionId();
        Question question = game.getQuestionIdToQuestion().get(questionId);
        if (question == null) {
            throw new GameException(QUESTION_NOT_FOUND);
        }
        Long answerId = answerQuestion.getAnswerId();
        Answer answer = question.getAnswerIdToCorrectAnswer().get(answerId);
        boolean isCorrectAnswer = answer != null;
        AnswerStatusEnum answerStatus = isCorrectAnswer ? AnswerStatusEnum.Correct : AnswerStatusEnum.Incorrect;
        int points = isCorrectAnswer ? question.getPoints() : Question.INCORRECT_ANSWER_POINTS;
        createPlayerAnswer(game, player, question, answer);
        updateGameLeaderBoard(game, player, points);
        if (!isCorrectAnswer) {
            boolean isIncorrectAnswer = question.getAnswerIdToIncorrectAnswers().containsKey(answerId);
            if (!isIncorrectAnswer) {
                throw new GameException(ANSWER_NOT_FOUND);
            }
        }

        return new AnswerQuestionResponse(answerStatus, points);
    }

    /**
     * creates player answers
     * <br> updates game with player question
     *
     * @param game     game
     * @param player   player
     * @param question question
     * @param answer   answer
     * @throws GameException PLAYER_ALREADY_ANSWERED_QUESTION
     */
    private void createPlayerAnswer(Game game, Player player, Question question, Answer answer) throws GameException {
        PlayerAnswer playerAnswer = new PlayerAnswer(player, question, answer);
        Set<PlayerAnswer> playerAnswers = game.getPlayerAnswers(player);
        if (playerAnswers == null) {
            playerAnswers = new HashSet<>();
            game.setPlayerAnswers(player, playerAnswers);
        }
        if (playerAnswers.contains(playerAnswer)) {//player already answered question
            throw new GameException(PLAYER_ALREADY_ANSWERED_QUESTION);
        }
        playerAnswers.add(playerAnswer);
    }

    /**
     * updates play score in game's leaderboard
     *
     * @param game   game
     * @param player player
     * @param points points
     */
    private void updateGameLeaderBoard(Game game, Player player, int points) {
        Map<Player, Long> leaderBoard = game.getLeaderBoard();
        leaderBoard.compute(player, (playerKey, prevScore) -> prevScore == null ? points : prevScore + points);
    }

    /**
     * call trivia DB to get questions and create a game from the response
     *
     * @param gameId game id
     * @return new game with questions
     */
    private Game createGame(Long gameId) {
        Game game = new Game(gameId);
        final String uri = "https://opentdb.com/api.php?amount={amount}&type={type}&encode={encode}";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("amount", "10");//todo const and enum
        params.put("type", "multiple");
        params.put("encode", "base64");
        TriviaRootResponse triviaRootResponse = restTemplate.getForObject(uri, TriviaRootResponse.class, params);
        gameIdToGameMap.put(gameId, game);
        return parseTriviaResponse(game, triviaRootResponse);
    }

    /**
     * parse game result from trivia db to game
     *
     * @param game               game
     * @param triviaRootResponse game result from trivia db
     * @return game from trivia game result
     */
    private Game parseTriviaResponse(Game game, TriviaRootResponse triviaRootResponse) {
        Map<Long, Question> questionIdToQuestion = createQuestions(triviaRootResponse);
        game.setQuestionIdToQuestion(questionIdToQuestion);
        return game;
    }

    /**
     * parse questions from trivia db to game
     *
     * @param triviaRootResponse game result from trivia db
     * @return question id to question map
     */
    private Map<Long, Question> createQuestions(TriviaRootResponse triviaRootResponse) {
        Map<Long, Question> questionIdToQuestion = new HashMap<>();
        for (TriviaQuestionResponse triviaQuestionResponse : triviaRootResponse.getTriviaQuestionResponseList()) {
            String decodedQuestion = new String(Base64.getDecoder().decode(triviaQuestionResponse.getQuestion()));

            Question question = new Question(decodedQuestion, random.nextInt(10) + 1);
            question.setId(answerQuestionIdGenerator.getAndIncrement());
            Map<Long, Answer> answerIdToCorrectAnswers = createAnswers(Arrays.asList(new String[]{triviaQuestionResponse.getCorrect_answer()}));
            question.setAnswerIdToCorrectAnswer(answerIdToCorrectAnswers);
            Map<Long, Answer> answerIdToIncorrectAnswers = createAnswers(triviaQuestionResponse.getIncorrect_answers());
            question.setAnswerIdToIncorrectAnswers(answerIdToIncorrectAnswers);
            questionIdToQuestion.put(question.getId(), question);
        }
        return questionIdToQuestion;
    }

    /**
     * parse answers list from trivia db
     *
     * @param answers answers from trivia db
     * @return answer id to answer map
     */
    private Map<Long, Answer> createAnswers(List<String> answers) {
        Map<Long, Answer> answerIdToAnswer = new HashMap<>();
        Answer answer;
        for (String answerDesc : answers) {
            String decodedAnswer = new String(Base64.getDecoder().decode(answerDesc));

            answer = new Answer(decodedAnswer);
            answer.setId(answerQuestionIdGenerator.getAndIncrement());
            answerIdToAnswer.put(answer.getId(), answer);
        }

        return answerIdToAnswer;
    }

    /**
     * create new game
     *
     * @return created game
     */
    public GameResponse createGame() {
        Game game = createGame(gamesIdGenerator.getAndIncrement());
        return createGameResponse(game);
    }

    /**
     * create game view for client
     *
     * @param game game
     * @return viewable game response
     */
    private GameResponse createGameResponse(Game game) {
        GameResponse gameResponse = new GameResponse(game.getId());
        List<QuestionResponse> questionResponses = createQuestionsResponse(game);
        gameResponse.setQuestions(questionResponses);
        return gameResponse;
    }

    /**
     * create questions view for client
     *
     * @param game game
     * @return viewable question response
     */
    private List<QuestionResponse> createQuestionsResponse(Game game) {
        List<QuestionResponse> questionResponses = new ArrayList<>();
        for (Question question : game.getQuestionIdToQuestion().values()) {
            QuestionResponse questionResponse = new QuestionResponse(question.getId());
            List<Answer> answers = new ArrayList<>();
            answers.addAll(question.getAnswerIdToCorrectAnswer().values());
            answers.addAll(question.getAnswerIdToIncorrectAnswers().values());
            questionResponse.setAnswers(answers);
            questionResponse.setDescription(question.getDescription());
            questionResponse.setPoints(question.getPoints());
            questionResponses.add(questionResponse);
        }

        return questionResponses;
    }

    /**
     * get game's leaderboard
     *
     * @param gameId game id
     * @return game's leader board response
     * @throws GameException GAME_NOT_FOUND
     */
    public GameLeaderboardResponse getGameLeaderboard(Long gameId) throws GameException {
        GameLeaderboardResponse leaderboardResponse = new GameLeaderboardResponse();
        leaderboardResponse.setGameId(gameId);
        Game game = gameIdToGameMap.get(gameId);
        if (game == null) {
            throw new GameException(GAME_NOT_FOUND);
        }
        //maps from game's leader board <Player,Score> to <Player's name, Score> in order
        List<GameLeaderboardResponse.PlayerScore> leaderboard = new ArrayList();
        game.getLeaderBoard().entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .forEach(entry -> leaderboard.add(new GameLeaderboardResponse.PlayerScore(entry.getKey().getName(), entry.getValue())));

        leaderboardResponse.setLeaderboard(leaderboard);
        return leaderboardResponse;
    }
}
