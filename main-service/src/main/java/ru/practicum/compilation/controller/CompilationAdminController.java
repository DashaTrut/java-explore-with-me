package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto compilationCreate(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Creating compilation admin{}", newCompilationDto);
        return compilationService.compilationCreateAdmin(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto compilationUpdate(@RequestBody @Valid UpdateCompilationRequest newCompilationDto, @PathVariable Integer compId) {
        log.info("Update compilation admin{}", newCompilationDto);
        return compilationService.compilationUpdateAdmin(newCompilationDto, compId);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer compId) {
        log.info("Delete compilation {}", compId);
        compilationService.deleteCompilationAdmin(compId);
    }
}
