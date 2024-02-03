package com.protu.sogaffer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protu.sogaffer.model.dto.LoginResponseDto;
import com.protu.sogaffer.model.dto.ResponseDto;

@SpringBootApplication
public class SogafferApplication {

	private static final String URI_SALT = "https://api.sorare.com/api/v1/users/";
	private static final String URI_BASE = "https://api.sorare.com/federation/graphql";
	private static String email;

	@Value("${api.key}")
	private static String apiKey;

	private static final RestTemplate REST_TEMPLATE = new RestTemplate();

	public static void main(String[] args) {

		String token = null;
		String query;
		String playerSlug = "francisco-roman-alarcon-suarez";
		String teamSlug = "real-betis-sevilla";

		/*
		 ****************************************************************************************** 
		 ************************************* SIGN IN ********************************************
		 ****************************************************************************************** 
		 */

		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter email: ");
		email = scanner.nextLine();

		System.out.print("Enter password for " + email + ": ");

		String password = scanner.nextLine();
		scanner.close();

		String salt = getSalt();
		String hashedPassword = hashPassword(password, salt);

		Map<String, Object> variables = new HashMap<>();
		Map<String, String> input = new HashMap<>();

		input.put("email", email);
		input.put("password", hashedPassword);
		variables.put("input", input);

		query = """
					mutation SignInMutation($input: signInInput!) {
						signIn(input: $input) {
						  currentUser {
							slug
							trophies {
							  cardRewards
							  finalRankings
							  podiumRankings
							  so5TournamentType {
								displayName
								id
								so5LeaderboardType
								svgLogoUrl
							  }
							}
							blockedUntil
							jwtToken(aud: "Sogaffer") {
							  token
							  expiredAt
							}
						  }
						  otpSessionChallenge
						  tcuToken
						  errors {
							message
						  }
						}
					  }

				""";

		LoginResponseDto loginResponseDto = callGraphQLService(query, variables, token);
		writeObjectToFile(loginResponseDto, "sign_in.json");
		token = loginResponseDto.getData().getSignIn().getCurrentUser().getJwtToken().getToken();

		/*
		 ****************************************************************************************** 
		 ******************************** GET PLAYER INFO *****************************************
		 ****************************************************************************************** 
		 */

		query = """
				query getPlayerInfo($slug: String!) {
					football {
					  player(slug: $slug) {
						firstName
						lastName
						displayName
						birthDate
						age
						position
						activeClub {
						  code
						  name
						  domesticLeague {
							displayName
						  }
						  upcomingGames(first: 2) {
							date
							venue
							homeTeam {
							  ... on Club {
								name
							  }
							  ... on NationalTeam {
								name
							  }
							}
							awayTeam {
							  ... on Club {
								name
							  }
							  ... on NationalTeam {
								name
							  }
							}
						  }
						}
						country {
						  threeLetterCode
						  flagUrl
						}
						shirtNumber
						bestFoot
						height
						weight
						playingStatus
						suspensions {
						  active
						  startDate
						  endDate
						  reason
						}
						injuries {
						  active
						  startDate
						  expectedEndDate
						}
						pictureUrl
					  }
					}
				  }
											""";

		variables = new HashMap<>();
		variables.put("slug", playerSlug);

		Object playerInfoResponse = callGraphQLService(query, variables, token);
		writeObjectToFile(playerInfoResponse, "player_info_" + playerSlug + ".json");

		/*
		 ****************************************************************************************** 
		 ********************************* GET PLAYER STATS ***************************************
		 ****************************************************************************************** 
		 */
		query = """
				query getPlayerStats($slug: String!, $season: Int!) {
					football {
					  player(slug: $slug) {
						stats(seasonStartYear: $season) {
						  appearances
						  substituteOut
						  substituteIn
						  goals
						  assists
						  minutesPlayed
						  yellowCards
						  redCards
						}


						allSo5Scores(first: 20) {
						  nodes {
							playerGameStats {
							  accuratePass
							  bigChanceCreated
							  cleanSheet
							  crossAccuracy
							  duelWon
							  effectiveClearance
							  fieldStatus
							  formationPlace
							  fouls
							  goalAssist
							  goalKicks
							  goals
							  goalsConceded

							  interceptionWon
							  lostCorners
							  minsPlayed
							  ontargetScoringAtt
							  ownGoals
							  parries
							  passAccuracy
							  penaltiesSaved
							  penaltyKickMissed
							  penaltySave
							  redCard
							  saves
							  shotAccuracy
							  shotEfficiency
							  singleGoalGame
							  totalClearance
							  totalPass
							  totalScoringAtt
							  totalTackle
							  wasFouled
							  wonContest
							  wonTackle
							  yellowCard
							}

							allAroundStats {
							  category
							  points
							  stat
							  statValue
							  totalScore
							}
							score
							game {
							  date
							  homeTeam {
								... on Club {
								  name
								}
								... on NationalTeam {
								  name
								}
							  }
							}
						  }
						}
					  }
					}
				  }
						""";

		variables = new HashMap<>();
		variables.put("slug", playerSlug);
		variables.put("season", 2023);

		Object playerBasicStats = callGraphQLService(query, variables, token);
		writeObjectToFile(playerBasicStats, "player_stats_" + playerSlug + ".json");

		/*
		 ****************************************************************************************** 
		 ************************** GET PLAYER POINTS EVOLUTION **********************************
		 ****************************************************************************************** 
		 */

		query = """
				query getPlayerPoints($slug: String!) {
					football {
					  player(slug:$slug) {
						allSo5Scores(first: 20){
						  nodes {
							score
							game {
							  date
							  homeTeam {
								...on Club {
								  name
								}
							  }
							  awayTeam {
								...on Club {
								  name
								}
							  }
							}
						  }
						}
					  }
					}
				  }
						""";

		variables = new HashMap<>();
		variables.put("slug", playerSlug);

		Object playerPointsEvolution = callGraphQLService(query, variables, token);
		writeObjectToFile(playerPointsEvolution, "player_points_evolution_" + playerSlug + ".json");

		/*
		 ****************************************************************************************** 
		 *********************************** GET USER CARDS ***************************************
		 ****************************************************************************************** 
		 */

		String username = loginResponseDto.getData().getSignIn().getCurrentUser().getSlug();

		variables = new HashMap<>();
		variables.put("username", username);
		query = """
				query getUserCards($username: String!) {
					user(slug: $username) {
					  footballCards {
						edges {
						  node {
							grade
							last_15: averageScore(type: LAST_FIFTEEN_SO5_AVERAGE_SCORE)
							last_5: averageScore(type: LAST_FIVE_SO5_AVERAGE_SCORE)
							positionTyped
							tradeableStatus
							seasonYear
							player {
							  activeClub {
								name
								domesticLeague {
								  displayName
								}
							  }
							  stats(seasonStartYear: 2023) {
								appearances
							  }
							  country {
								threeLetterCode
							  }
							}
						  }
						}
						pageInfo {
						  endCursor
						  hasNextPage
						}
					  }
					}
				  }

							""";

		Object userCards = callGraphQLService(query, variables, token);
		writeObjectToFile(userCards, "user_cards_" + username + ".json");

		/*
		 ****************************************************************************************** 
		 ********************************** GET ALL PLAYERS ***************************************
		 ****************************************************************************************** 
		 */

		query = """
				query getAllPlayers {
					football {
					  allCards(first: 100) {
						nodes {
						  rarity
						  name
						  player {
							displayName
							age

							activeClub {
							  name
							  domesticLeague {
								displayName
							  }
							  upcomingGames(first: 1){
								awayTeam {
								  ...on Club {
									name
								  }
								}
								homeTeam {
								  ...on Club {
									name
								  }
								}
							  }
							}
							position
							country {
							  threeLetterCode
							  flagUrl
							}
							averageScore(type: LAST_FIFTEEN_SO5_AVERAGE_SCORE)
							injuries {
							  active
							  startDate
							  expectedEndDate
							  kind
							}
							suspensions {
							  active
							  startDate
							  endDate
							  reason
							}
						  }
						}
						pageInfo {
						  endCursor
						  hasNextPage
						}
					  }
					}
				  }
						""";

		Object allPlayers = callGraphQLService(query, variables, token);
		writeObjectToFile(allPlayers, "all_players.json");

		/*
		 ****************************************************************************************** 
		 ***************************** GET PAST LINEUP RESULTS ************************************
		 ****************************************************************************************** 
		 */

		query = """
				query getPlayerResults {
					football {
					  so5 {
						myOngoingAndRecentSo5Lineups {
						  so5Fixture {
							gameWeek
							startDate
							endDate
							shortDisplayName
						  }
						  so5Appearances {
							card {
							  slug
							  positionTyped
							  player {
								displayName
							  }
							  seasonYear
							}
							so5Score {
							 score
							}
							captain
						  }
						  so5Leaderboard {
							displayName
							so5LineupsCount
						  }

						  so5Rankings {
							score
							ranking
							eligibleRewards {
							  cards {
								player {
								  displayName
								}
							  }
							}
							so5Rewards {
							  coinAmount
							  aasmState
							}
						  }
						}
					  }
					}
				  }
							""";

		Object pastLineupResults = callGraphQLService(query, variables, token);
		writeObjectToFile(pastLineupResults, "past_lineup_results.json");

		/*
		 ****************************************************************************************** 
		 ********************************** GET TEAM STATS ****************************************
		 ****************************************************************************************** 
		 */

		/**
		 * It's not as easy as getting all stats for each player, as there are some
		 * players who are on loan and whose current team is not the club they belong
		 * to, but the club they're on loan to. This means that we'll have to get the
		 * stats for each match the team has played on the specified season, and them
		 * sum them up for each player that participated in that match
		 */

		// Firstly, the games for that team are fetched
		query = """
				query getTeamGames($slug: String!) {
					football {
					  club(slug: $slug) {
						name
						latestGames(first: 1000) {
						  nodes {
							id
							date # only used to check a likely first-last confusion in order
						  }
						}
					  }
					}
				  }

							""";

		variables = new HashMap<>();
		variables.put("slug", teamSlug);

		Object teamGames = callGraphQLService(query, variables, token);
		writeObjectToFile(teamGames, "past_games_" + teamSlug + ".json");

		/**
		 * XXX
		 * From this point forward, we'll suppose that the previous query has been
		 * filtered and only shows the games for the specified season
		 */

		String gameId = "1a02380f-230d-4c4e-8539-05a0f64f5fd5";

		query = """
				query getGamePlayers($id: ID!) {
					football {
					  game(id: $id) {
						homeTeam {
						  ... on Club {
							name
						  }
						}
						awayTeam {
						  ... on Club {
							name
						  }
						}
						awayFormation {
						  startingLineup {
							slug
							currentSeason {
							  startYear
							}
							activeClub {
							  name
							}
							so5Score(gameId: $id) {
							  score
							  allAroundStats {
								points
								stat
								statValue
							  }
							  playerGameStats {
								accuratePass
								duelWon
							  }
							}
						  }
						}
					  }
					}
				  }
								""";

		variables = new HashMap<>();
		variables.put("id", gameId);

		Object gameStats = callGraphQLService(query, variables, token);
		writeObjectToFile(gameStats, "game_stats_" + gameId + ".json");

		/*
		 ****************************************************************************************** 
		 ***************************** GET PLAYER PRICE EVOLUTION *********************************
		 ****************************************************************************************** 
		 */
		query = """
				query getPlayerEvolution($slug:String!) {
					football {
					  player(slug:$slug) {
						displayName
						pastGames(last:1000) {
						  nodes {
							date
							so5Score(playerSlug:$slug) {
							  score
							}
						  }
						  pageInfo {
							endCursor
							hasPreviousPage
						  }
						}
					  }
					}
				  }
					""";
		variables = new HashMap<>();
		variables.put("slug", playerSlug);

		Object getPlayerValueEvolution = callGraphQLService(query, variables, token);
		writeObjectToFile(getPlayerValueEvolution, "player_value_evolution_" + playerSlug + ".json");

	} // main

	private static String getSalt() {
		String salt = null;
		ResponseEntity<Map<String, Object>> response = REST_TEMPLATE.exchange(
				URI_SALT + email,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<Map<String, Object>>() {
				});
		Map<String, Object> body = response.getBody();

		// Body might be null
		if (body != null) {
			salt = body.get("salt").toString();
		} // if

		return salt;
	} // getSalt

	/**
	 * Hashes a password with a given salt
	 */
	private static String hashPassword(String password, String salt) {
		return BCrypt.hashpw(password, salt);
	} // hashPassword

	/**
	 * Makes a query-request to Sorare's API
	 * 
	 * @param operationName: the operation's name
	 * @param query:         the query
	 * @param variables:     the variables in the query
	 * @return the response
	 */
	private static <T> T callGraphQLService(String query,
			Map<String, Object> variables, String token) {

		// Create the request body
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("query", query);

		if (variables != null) {
			requestBody.put("variables", variables);
		} // if

		// Create the request entity
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, createHeaders(token));

		Class<?> responseClass = Object.class;
		if (query.contains("signIn")) {
			responseClass = LoginResponseDto.class;
		} // if

		// Sends the request
		return (T) REST_TEMPLATE.postForEntity(URI_BASE, requestEntity, responseClass).getBody();
	}

	private static HttpHeaders createHeaders(String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("APIKEY", apiKey);

		if (token != null && !token.isEmpty()) {
			headers.setBearerAuth(token);
		} // if
		headers.set("JWT-AUD", "Sogaffer");

		return headers;
	} // createHeaders

	private static void writeObjectToFile(Object object, String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		try {

			ResponseDto responseDto = new ResponseDto();
			responseDto.setData(object);
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/" + fileName), responseDto);
			System.out.println("Data successfully written in " + fileName);
		} catch (IOException e) {
			System.err.println("Error reading file " + fileName);
		} // try-catch
	}
} // DemoApplication