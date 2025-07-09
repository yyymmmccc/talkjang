package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.*;
import com.talkjang.demo.dto.request.order.OrderFinalizeRequestDto;
import com.talkjang.demo.dto.request.order.OrderUpdateTrackingNumberRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.order.*;
import com.talkjang.demo.entity.*;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // 주문페이지에 들어가기 위해 주문을 생성
    @Transactional
    public CommonResponseDto createOrderDraft(String userId, Long productId, TradeMethod tradeMethod){
        User user = findByUserOrThrow(userId);
        Product product = findByProductOrThrow(productId);

        Orders orders = ordersRepository.save(
                Orders.builder()
                        .user(user)
                        .product(product)
                        .state(OrderState.PENDING_PAYMENT)
                        .tradeMethod(tradeMethod)
                        .totalAmount(
                                tradeMethod == TradeMethod.DELIVERY ? product.getPrice() + product.getDeliveryFee() : product.getPrice()
                        )
                        .isBuyerReview(false)
                        .isSellerReview(false)
                        .build()
        );

        return CommonResponseDto.success(orders.getId());
    }

    public CommonResponseDto getOrderDraft(String userId, Long orderId) {
        Orders orders = findByOrdersOrThrow(orderId);

        // 해당 주문이 구매자 요청인지 검증
        validateOrderBuyerUser(userId, orders);

        return CommonResponseDto.success(OrderDetailResponseDto.from(orders));
    }

    @Transactional
    public CommonResponseDto finalizeOrder(String userId, Long orderId, OrderFinalizeRequestDto dto) {

        User user = findByUserOrThrow(userId);

        Orders orders = findByOrdersOrThrow(orderId);

        // 해당 주문이 구매자 주문인지 검증
        validateOrderBuyerUser(userId, orders);
        // 총 금액, 포인트 사용 금액, 총 결제금액 검증
        validatePaymentAmount(orders.getTotalAmount(), dto.getUsedPoint(), dto.getPaidAmount());

        // 프론트에서 다음에 주문할 때도 해당 주소 사용하기 체크한 경우 -> user 테이블에 주소를 업데이트
        if(dto.getIsAddressSave()){
            user.updateAddress(dto.getAddress(), dto.getAddressDetail());
        }

        // 결제버튼을 눌렀을 때, 나머지(요청사항, 배송지 주소 등을 넣기) 업데이트
        orders.updateOrderDetails(dto);

        return CommonResponseDto.success(OrderFinalizeResponseDto.from(orders));
    }

    @Transactional
    // 해당 구매자가 결제 후 -> 주문번호에 해당하는 주문객체를 꺼내와서 구매자에게 결제됐다는 메시지 전송, 판매자에게 운송장번호를 등록하라는 메시지 전송
    public CommonResponseDto sendPaymentCompleteMessage(Long orderId) {

        Orders orders = findByOrdersOrThrow(orderId);

        String sellerId = notificationService.sendPaymentCompleteMessage(orders);

        return CommonResponseDto.success(sellerId);
    }

    // 해당 주문을 주문번호로 가져오기
    public CommonResponseDto getOrder(String userId, Long orderId) {

        Orders orders = findByOrdersOrThrow(orderId);

        // 주문이 결제전일 경우 주문정보를 조회할수없음 오류
        if(orders.getState().equals(OrderState.PENDING_PAYMENT)){
            throw new CustomException(ErrorCode.BAD_REQUEST_ORDER_STATE);
        }

        String buyerId = orders.getUser().getId();
        String sellerId = orders.getProduct().getUser().getId();

        // 요청한 사람이 구매자도 아니고 판매자도 아니면 예외처리
        if (!buyerId.equals(userId) && !sellerId.equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return CommonResponseDto.success(OrderResponseDto.of(orders, userId.equals(sellerId)));
    }

    @Transactional
    public CommonResponseDto orderCancel(String userId, Long orderId) {

        Orders orders = findByOrdersOrThrow(orderId);

        validateOrderBuyerUser(userId, orders);

        // 해당 주문이 결제완료 상태가 아닐 때 throw
        if(!orders.getState().equals(OrderState.PAID)){
            throw new CustomException(ErrorCode.BAD_REQUEST_ORDER_CANCEL);
        }

        // 해당 주문상태 취소로 변경
        orders.updateOrderState(OrderState.CANCELLED);

        // 상품 상태를 거래중 -> 판매중으로 변경
        orders.getProduct().updateProductState(ProductState.ON_SALE);

        // 해당 사용자 포인트 환불
        User user = findByUserOrThrow(userId);
        Long currentPoint = user.getPoint();
        Long usedPoint = orders.getUsedPoint();

        user.updatePoint(currentPoint + usedPoint);

        String sellerId = notificationService.sendOrderCancelMessage(orders);

        return CommonResponseDto.success(sellerId);
    }

    @Transactional
    public CommonResponseDto updateOrderTrackingNumber(Long orderId, OrderUpdateTrackingNumberRequestDto dto) {
        Orders orders = findByOrdersOrThrow(orderId);

        String courier = dto.getCourier();
        String trackingNumber = dto.getTrackingNumber();

        // 트래킹번호 추가하기
        orders.updateCourierAndTrackingNumber(courier, trackingNumber);
        orders.updateOrderState(OrderState.READY_FOR_SHIPMENT);
        orders.updateTrackingNumberRegisteredAt(LocalDateTime.now());

        notificationService.sendTrackingNumberMessage(orders);

        return CommonResponseDto.success(null);
    }

    // 구매자가 확정 버튼 누른경우 -> 해당 userID가 구매자인지 검증
    @Transactional
    public CommonResponseDto updateOrderPurchaseConfirmation(String userId, Long orderId) {
        Orders orders = findByOrdersOrThrow(orderId);

        validateOrderBuyerUser(userId, orders);

        orders.updateOrderState(OrderState.COMPLETED);
        orders.getProduct().updateProductState(ProductState.SOLD_OUT);

        String sellerId = orders.getProduct().getUser().getId();

        User user = findByUserOrThrow(sellerId);

        Long totalAmount = orders.getTotalAmount();
        Long currentPoint = user.getPoint();
        user.updatePoint(currentPoint + totalAmount);

        notificationService.sendPurchasesConfirmationMessage(orders);

        return CommonResponseDto.success(sellerId);
    }

    public CommonResponseDto acceptDirectTrade(String userId, Long orderId) {
        // 해당 order 판매자가 userId 인지 비교
        // order.state 를 direct 로 변경

         // 직거래 수락 메시지보내기
        Orders orders = findByOrdersOrThrow(orderId);

        validateOrderSellerUser(userId, orders);

        orders.updateOrderState(OrderState.DIRECT);

        notificationService.sendDirectTradeAcceptMessage(orders);

        return CommonResponseDto.success(orders.getUser().getId());
    }

    public CommonResponseDto getMePurchases(String userId, String state, Long lastIndex, Long pageSize) {

        Boolean hasNext = false;

        List<OrderPurchasesResponseDto> dtoList = ordersRepository.findPurchasesByUserId(userId, state, lastIndex, pageSize);

        if(dtoList.size() > pageSize) hasNext = true;

        List<OrderPurchasesResponseDto> responseDto = dtoList.stream().limit(pageSize).toList();

        return CommonResponseDto.success(
                OrderPurchasesInfinityScrollResponseDto.of(responseDto, hasNext, responseDto.getLast().getOrderId())
        );
    }

    public CommonResponseDto getMeSales(String userId, String state, Long lastIndex, Long pageSize) {

        Boolean hasNext = false;

        List<OrderSalesResponseDto> dtoList = ordersRepository.findSalesByUserId(userId, state, lastIndex, pageSize);

        if(dtoList.size() > pageSize) hasNext = true;

        List<OrderSalesResponseDto> responseDto = dtoList.stream().limit(pageSize).toList();

        return CommonResponseDto.success(
                OrderSalesInfinityScrollResponseDto.of(responseDto, hasNext, responseDto.getLast().getOrderId())
        );
    }

    // 구매자가 결제 완료 후 -> 주문상태(결제완료), 상품상태(거래중), 결제시간(현재) 로 바꾸기
    public Orders updateOrderAfterPayment(String orderId) {
        Orders orders = findByOrdersOrThrow(Long.valueOf(orderId));

        orders.updateOrderState(OrderState.PAID);
        orders.getProduct().updateProductState(ProductState.IN_TRANSACTION);
        orders.updatePaymentCompletedAt(LocalDateTime.now());

        return orders;
    }

    public void updateUserPoint(String userId, Orders orders) {

        User user = findByUserOrThrow(userId);

        Long usedPoint = orders.getUsedPoint();
        Long currentPoint = user.getPoint();

        user.updatePoint(currentPoint - usedPoint);
    }

    private void validatePaymentAmount(Long totalAmount, Long usedPoint, Long paidAmount) {
        Long expectedPaid = totalAmount - usedPoint;

        if (!expectedPaid.equals(paidAmount)) {

            throw new CustomException(ErrorCode.BAD_REQUEST_PAYMENT);
        }
    }

    private void validateOrderBuyerUser(String userId, Orders orders) {
        if (!orders.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void validateOrderSellerUser(String userId, Orders orders) {
        if (!orders.getProduct().getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    public Product findByProductOrThrow(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
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
