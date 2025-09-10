package com.harish.inshorts.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class CohereService {
    @Value("${cohere.api.url}")
    private String apiUrl;

    @Value("${cohere.api.key}")
    private String apiKey;

    public Map<String, Object> analyzeQuery(String query) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("model", "command-r-plus");
        body.put("message", "Extract entities, concepts, and intent from this query: " + query);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.err.println("HTTP Error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            System.err.println("Resource Access Error: " + ex.getMessage());
        } catch (RestClientException ex) {
            System.err.println("Rest Client Error: " + ex.getMessage());
        }

        return Collections.emptyMap();
    }


    public String summarizeArticle(String text) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("model", "command-r-plus");
        body.put("message", "Summarize this article in 2 lines: " + text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.get("text") != null) {
                return responseBody.get("text").toString();
            } else {
                System.err.println("Empty response from API");
                return "";
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.err.println("HTTP Error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            System.err.println("Resource Access Error: " + ex.getMessage());
        } catch (RestClientException ex) {
            System.err.println("Rest Client Error: " + ex.getMessage());
        }

        return "";
    }

}
