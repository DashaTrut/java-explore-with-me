package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {
    private final CompilationService compilationService;


    @GetMapping("/{compId}")
    public CompilationDto compilationGet(@PathVariable @Positive Integer compId) {
        log.info("Get compilation {}", compId);
        return compilationService.compilationGet(compId);
    }

    @GetMapping()
    public List<CompilationDto> compilationGetAll(@RequestParam(required = false) Boolean pinned,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get All compilation");
        return compilationService.compilationGetAll(pinned, from, size);
    }
}
