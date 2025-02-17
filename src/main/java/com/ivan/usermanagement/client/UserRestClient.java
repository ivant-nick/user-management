package com.ivan.usermanagement.client;

import com.ivan.usermanagement.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

public class UserRestClient {

    private final WebClient webClient;

    public UserRestClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/api/users")
                .build();
    }

    /**
     * Create a new user
     */
    public UserDto createUser(UserDto userDto) {
        return webClient.post()
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    /**
     * Get user by ID
     */
    public UserDto getUserById(Long userId) {
        try {
            return webClient.get()
                    .uri("/{id}", userId)
                    .retrieve()
                    .bodyToMono(UserDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("User not found with ID: " + userId);
            }
            throw e;
        }
    }

    /**
     * Get all users
     */
    public List<UserDto> getAllUsers() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(UserDto.class)
                .collectList()
                .block();
    }

    /**
     * Update a user
     */
    public UserDto updateUser(Long userId, UserDto userDto) {
        return webClient.put()
                .uri("/{id}", userId)
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    /**
     * Delete a user
     */
    public void deleteUser(Long userId) {
        webClient.delete()
                .uri("/{id}", userId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
