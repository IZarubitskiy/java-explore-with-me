package ru.practicum.ewm.category.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryRequest;
import ru.practicum.ewm.category.dto.CategoryResponse;
import ru.practicum.ewm.category.service.CategoryService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.createCategory(categoryRequest);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse updateCategory(@Valid @RequestBody CategoryRequest request,
                                           @PathVariable(name = "catId") Long categoryId) {
        return categoryService.updateCategory(request, categoryId);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "catId") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }
}