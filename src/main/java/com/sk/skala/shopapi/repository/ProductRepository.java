package com.sk.skala.shopapi.repository;

import com.sk.skala.shopapi.data.table.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);

    @Query("SELECT p FROM Product p "
            + "WHERE (:productName IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%'))) "
            + "AND (:minPrice IS NULL OR p.productPrice >= :minPrice) "
            + "AND (:maxPrice IS NULL OR p.productPrice <= :maxPrice)")
    Page<Product> search(@Param("productName") String productName,
                          @Param("minPrice") Double minPrice,
                          @Param("maxPrice") Double maxPrice,
                          Pageable pageable);
}
