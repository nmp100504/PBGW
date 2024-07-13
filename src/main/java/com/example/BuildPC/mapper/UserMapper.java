package com.example.BuildPC.mapper;

import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        if (user == null) return null;
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
