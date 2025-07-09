package com.talkjang.demo.repository;

import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.dto.response.product.ProductListResponseDto;
import com.talkjang.demo.entity.Category;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

    /*
    @Query("SELECT p FROM Product p" +
            " JOIN FETCH p.category" +
            " JOIN FETCH p.user" +
            " LEFT JOIN FETCH p.productImageList" +
            " WHERE p.id = :productId")
    Product findByIdWithCategoryAndSellerAndImages(@Param("productId") Long productId);

     */

    @Query(
            value = "SELECT COUNT(*)" +
                    "   FROM " +
                    "   (SELECT id " +
                    "       FROM product" +
                    "       WHERE (0 = :categoryId OR category_id = :categoryId)" +
                    "       AND name LIKE %:searchWord%" +
                    "       AND state = 'ON_SALE'"+
                    "       LIMIT :limit) t",
            nativeQuery = true
    )
    Long pageCount(@Param("limit") Long limit, @Param("categoryId") Long categoryId, @Param("searchWord") String searchWord);

    @Query(
            value = "SELECT COUNT(*)" +
                    "   FROM product" +
                    "   WHERE seller_id = :userId",
            nativeQuery = true
    )
    Long countProductByUserId(@Param("userId") String userId);

    @Modifying
    @Query(
            value = "UPDATE product" +
                    "   SET view_count = :viewCount" +
                    "   WHERE id = :productId AND" +
                    "   :viewCount >= view_count",
            nativeQuery = true
    )
    void productViewCountBackUp(@Param("productId") Long productId, @Param("viewCount") Long viewCount);

    @Query(
            value = "SELECT p FROM Product p " +
                    "JOIN FETCH p.productImageList pil " +
                    "JOIN FETCH p.category c " +
                    "WHERE p.id = :productId"
    )
    Optional<Product> findProductFetchJoinProductImageById(@Param("productId") Long productId);

    @Modifying
    @Query(
            value = "UPDATE Product p " +
                    "SET p.upAt = :now " +
                    "WHERE p.id = :productId"
    )
    int upAtByProductId(@Param("productId") Long productId, @Param("now") LocalDateTime now);

}
