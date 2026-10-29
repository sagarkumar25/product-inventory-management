
package com.ikaansh.inventory.service;

import com.ikaansh.inventory.dto.CategoryRequest;
import com.ikaansh.inventory.dto.CategoryResponse;
import com.ikaansh.inventory.entity.Category;
import com.ikaansh.inventory.repository.CategoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Category name already exists");
        }

        Category category = new Category();
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());

        return mapToResponse(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        return mapToResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        categoryRepository.findByNameIgnoreCase(request.getName().trim())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Category name already exists");
                });

        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());

        return mapToResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        if (!category.getProducts().isEmpty()
                || !category.getTaggedProducts().isEmpty()) {
            throw new RuntimeException(
                    "Cannot delete a category assigned to products");
        }

        categoryRepository.delete(category);
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}

