//
//package com.ikaansh.inventory.service;
//
//import com.ikaansh.inventory.entity.Product;
//import com.ikaansh.inventory.repository.CategoryRepository;
//import com.ikaansh.inventory.repository.ProductRepository;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class ProductServiceTest {
//
//    @Test
//    void getProductByIdShouldReturnProduct() {
//        ProductRepository productRepository = mock(ProductRepository.class);
//        CategoryRepository categoryRepository = mock(CategoryRepository.class);
//
//        ProductService productService = new ProductService(
//                productRepository, categoryRepository);
//
//        Product product = new Product();
//        product.setId(1L);
//        product.setName("Wireless Mouse");
//
//        when(productRepository.findById(1L))
//                .thenReturn(Optional.of(product));
//
//        assertEquals("Wireless Mouse",
//                productService.getProductById(1L).name());
//
//        verify(productRepository).findById(1L);
//    }
//}




package com.ikaansh.inventory.service;

import com.ikaansh.inventory.dto.ProductRequest;
import com.ikaansh.inventory.dto.ProductResponse;
import com.ikaansh.inventory.entity.Product;
import com.ikaansh.inventory.repository.CategoryRepository;
import com.ikaansh.inventory.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Test
    void getProductByIdShouldReturnProduct() {
        ProductRepository productRepository = mock(ProductRepository.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);

        ProductService productService = new ProductService(
                productRepository, categoryRepository);

        Product product = new Product();
        product.setId(1L);
        product.setName("Wireless Mouse");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertEquals("Wireless Mouse",
                productService.getProductById(1L).name());

        verify(productRepository).findById(1L);
    }

    @Test
    void getProductByIdShouldThrowExceptionWhenNotFound() {
        ProductRepository productRepository = mock(ProductRepository.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);

        ProductService productService = new ProductService(
                productRepository, categoryRepository);

        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.getProductById(99L));

        assertEquals("Product not found with id: 99", exception.getMessage());
    }

    @Test
    void searchProductsByNameShouldReturnMatchingProducts() {
        ProductRepository productRepository = mock(ProductRepository.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);

        ProductService productService = new ProductService(
                productRepository, categoryRepository);

        Product product = new Product();
        product.setId(1L);
        product.setName("Wireless Mouse");
        product.setCategories(List.of());

        when(productRepository.searchByName("Mouse"))
                .thenReturn(List.of(product));

        List<ProductResponse> results =
                productService.searchProductsByName("Mouse");

        assertEquals(1, results.size());
        assertEquals("Wireless Mouse", results.get(0).name());

        verify(productRepository).searchByName("Mouse");
    }

    @Test
    void createProductShouldSaveProduct() {
        ProductRepository productRepository = mock(ProductRepository.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);

        ProductService productService = new ProductService(
                productRepository, categoryRepository);

        ProductRequest request = new ProductRequest();
        request.setName("Wireless Mouse");
        request.setDescription("USB wireless mouse");
        request.setPrice(new BigDecimal("799.00"));
        request.setQuantity(25);
        request.setCategoryIds(List.of());

        when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class)))
                .thenAnswer(invocation -> {
                    Product saved = invocation.getArgument(0);
                    saved.setId(1L);
                    return saved;
                });

        ProductResponse response = productService.createProduct(request);

        assertEquals(1L, response.id());
        assertEquals("Wireless Mouse", response.name());
        assertEquals(new BigDecimal("799.00"), response.price());

        verify(productRepository).save(
                org.mockito.ArgumentMatchers.any(Product.class));
    }
}
