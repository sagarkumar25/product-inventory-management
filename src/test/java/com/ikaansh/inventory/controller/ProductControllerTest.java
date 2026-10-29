
package com.ikaansh.inventory.controller;

import com.ikaansh.inventory.dto.ProductResponse;
import com.ikaansh.inventory.exception.GlobalExceptionHandler;
import com.ikaansh.inventory.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private ProductService productService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);

        ProductController productController =
                new ProductController(productService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllProductsShouldReturnProducts() throws Exception {
        ProductResponse product = new ProductResponse(
                1L,
                "Wireless Mouse",
                "USB wireless mouse",
                new BigDecimal("799.00"),
                25,
                null,
                List.of()
        );

        when(productService.getAllProducts())
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Wireless Mouse"))
                .andExpect(jsonPath("$[0].quantity").value(25));
    }

    @Test
    void getProductByIdShouldReturnProduct() throws Exception {
        ProductResponse product = new ProductResponse(
                1L,
                "Wireless Mouse",
                "USB wireless mouse",
                new BigDecimal("799.00"),
                25,
                null,
                List.of()
        );

        when(productService.getProductById(1L))
                .thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Wireless Mouse"));
    }

    @Test
    void getProductByIdShouldReturnNotFound() throws Exception {
        when(productService.getProductById(99L))
                .thenThrow(
                        new RuntimeException("Product not found with id: 99")
                );

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Product not found with id: 99"));
    }
}

