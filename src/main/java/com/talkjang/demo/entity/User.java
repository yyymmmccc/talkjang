package com.talkjang.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.talkjang.demo.common.enums.UserProvider;
import com.talkjang.demo.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    private String password;

    private String name;

    @Column(name = "shop_name")
    private String shopName;

    private String email;
    private String phone;

    private String introduction;

    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "view_count")
    private Long viewCount;

    private Long point;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private UserProvider provider;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateShopName(String shopName){
        this.shopName = shopName;
    }

    public void updateIntroduction(String introduction){
        this.introduction = introduction;
    }

    public void updateProfileImage(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void updateAddress(String address, String addressDetail){
        this.address = address;
        this.addressDetail = addressDetail;
    }

    public void updatePoint(Long point){
        this.point = point;
    }

}
