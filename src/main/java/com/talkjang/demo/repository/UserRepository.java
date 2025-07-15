package com.talkjang.demo.repository;

import com.talkjang.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByShopName(String shopName);

    Optional<User> findByEmail(String email);

    Optional<User>findByPhone(String phoneNumber);

    @Modifying
    @Query(
            value = "UPDATE user" +
                    "   SET view_count = :viewCount" +
                    "   WHERE id = :userId AND " +
                    "   (view_count = 0 OR view_count >= :viewCount)",
            nativeQuery = true
    )
    void shopViewCountBackUp(@Param("userId") String userId, @Param("viewCount") Long viewCount);
}
