package ru.practicum.ewm.user.utils;


import ru.practicum.ewm.user.model.User;

public interface UserSearchUtil {
    User findUserById(Long userId);
}