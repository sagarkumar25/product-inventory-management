
package com.ikaansh.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        CategoryResponse primaryCategory,
        List<CategoryResponse> categories
) {
}

