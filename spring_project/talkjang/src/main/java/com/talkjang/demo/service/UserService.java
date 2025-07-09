package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.dto.request.shop.UpdateIntroductionRequestDto;
import com.talkjang.demo.dto.request.user.UpdatePasswordRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.user.UserResponseDto;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 자신의 정보가져오기
    public CommonResponseDto getUser(String userId){
        User user = getUserByUserIdOrThrow(userId);

        return CommonResponseDto.success(UserResponseDto.from(user));
    }

    // 사용자의 비밀번호를 변경하기
    @Transactional
    public CommonResponseDto updatePassword(String userId, UpdatePasswordRequestDto dto) {
        User user = getUserByUserIdOrThrow(userId);

        if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.BAD_REQUEST_UPDATE_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(dto.getNewPassword()));

        return CommonResponseDto.success(null);
    }

    @Transactional
    public User updateShopName(String oldShopName, String newShopName){

        userRepository.findByShopName(newShopName)
                .ifPresent(id -> {
                    throw new CustomException(ErrorCode.DUPLICATE_SHOP_NAME);
                });
        // newShopName 을 db에 봐서 중복되는지 확인해야함

        User user = getUserByShopNameOrThrow(oldShopName);

        user.updateShopName(newShopName);

        return user;
    }

    @Transactional
    public User updateIntroduction(UpdateIntroductionRequestDto dto) {
        User user = getUserByShopNameOrThrow(dto.getShopName());

        user.updateIntroduction(dto.getIntroduction());

        return user;
    }

    // shopName 에 해당하는 정보가져오기
    public User getUserByShopNameOrThrow(String shopName) {
        return userRepository.findByShopName(shopName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public User getUserByUserIdOrThrow(String userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public String updateProfileImage(String userId, String profileImageUrl) {
        User user = getUserByUserIdOrThrow(userId);

        user.updateProfileImage(profileImageUrl);

        return user.getProfileImageUrl();
    }
}
