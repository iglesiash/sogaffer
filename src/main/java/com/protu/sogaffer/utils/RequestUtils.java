package com.protu.sogaffer.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Service
@Data
public class RequestUtils {

  @Value("${graphql-url}")
  private String graphqlUrl;

  @Value("${api-key}")
  private String apiKey;

  @Value("${aud}")
  private String aud;

  private RestTemplate restTemplate;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public RequestUtils() {
    this.restTemplate = new RestTemplate();
  }

    /**
     * Makes a query request to Sorare's API
     * 
     * @param operationName: the operation's name
     * @param query:         the query
     * @param variables:     the variables in the query
     * @return the response
     */
    public ResponseEntity<String> callGraphQLService(String query,
            Map<String, Object> variables, String token) {

        // Create the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        if (variables != null) {
            requestBody.put("variables", variables);
        } // if

        // Create the request entity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, createHeaders(token));

        // Sends the request
        return restTemplate.postForEntity(graphqlUrl, requestEntity, String.class);
    } // callGraphQLService

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
        headers.set("APIKEY", apiKey);

        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        } // if

        headers.set("JWT-AUD", aud);

        return headers;
    } // createHeaders
}
