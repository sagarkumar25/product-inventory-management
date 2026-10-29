
package com.ikaansh.inventory.service;

import com.ikaansh.inventory.dto.CategoryRequest;
import com.ikaansh.inventory.dto.CategoryResponse;
import com.ikaansh.inventory.entity.Category;
import com.ikaansh.inventory.repository.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getAllCategoriesShouldReturnCategories() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setDescription("Electronic products");

        when(categoryRepository.findAll())
                .thenReturn(List.of(category));

        List<CategoryResponse> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).name());
    }

    @Test
    void getCategoryByIdShouldReturnCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setDescription("Electronic products");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        CategoryResponse result = categoryService.getCategoryById(1L);

        assertEquals(1L, result.id());
        assertEquals("Electronics", result.name());
    }

    @Test
    void getCategoryByIdShouldThrowExceptionWhenNotFound() {
        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> categoryService.getCategoryById(99L)
        );
    }

    @Test
    void createCategoryShouldSaveCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");
        request.setDescription("Electronic products");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Electronics");
        savedCategory.setDescription("Electronic products");

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(savedCategory);

        CategoryResponse result = categoryService.createCategory(request);

        assertEquals(1L, result.id());
        assertEquals("Electronics", result.name());
        verify(categoryRepository).save(any(Category.class));
    }
}

