
package com.ikaansh.inventory.repository;

import com.ikaansh.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(name = "Product.searchByName")
    List<Product> searchByName(@Param("name") String name);
}
