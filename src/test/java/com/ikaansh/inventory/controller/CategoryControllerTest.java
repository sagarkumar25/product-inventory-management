
package com.ikaansh.inventory.controller;

import com.ikaansh.inventory.dto.CategoryResponse;
import com.ikaansh.inventory.exception.GlobalExceptionHandler;
import com.ikaansh.inventory.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest {

    private CategoryService categoryService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);

        CategoryController categoryController =
                new CategoryController(categoryService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllCategoriesShouldReturnCategories() throws Exception {
        CategoryResponse category = new CategoryResponse(
                1L,
                "Electronics",
                "Electronic products"
        );

        when(categoryService.getAllCategories())
                .thenReturn(List.of(category));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void getCategoryByIdShouldReturnCategory() throws Exception {
        CategoryResponse category = new CategoryResponse(
                1L,
                "Electronics",
                "Electronic products"
        );

        when(categoryService.getCategoryById(1L))
                .thenReturn(category);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void getCategoryByIdShouldReturnNotFound() throws Exception {
        when(categoryService.getCategoryById(99L))
                .thenThrow(
                        new RuntimeException("Category not found with id: 99")
                );

        mockMvc.perform(get("/api/categories/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Category not found with id: 99"));
    }
}
