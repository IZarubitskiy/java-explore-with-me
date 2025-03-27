package ru.practicum.ewm.category.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.category.dto.CategoryRequest;
import ru.practicum.ewm.category.dto.CategoryResponse;
import ru.practicum.ewm.category.model.Category;

@Mapper
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category requestToCategory(CategoryRequest categoryRequest);

    CategoryResponse categoryToResponse(Category category);
}