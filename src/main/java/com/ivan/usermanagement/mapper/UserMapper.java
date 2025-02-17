package com.ivan.usermanagement.mapper;


import com.ivan.usermanagement.dto.UserDto;
import com.ivan.usermanagement.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDateOfBirth()
        );
    }

    public User toEntity(UserDto dto) {
        return new User(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getDateOfBirth()
        );
    }
}
