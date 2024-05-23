package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.errors.exception.EntityNotFoundException;
import ru.practicum.errors.exception.ValidationException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepositoryJpa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepositoryJpa userRepositoryJpa;

    @Transactional
    public UserDto userCreate(NewUserRequest newUserRequest) {
        try {
            User userToSave = UserMapper.toUser(newUserRequest);
            User user = userRepositoryJpa.save(userToSave);
            return UserMapper.toUserDto(user);
        } catch (Exception e) {
            throw new ValidationException("Ошибка создания user");
        }

    }

    public List<UserDto> userGetAll(List<Integer> ids, Integer from, Integer size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by(ASC, "id"));
        Page<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepositoryJpa.findAll(pageable);
        } else {
            users = userRepositoryJpa.findByIdIn(ids, pageable);
        }
        if (!users.hasContent()) {
            return new ArrayList<>();
        }
        return users.getContent().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Integer userId) {
        userRepositoryJpa.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Зарегистрированного пользователя не существует"));
        userRepositoryJpa.deleteById(userId);
    }

}
