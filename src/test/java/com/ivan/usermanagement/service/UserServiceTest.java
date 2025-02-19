package com.ivan.usermanagement.service;

import com.ivan.usermanagement.dto.UserDto;
import com.ivan.usermanagement.entity.User;
import com.ivan.usermanagement.exception.ResourceNotFoundException;
import com.ivan.usermanagement.mapper.UserMapper;
import com.ivan.usermanagement.repository.UserRepository;
import com.ivan.usermanagement.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "Doe", "john.doe@example.com", LocalDate.of(1990, 5, 20));
        userDto = new UserDto(1L, "John", "Doe", "john.doe@example.com", LocalDate.of(1990, 5, 20));
    }

    // ✅ Test: Create User
    @Test
    void testCreateUser() {
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto savedUser = userService.createUser(userDto);

        assertNotNull(savedUser);
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        verify(userRepository, times(1)).save(user);
    }

    // ✅ Test: Get User by ID (Success)
    @Test
    void testGetUserById_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("John", foundUser.getFirstName());
        assertEquals("Doe", foundUser.getLastName());
    }

    // ❌ Test: Get User by ID (Not Found)
    @Test
    void testGetUserById_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    // ✅ Test: Get All Users
    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user);
        List<UserDto> userDtos = Arrays.asList(userDto);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    // ✅ Test: Update User (Success)
    @Test
    void testUpdateUser_WhenUserExists() {
        User updatedUser = new User(1L, "Jane", "Doe", "jane.doe@example.com", LocalDate.of(1995, 8, 15));
        UserDto updatedUserDto = new UserDto(1L, "Jane", "Doe", "jane.doe@example.com", LocalDate.of(1995, 8, 15));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // ✅ Use `any(User.class)` to avoid argument mismatch issue
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(updatedUserDto);

        UserDto result = userService.updateUser(1L, updatedUserDto);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        // ✅ Ensure repository interactions are correct
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }



    // ❌ Test: Update User (Not Found)
    @Test
    void testUpdateUser_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserDto updatedUserDto = new UserDto(1L, "Jane", "Doe", "jane.doe@example.com", LocalDate.of(1995, 8, 15));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(1L, updatedUserDto);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    // ✅ Test: Delete User (Success)
    @Test
    void testDeleteUser_WhenUserExists() {
        // Ensure user exists before deletion
        when(userRepository.existsById(1L)).thenReturn(true);

        // Perform delete operation
        userService.deleteUser(1L);

        // Verify delete was called once
        verify(userRepository, times(1)).deleteById(1L);
    }



    // ❌ Test: Delete User (Not Found)
    @Test
    void testDeleteUser_WhenUserNotFound() {

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }
}
