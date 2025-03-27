package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserCreateRequest;
import ru.practicum.ewm.user.dto.UserResponse;

import java.util.Collection;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest userCreateRequest);

    void deleteUserById(Long userId);

    Collection<UserResponse> getUsers(List<Long> userIds, int from, int size);
}
