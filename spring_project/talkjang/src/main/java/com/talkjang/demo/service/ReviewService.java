package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.dto.request.review.ReviewCreateRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.entity.Orders;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.OrdersRepository;
import com.talkjang.demo.repository.ReviewRepository;
import com.talkjang.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;

    @Transactional
    public CommonResponseDto create(String userId, ReviewCreateRequestDto dto){

        User fromUser = findByUserOrThrow(userId);
        User toUser = findByUserOrThrow(dto.getToUserId());

        reviewRepository.save(dto.toEntity(fromUser, toUser));

        Orders orders = findByOrdersOrThrow(dto.getOrderId());

        String buyerId = orders.getUser().getId();
        // 리뷰 작성한 사람이 구매자인경우, orders 테이블 구매자리뷰 작성 true
        if(buyerId.equals(userId)) orders.updateBuyerReview();

        // 리뷰 작성한 사람이 구매자인경우, orders 테이블 구매자리뷰 작성 true
        else orders.updateSellerReview();

        return CommonResponseDto.success(null);
    }

    public User findByUserOrThrow(String userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public Orders findByOrdersOrThrow(Long orderId){
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
    }
}
