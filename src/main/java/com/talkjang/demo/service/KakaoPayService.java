package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.dto.request.kakaopay.KakaoPayApproveRequestDto;
import com.talkjang.demo.dto.request.kakaopay.KakaoPayCancelRequestDto;
import com.talkjang.demo.dto.request.kakaopay.KakaoPayReadyRequestDto;
import com.talkjang.demo.dto.request.kakaopay.OrderInfoRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.kakaopay.KakaoPayApproveResponseDto;
import com.talkjang.demo.dto.response.kakaopay.KakaoPayReadyResponseDto;
import com.talkjang.demo.entity.Orders;
import com.talkjang.demo.entity.OrdersKakao;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.OrdersKakaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPayService {

    @Value("${kakao-pay.key}")
    private String kakaoPayKey;

    @Value("${kakao-pay.cid}")
    private String cid;

    private final OrdersKakaoRepository ordersKakaoRepository;
    private final OrdersService ordersService;

    public CommonResponseDto ready(String userId, OrderInfoRequestDto orderInfoRequestDto){

        KakaoPayReadyRequestDto kakaoPayReadyRequestDto =
                KakaoPayReadyRequestDto.builder()
                        .cid(cid)
                        .partner_order_id(orderInfoRequestDto.getPartner_order_id())
                        .partner_user_id(userId)
                        .item_name(orderInfoRequestDto.getItem_name())
                        .quantity(orderInfoRequestDto.getQuantity())
                        .total_amount(orderInfoRequestDto.getTotal_amount())
                        .vat_amount(orderInfoRequestDto.getVat_amount())
                        .tax_free_amount(orderInfoRequestDto.getTax_free_amount())
                        .approval_url("http://localhost:8080/api/kakao-pay/approve?orderId=" + orderInfoRequestDto.getPartner_order_id() + "&userId=" + userId)
                        .fail_url("http://localhost:8080/api/kakao-pay/fail?orderId=" + orderInfoRequestDto.getPartner_order_id())
                        .cancel_url("http://localhost:8080/api/kakao-pay/cancel?orderId=" + orderInfoRequestDto.getPartner_order_id())
                        .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + kakaoPayKey);

        HttpEntity<KakaoPayReadyRequestDto> httpEntity = new HttpEntity<>(kakaoPayReadyRequestDto, headers);

        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";


        ResponseEntity<KakaoPayReadyResponseDto> response =
                new RestTemplate().postForEntity(
                        url,
                        httpEntity,
                        KakaoPayReadyResponseDto.class
                );

        // 카카오서버에서 응답한 tid 를 db에 저장

        ordersKakaoRepository.save(
                OrdersKakao.builder()
                        .orderId(Long.valueOf(orderInfoRequestDto.getPartner_order_id()))
                        .tid(response.getBody().getTid())
                        .build()
        );

        return CommonResponseDto.success(response);
    }

    // 사용자가 qr로 결제완료를 했을 때
    // 카카오서버에서 우리서버로 http://localhost:8080?pgToken=252341&orderId=2341203&userId=325135 이런식으로 넘겨줌
    @Transactional
    public ResponseEntity approve(String pgToken, String orderId, String userId) {

        KakaoPayApproveResponseDto response = requestKakaoPayApproval(pgToken, orderId, userId);

        // 실제 결제가 이루어진후 상품 상태 변경하기
        //Orders orders = ordersService.updateOrderAfterPayment(orderId);

        //ordersService.

        // 포인트를 쓴 사용자인 경우 쓴 포인트만큼 차감하기

        // 실제 결제가 이루어진 후 주문 생성하기
        //ordersService

        HttpHeaders redirectHeaders = new HttpHeaders();
        redirectHeaders.setLocation(URI.create("http://localhost:8100/purchases/success.html?orderId=" + orderId));
        return new ResponseEntity<>(redirectHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

    private KakaoPayApproveResponseDto requestKakaoPayApproval(String pgToken, String orderId, String userId) {

        OrdersKakao ordersKakao = findByOrdersKakaoOrThrow(Long.valueOf(orderId));

        KakaoPayApproveRequestDto requestDto = KakaoPayApproveRequestDto.builder()
                .cid(cid)
                .tid(ordersKakao.getTid())
                .partner_order_id(orderId)
                .partner_user_id(userId)
                .pg_token(pgToken)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaoPayKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(requestDto, headers);
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";

        ResponseEntity<KakaoPayApproveResponseDto> response =
                new RestTemplate().postForEntity(url, entity, KakaoPayApproveResponseDto.class);

        return response.getBody(); // 혹은 실패 시 예외 던지기
    }

    public CommonResponseDto cancel(Long orderId) {

        // 해당 주문번호로 주문정보찾기, 해당 주문번호로 tid 찾기
        Orders orders = ordersService.findByOrdersOrThrow(orderId);
        OrdersKakao ordersKakao = findByOrdersKakaoOrThrow(orderId);

        KakaoPayCancelRequestDto requestDto = KakaoPayCancelRequestDto.builder()
                .cid(cid)
                .tid(ordersKakao.getTid())
                .cancel_amount(orders.getPaidAmount())
                .cancel_tax_free_amount(0L)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + kakaoPayKey);

        String url = "https://open-api.kakaopay.com/online/v1/payment/cancel";

        HttpEntity httpEntity = new HttpEntity<>(requestDto, headers);

        new RestTemplate().postForEntity(
                url,
                httpEntity,
                String.class
        );

        return CommonResponseDto.success(orderId);
    }

    public OrdersKakao findByOrdersKakaoOrThrow(Long orderId){
        return ordersKakaoRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_KAKAOPAY_TID));
    }
}
