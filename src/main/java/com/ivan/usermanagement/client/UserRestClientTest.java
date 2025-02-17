package com.ivan.usermanagement.client;

import com.ivan.usermanagement.dto.UserDto;

import java.time.LocalDate;
import java.util.List;

public class UserRestClientTest {

    public static void main(String[] args) {
        UserRestClient client = new UserRestClient();

        // 1️⃣ Create a new user
        UserDto newUser = new UserDto(null, "Emma", "Watson", "emma.watson@example.com", LocalDate.of(1990, 4, 15));
        UserDto createdUser = client.createUser(newUser);
        System.out.println("Created User: " + createdUser.getId() + " - " + createdUser.getFirstName() + " " + createdUser.getLastName());

        // 2️⃣ Get user by ID
        UserDto userById = client.getUserById(createdUser.getId());
        System.out.println("Fetched User: " + userById.getId() + " - " + userById.getFirstName() + " " + userById.getLastName());

        // 3️⃣ Get all users
        List<UserDto> allUsers = client.getAllUsers();
        System.out.println("All Users: " + allUsers.size());

        // 4️⃣ Update user
        createdUser.setLastName("Granger");
        createdUser.setEmail("emma.granger@example.com");
        UserDto updatedUser = client.updateUser(createdUser.getId(), createdUser);
        System.out.println("Updated User: " + updatedUser.getId() + " - " + updatedUser.getFirstName() + " " + updatedUser.getLastName());

        // 5️⃣ Delete user
        client.deleteUser(createdUser.getId());
        System.out.println("User deleted successfully!");
    }
}
