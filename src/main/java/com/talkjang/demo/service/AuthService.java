package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.dto.request.auth.LoginRequestDto;
import com.talkjang.demo.dto.request.auth.UserCreateRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.auth.LoginResponseDto;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.jwt.JwtTokenProvider;
import com.talkjang.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CommonResponseDto checkUserIdDuplicate(String userId){

        existsByUserId(userId);

        return CommonResponseDto.success(null);
    }

    public CommonResponseDto checkShopNameDuplicate(String shopName){

        existsByShopName(shopName);

        return CommonResponseDto.success(null);
    }

    public CommonResponseDto checkUserEmailDuplicate(String email){

        existsByUserEmail(email);

        return CommonResponseDto.success(null);
    }

    public CommonResponseDto checkUserPhoneDuplicate(String phoneNumber){

        existsByPhoneNumber(phoneNumber);

        return CommonResponseDto.success(null);
    }

    @Transactional
    public CommonResponseDto create(UserCreateRequestDto dto){

        validateDuplicateUser(dto);

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        userRepository.save(dto.toEntity(encodedPassword));

        return CommonResponseDto.success(null);
    }

    public CommonResponseDto login(LoginRequestDto dto){

        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.NOT_FOUND_USER);

        // jwt 토큰 리턴하기
        String accessToken = jwtTokenProvider.createAccessToken(user.getId());

        return CommonResponseDto.success(LoginResponseDto.of(accessToken, dto.getRedirect()));
    }

    // 회원가입시 최종 검사
    public void validateDuplicateUser(UserCreateRequestDto dto){
        existsByUserId(dto.getId());
        existsByShopName(dto.getShopName());
        existsByUserEmail(dto.getEmail());
        existsByPhoneNumber(dto.getPhone());
    }

    public void existsByUserId(String userId){

        userRepository.findById(userId)
                .ifPresent(id -> {
                    throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
                });
    }

    public void existsByShopName(String shopName){

        userRepository.findByShopName(shopName)
                .ifPresent(id -> {
                    throw new CustomException(ErrorCode.DUPLICATE_SHOP_NAME);
                });
    }

    public void existsByUserEmail(String email){

        userRepository.findByEmail(email)
                .ifPresent(id -> {
                    throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);
                });
    }

    private void existsByPhoneNumber(String phoneNumber) {

        userRepository.findByPhone(phoneNumber)
                .ifPresent(id -> {
                    throw new CustomException(ErrorCode.DUPLICATE_USER_PHONE);
                });
    }
}
