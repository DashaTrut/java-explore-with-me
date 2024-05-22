package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto userCreate(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Creating user {}", newUserRequest);
        return userService.userCreate(newUserRequest);
    }

    @GetMapping
    public List<UserDto> getAllUser(@RequestParam(required = false) List<Integer> ids,
                                    @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all user");
        return userService.userGetAll(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Integer userId) {
        log.info("Delete user {}", userId);
        userService.deleteUser(userId);
    }
}
