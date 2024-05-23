package ru.practicum.category.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Integer catId) {
        log.info("Get category {}", catId);
        return categoryService.categoryGet(catId);
    }

    @GetMapping
    public List<CategoryDto> getAllCategory(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all category");
        return categoryService.categoryGetAll(from, size);
    }
}
