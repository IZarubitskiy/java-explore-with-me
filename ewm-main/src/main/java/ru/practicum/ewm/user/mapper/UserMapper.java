package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.user.dto.UserCreateRequest;
import ru.practicum.ewm.user.dto.UserResponse;
import ru.practicum.ewm.user.model.User;

@Mapper
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User requestToUser(UserCreateRequest createUserRequest);

    UserResponse userToResponse(User user);
}