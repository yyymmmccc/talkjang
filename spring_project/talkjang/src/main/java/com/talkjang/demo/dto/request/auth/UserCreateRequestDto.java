package com.talkjang.demo.dto.request.auth;

import com.talkjang.demo.common.enums.UserProvider;
import com.talkjang.demo.common.enums.UserRole;
import com.talkjang.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {

    private String id;
    private String password;
    private String name;
    private String shopName;
    private String email;
    private String phone;
    private String address;
    private String addressDetail;
    private String profileImageUrl;

    public User toEntity(String encodedPassword){
        return User.builder()
                .id(id)
                .password(encodedPassword)
                .name(name)
                .shopName(shopName)
                .email(email)
                .phone(phone)
                .address(address)
                .addressDetail(addressDetail)
                .profileImageUrl(profileImageUrl)
                .provider(UserProvider.LOCAL)
                .role(UserRole.USER)
                .point(0L)
                .viewCount(0L)
                .build();
    }
}
