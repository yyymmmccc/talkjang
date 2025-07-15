package com.talkjang.demo.dto.response.user;

import com.talkjang.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String userId;
    private String shopName;
    private Long point;

    public static UserResponseDto from(User user){
        return UserResponseDto.builder()
                .userId(user.getId())
                .shopName(user.getShopName())
                .point(user.getPoint())
                .build();
    }
}
