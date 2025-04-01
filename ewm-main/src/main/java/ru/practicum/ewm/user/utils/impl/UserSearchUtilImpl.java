package ru.practicum.ewm.user.utils.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.exeption.exemptions.NotFoundException;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.utils.UserSearchUtil;


@Slf4j
@Component
@AllArgsConstructor
public class UserSearchUtilImpl implements UserSearchUtil {
    private final UserRepository userRepository;


    @Override
    public User getById(Long userId) {
        log.info("Searching User with id={}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", userId)));
    }
}
