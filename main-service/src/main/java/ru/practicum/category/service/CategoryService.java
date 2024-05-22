package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepositoryJpa;
import ru.practicum.errors.exception.EntityNotFoundException;
import ru.practicum.errors.exception.ValidationException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepositoryJpa categoryRepositoryJpa;

    @Transactional
    public CategoryDto categoryCreate(NewCategoryDto newCategoryDto) {
        try {
            Category category = categoryRepositoryJpa.save(CategoryMapper.toCategory(newCategoryDto));
            return CategoryMapper.toCategoryDto(category);
        } catch (Exception e) {
            throw new ValidationException("Ошибка создания категории");
        }
    }

    @Transactional
    public CategoryDto categoryGet(int categoryId) {
        Category category = categoryRepositoryJpa.findById(categoryId).orElseThrow(() ->
                new EntityNotFoundException("Категории не существует"));
        return CategoryMapper.toCategoryDto(category);
    }

    @Transactional
    public CategoryDto categoryUpdate(Integer catId, NewCategoryDto categoryDto) {
        try {
            Category category = categoryExists(catId);
            category.setName(categoryDto.getName());
            Category categoryResponse = categoryRepositoryJpa.save(category);
            return CategoryMapper.toCategoryDto(categoryResponse);
        } catch (Exception e) {
            throw new ValidationException("Ошибка изменения категории");
        }
    }

    @Transactional
    public void deleteCategory(Integer catId) {
        try {
            categoryExists(catId);
            categoryRepositoryJpa.deleteById(catId);
        } catch (Exception e) {
            throw new ValidationException("Ошибка создания категории");
        }
    }

    @GetMapping
    public List<CategoryDto> categoryGetAll(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by(ASC, "id"));
        Page<Category> categories = categoryRepositoryJpa.findAll(pageable);
        if (!categories.hasContent()) {
            return new ArrayList<>();
        }
        return categories.getContent().stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }


    public Category categoryExists(int categoryId) {
        return categoryRepositoryJpa.findById(categoryId).orElseThrow(() ->
                new ValidationException("Категории не существует"));
    }
}
