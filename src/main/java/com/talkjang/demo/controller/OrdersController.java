package com.talkjang.demo.controller;

import com.talkjang.demo.common.enums.TradeMethod;
import com.talkjang.demo.dto.request.order.OrderFinalizeRequestDto;
import com.talkjang.demo.dto.request.order.OrderUpdateTrackingNumberRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping("/{productId}/create")
    public CommonResponseDto createOrderDraft(@AuthenticationPrincipal String userId,
                                              @PathVariable("productId") Long productId,
                                              @RequestParam("tradeMethod") TradeMethod tradeMethod){

        return ordersService.createOrderDraft(userId, productId, tradeMethod);
    }

    // 해당 임시주문 가져오기
    @GetMapping("/{orderId}/draft")
    public CommonResponseDto getOrderDraft(@AuthenticationPrincipal String userId,
                                           @PathVariable("orderId") Long orderId){

        return ordersService.getOrderDraft(userId, orderId);
    }

    // 최종 결제버튼을 누르면, 기존의 order 테이블에 생성된 주문 레코드 업데이트하기 (결제방식, 요청사항, 배송지 등등)
    @PostMapping("/{orderId}")
    public CommonResponseDto finalizeOrder(@AuthenticationPrincipal String userId,
                                           @PathVariable("orderId") Long orderId,
                                           @RequestBody OrderFinalizeRequestDto dto){

        return ordersService.finalizeOrder(userId, orderId, dto);
    }

    // 실제 결제가 된 후 redis 에 있는 order 정보를 db 에 저장
    @PostMapping("{orderId}/save")
    public CommonResponseDto saveOrder(@PathVariable("orderId") Long orderId){

        return ordersService.saveOrder(orderId);
    }


    // 주문 결제가 완료된 후 orderId 로 해당 주문 조회한 후 구매자 판매자에게 구매 알림톡 보내기
    @PostMapping("/{orderId}/success")
    public CommonResponseDto sendPaymentComplete(@PathVariable Long orderId){

        return ordersService.sendPaymentCompleteMessage(orderId);
    }

    @GetMapping("/{orderId}")
    public CommonResponseDto getOrder(@AuthenticationPrincipal String userId,
                                      @PathVariable Long orderId){

        return ordersService.getOrder(userId, orderId);
    }

    // 주문을 취소할 때 : 알림톡 메시지도 전송
    @PatchMapping("{orderId}/cancel")
    public CommonResponseDto orderCancel(@AuthenticationPrincipal String userId,
                                         @PathVariable("orderId") Long orderId){

        return ordersService.orderCancel(userId, orderId);
    }

    // 판매자가 운송장번호를 눌렀을 때 api : 주문레코드에, 택배사와 운송장 번호 추가하기 -> 업데이트 후 메시지 보내기 (운송장번호가 등록되었어요)
    @PatchMapping("/{orderId}/tracking-number")
    public CommonResponseDto updateOrderTrackingNumber(@PathVariable("orderId") Long orderId,
                                                       @RequestBody OrderUpdateTrackingNumberRequestDto dto){

        return ordersService.updateOrderTrackingNumber(orderId, dto);
    }

    // 구매자가 구매확정버튼 누른경우 -> 해당 orderId 로 판매자 찾아서 결제금액 point 추가해주기, 주문상태, 상품상태 업데이트
    @PostMapping("/{orderId}/purchase-confirmation")
    public CommonResponseDto updateOrderPurchaseConfirmation(@AuthenticationPrincipal String userId,
                                                             @PathVariable("orderId") Long orderId){

        return ordersService.updateOrderPurchaseConfirmation(userId, orderId);
    }

    // 직거래 수락하기 -> orderState = DIRECT 로 바꿈, 구매자에게 알림톡 (구매자가 거래를 수락했어요. 안전한 거래후 구매확정~~~), 판매자에게 알림톡 (구매자와 거래 후 구매확정 버튼을 꼭 )

    @PostMapping("/{orderId}/direct-accept")
    public CommonResponseDto acceptDirectTrade(@AuthenticationPrincipal String userId,
                                               @PathVariable("orderId") Long orderId){

        return ordersService.acceptDirectTrade(userId, orderId);
    }

    // 나의 구매리스트 가져오기 /api/orders/purchases
    @GetMapping("/purchases")
    public CommonResponseDto getMePurchases(@AuthenticationPrincipal String userId,
                                            @RequestParam(value = "state", defaultValue = "ALL") String state,
                                            @RequestParam(value = "lastIndex", required = false) Long lastIndex,
                                            @RequestParam(value = "pageSize", defaultValue = "3") Long pageSize){

        return ordersService.getMePurchases(userId, state, lastIndex, pageSize);
    }

    // 나의 판매리스트
    @GetMapping("/sales")
    public CommonResponseDto getMeSales(@AuthenticationPrincipal String userId,
                                        @RequestParam(value = "state", defaultValue = "ALL") String state,
                                        @RequestParam(value = "lastIndex", required = false) Long lastIndex,
                                        @RequestParam(value = "pageSize", defaultValue = "3") Long pageSize){

        return ordersService.getMeSales(userId, state, lastIndex, pageSize);
    }
}
