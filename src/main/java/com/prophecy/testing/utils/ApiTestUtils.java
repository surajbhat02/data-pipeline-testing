package com.prophecy.testing.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * Utility class for API testing operations
 */
public class ApiTestUtils {
    private static final Logger logger = LoggerFactory.getLogger(ApiTestUtils.class);
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Send GET request to the specified URL
     */
    public static HttpResponse<String> sendGetRequest(String url, Map<String, String> headers) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .GET();
            
            // Add headers if provided
            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }
            
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            logger.info("GET request sent to: {} - Status: {}", url, response.statusCode());
            return response;
            
        } catch (Exception e) {
            logger.error("Error sending GET request to {}: {}", url, e.getMessage());
            throw new RuntimeException("Failed to send GET request", e);
        }
    }
    
    /**
     * Send POST request with JSON body
     */
    public static HttpResponse<String> sendPostRequest(String url, String jsonBody, Map<String, String> headers) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));
            
            // Add additional headers if provided
            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }
            
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            logger.info("POST request sent to: {} - Status: {}", url, response.statusCode());
            return response;
            
        } catch (Exception e) {
            logger.error("Error sending POST request to {}: {}", url, e.getMessage());
            throw new RuntimeException("Failed to send POST request", e);
        }
    }
    
    /**
     * Parse JSON response to JsonNode
     */
    public static JsonNode parseJsonResponse(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (IOException e) {
            logger.error("Error parsing JSON response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }
    
    /**
     * Validate response status code
     */
    public static boolean isSuccessfulResponse(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }
    
    /**
     * Extract value from JSON response using JSON path
     */
    public static String extractJsonValue(JsonNode jsonNode, String path) {
        try {
            String[] pathParts = path.split("\\.");
            JsonNode currentNode = jsonNode;
            
            for (String part : pathParts) {
                if (currentNode.has(part)) {
                    currentNode = currentNode.get(part);
                } else {
                    logger.warn("Path '{}' not found in JSON response", path);
                    return null;
                }
            }
            
            return currentNode.asText();
        } catch (Exception e) {
            logger.error("Error extracting value from JSON path '{}': {}", path, e.getMessage());
            return null;
        }
    }
    
    /**
     * Wait for API endpoint to be available
     */
    public static boolean waitForApiAvailability(String url, int maxRetries, int retryIntervalSeconds) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                HttpResponse<String> response = sendGetRequest(url, null);
                if (isSuccessfulResponse(response.statusCode())) {
                    logger.info("API endpoint {} is available", url);
                    return true;
                }
            } catch (Exception e) {
                logger.debug("API endpoint {} not available yet, attempt {}/{}", url, i + 1, maxRetries);
            }
            
            try {
                Thread.sleep(retryIntervalSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        logger.error("API endpoint {} is not available after {} retries", url, maxRetries);
        return false;
    }
}