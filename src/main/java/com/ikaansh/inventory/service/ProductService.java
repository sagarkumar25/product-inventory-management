
package com.ikaansh.inventory.service;

import com.ikaansh.inventory.dto.CategoryResponse;
import com.ikaansh.inventory.dto.ProductRequest;
import com.ikaansh.inventory.dto.ProductResponse;
import com.ikaansh.inventory.entity.Category;
import com.ikaansh.inventory.entity.Product;
import com.ikaansh.inventory.repository.CategoryRepository;
import com.ikaansh.inventory.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = mapRequestToProduct(request);
        Product savedProduct = productRepository.save(product);
        return mapProductToResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapProductToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapProductToResponse);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchProductsByName(String name) {
        return productRepository.searchByName(name)
                .stream()
                .map(this::mapProductToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id));

        return mapProductToResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id));

        Product updatedProduct = mapRequestToProduct(request);

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());
        product.setPrimaryCategory(updatedProduct.getPrimaryCategory());
        product.setCategories(new ArrayList<>(updatedProduct.getCategories()));

        Product savedProduct = productRepository.save(product);
        return mapProductToResponse(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id));

        productRepository.delete(product);
    }

    private Product mapRequestToProduct(ProductRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        if (request.getPrimaryCategoryId() != null) {
            Category primaryCategory = categoryRepository
                    .findById(request.getPrimaryCategoryId())
                    .orElseThrow(() ->
                            new RuntimeException("Primary category not found"));

            product.setPrimaryCategory(primaryCategory);
        } else {
            product.setPrimaryCategory(null);
        }

        List<Category> categories = new ArrayList<>();

        if (request.getCategoryIds() != null) {
            for (Long categoryId : request.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found with id: " + categoryId));

                categories.add(category);
            }
        }

        product.setCategories(categories);

        return product;
    }

    private ProductResponse mapProductToResponse(Product product) {
        CategoryResponse primaryCategory = null;

        if (product.getPrimaryCategory() != null) {
            Category category = product.getPrimaryCategory();

            primaryCategory = new CategoryResponse(
                    category.getId(),
                    category.getName(),
                    category.getDescription()
            );
        }

        List<CategoryResponse> categories = product.getCategories()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()
                ))
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                primaryCategory,
                categories
        );
    }
}
