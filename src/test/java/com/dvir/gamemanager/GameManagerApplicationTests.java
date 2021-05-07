package com.dvir.gamemanager;

import com.dvir.gamemanager.component.GameService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameManagerApplicationTests {

	private final static Logger logger = LoggerFactory.getLogger(GameManagerApplicationTests.class);
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	public static final String URL_CREATE_GAME = "/game";
	public static final String URL_ANSWER = "/answer";
	public static final String URL_LEADERBOARD = "/games/1/leaderboard";

	@Autowired
	private GameService gameService;

	@Autowired
	private MockMvc mvc;

	/**
	 * test generate game
	 *
	 * @throws Exception
	 */
	@Test
	public void getGenerateGame() throws Exception {
		logger.info("GameManagerApplicationTests.postGenerateGame");

		mvc.perform(get(URL_CREATE_GAME).contentType(APPLICATION_JSON_UTF8)
				.content(""))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"gameId\":")))
				.andExpect(content().string(containsString("\"questionId\":")));

	}

	/**
	 * test answer question right
	 *
	 * @throws Exception
	 */
	@Test
	public void postAnswerQuestionRight() throws Exception {
		logger.info("GameManagerApplicationTests.postAnswerQuestionRight");

		String rightAnswer = "{\"gameId\": 1, \"userName\" : \"user 2\", \"questionId\" : 1, \"answerId\" : 2}";
		mvc.perform(post(URL_ANSWER).contentType(APPLICATION_JSON_UTF8)
				.content(rightAnswer))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"Correct\"")));
	}


	/**
	 * test answer question wrong
	 *
	 * @throws Exception
	 */
	@Test
	public void postAnswerQuestionWrong() throws Exception {
		logger.info("GameManagerApplicationTests.postAnswerQuestionWrong");

		String wrongAnswer = "{\"gameId\": 1, \"userName\" : \"user 1\", \"questionId\" : 1, \"answerId\" : 3}";
		mvc.perform(post(URL_ANSWER).contentType(APPLICATION_JSON_UTF8)
				.content(wrongAnswer))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"Incorrect\"")));
	}

	/**
	 * test answer question wrong
	 *
	 * @throws Exception
	 */
	@Test
	public void getLeaderBoard() throws Exception {
		logger.info("GameManagerApplicationTests.getLeaderBoard");

		mvc.perform(get(URL_LEADERBOARD).contentType(APPLICATION_JSON_UTF8)
				.content(""))
				.andExpect(status().isOk())
				.andExpect(content().string(stringContainsInOrder("\"user 2\"", "\"user 1\"")));
	}

}
