package ru.practicum.ewm.category.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryRequest;
import ru.practicum.ewm.category.dto.CategoryResponse;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.exeption.exemptions.DuplicationException;
import ru.practicum.ewm.exeption.exemptions.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        try {
            Category category = categoryMapper.requestToCategory(categoryRequest);
            CategoryResponse categoryResponse = categoryMapper.categoryToResponse(categoryRepository.save(category));
            log.info("Category with id={} was created", categoryResponse.getId());
            return categoryResponse;
        } catch (DataIntegrityViolationException e) {
            throw checkUniqueConstraint(e, categoryRequest.getName());
        }
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        log.info("Get category with id={}", categoryId);
        return categoryMapper.categoryToResponse(findById(categoryId));
    }

    @Override
    public Collection<CategoryResponse> getCategories(int from, int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        Page<Category> page = categoryRepository.findAll(pageable);

        log.info("Get users with {from, size} = ({}, {})", from, size);
        return page.getContent().stream().map(categoryMapper::categoryToResponse).toList();
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long categoryId) {
        try {
            Category oldCategory = findById(categoryId);
            oldCategory.setName(categoryRequest.getName());
            CategoryResponse categoryResponse = categoryMapper.categoryToResponse(categoryRepository.save(oldCategory));
            log.info("Category with id={} was updated", categoryId);
            return categoryResponse;
        } catch (DataIntegrityViolationException e) {
            throw checkUniqueConstraint(e, categoryRequest.getName());
        }
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        findById(categoryId);
        categoryRepository.deleteById(categoryId);
        log.info("Category with id={} was deleted", categoryId);
    }

    private RuntimeException checkUniqueConstraint(RuntimeException e, String categoryName) {
        if (e.getMessage().contains("categories_name_key")) {
            log.warn("Category with name '{}' already exists", categoryName);
            return new DuplicationException(String.format("Category with name '%s' already exists", categoryName));
        }
        log.warn("Data integrity violation", e);
        return e;
    }

    public Category findById(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            log.info("Category with id={} found", categoryId);
            return categoryOpt.get();
        } else {
            log.warn("Category with id={} not found", categoryId);
            throw new NotFoundException(String.format("Category with id=%d not found", categoryId));
        }
    }
}