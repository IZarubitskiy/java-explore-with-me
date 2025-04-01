package ru.practicum.ewm.user.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exeption.exemptions.AlreadyExistsException;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.dto.UserCreateRequest;
import ru.practicum.ewm.user.dto.UserResponse;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        try {
            User user = userMapper.requestToUser(userCreateRequest);
            UserResponse userResponse = userMapper.userToResponse(userRepository.save(user));
            log.info("User with id={} was created", userResponse.getId());
            return userResponse;
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("users_email_key")) {
                log.warn("User with email '{}' already exists", userCreateRequest.getEmail());
                throw new AlreadyExistsException(String.format("User with email '%s' already exists", userCreateRequest.getEmail()));
            } else {
                throw e;
            }
        }
    }

    @Override
    public Collection<UserResponse> getUsers(List<Long> userIds, int from, int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        Page<User> page;
        if (userIds != null && !userIds.isEmpty()) {
            page = userRepository.findUsersByIdIn(userIds, pageable);
        } else {
            page = userRepository.findAll(pageable);
        }

        log.info("Get users with {ids, from, size} = ({}, {}, {})", userIds, from, size);
        return page.getContent().stream().map(userMapper::userToResponse).toList();
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
        log.info("User with id={} was deleted", userId);
    }

}