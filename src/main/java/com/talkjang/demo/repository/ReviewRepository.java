package com.talkjang.demo.repository;

import com.talkjang.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {


    Long countByToUserId(String id);

    @Query(
            value = "SELECT AVG(rating) FROM" +
                    "   review" +
                    "   WHERE to_user_id = :userId",
            nativeQuery = true
    )
    Double averageReviewRatingByUserId(String userId);
}
