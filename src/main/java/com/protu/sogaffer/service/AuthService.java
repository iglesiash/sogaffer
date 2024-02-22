package com.protu.sogaffer.service;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.protu.sogaffer.dto.login.CredentialsRequestDto;
import com.protu.sogaffer.model.dto.LoginResponseDto;

@Service
public class AuthService {

  @Value("${login-url}")
  private String LOGIN_URL;

  @Value("${graphql-url}")
  private String GRAPHQL_URL;

  @Value("${api-key}")
  private String API_KEY;

  @Value("${aud}")
  private String AUD;

  private static final RestTemplate REST_TEMPLATE = new RestTemplate();

  public LoginResponseDto authenticate(CredentialsRequestDto credentials) {
    String email = credentials.getEmail();
    String password = credentials.getPassword();

    String salt = getUserSalt(email);
    String hashedPassword = getHashedPassword(password, salt);

    // TODO: find a better way to do this
    Map<String, Object> variables = new HashMap<>();
    Map<String, String> input = new HashMap<>();

    input.put("email", email);
    input.put("password", hashedPassword);
    variables.put("input", input);
    variables.put("aud", AUD);

    String query = """
        mutation SignInMutation($input: signInInput!, $aud:String!) {
            signIn(input: $input) {
              currentUser {
                slug
                apiKey
                profile {
                  clubName
                  pictureUrl
                }
                jwtToken(aud: $aud) {
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

    return callGraphQLService(query, variables, null);
  }

  /**
   * Makes a query request to Sorare's API
   * 
   * @param operationName: the operation's name
   * @param query:         the query
   * @param variables:     the variables in the query
   * @return the response
   */
  private <T> T callGraphQLService(String query,
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
    return (T) REST_TEMPLATE.postForEntity(GRAPHQL_URL, requestEntity, responseClass).getBody();
  }

  /**
   * Returns the salt associated with a user's email
   *
   * @param email: the email of the user whose salt is to be retrieved
   * @return the salt associated with the user's email; null if the salt cannot be
   *         retrieved
   */
  private String getUserSalt(String email) {
    String salt = null;

    // Sends a GET request to retrieve the user's salt from Sorare's login service
    ResponseEntity<Map<String, Object>> response = REST_TEMPLATE.exchange(
        LOGIN_URL + email,
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
  } // getPasswordSalt

  /**
   * Hashes a password with a given salt
   */
  private String getHashedPassword(String password, String salt) {
    return BCrypt.hashpw(password, salt);
  } // hashPassword

  /**
   * Prepares the headers for the requests which go to Sorare's API
   * 
   * @param token: the token to be set on the Authorization: Bearer field, in case
   *               it exists (will be null when trying to login)
   * @return the headers with the information ready to be sent
   */
  private HttpHeaders createHeaders(String token) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("APIKEY", API_KEY);

    if (token != null && !token.isEmpty()) {
      headers.setBearerAuth(token);
    } // if

    headers.set("JWT-AUD", AUD);

    return headers;
  } // createHeaders
} // AuthService