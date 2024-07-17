package com.example.BuildPC.mapper;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        if (user == null) return null;
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setEmail(user.getEmail());
        userDto.setAvatar(user.getAvatar());
        userDto.setPhone(user.getPhone());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        return userDto;
    }


}
