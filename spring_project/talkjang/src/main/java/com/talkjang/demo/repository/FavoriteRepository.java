package com.talkjang.demo.repository;

import com.talkjang.demo.entity.Favorite;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    @Query(
            value = "SELECT COUNT(*)" +
                    "   FROM favorite" +
                    "   WHERE product_id = :productId",
            nativeQuery = true
    )
    Long countByProductId(@Param("productId") Long productId);

    Favorite findByUserAndProduct(User user, Product product);

    Long countByUserId(String userId);

}
