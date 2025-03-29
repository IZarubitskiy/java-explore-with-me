package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryRequest;
import ru.practicum.ewm.category.dto.CategoryResponse;

import java.util.Collection;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryMergeRequest);

    Collection<CategoryResponse> getCategories(int from, int size);

    CategoryResponse updateCategory(CategoryRequest request, Long categoryId);

    CategoryResponse getCategoryById(Long categoryId);

    void deleteCategoryById(Long categoryId);
}