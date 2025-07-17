package com.talkjang.demo.scheduler;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.common.enums.OrderState;
import com.talkjang.demo.common.enums.PaymentMethod;
import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.entity.Orders;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.OrdersRepository;
import com.talkjang.demo.repository.UserRepository;
import com.talkjang.demo.service.KakaoPayService;
import com.talkjang.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCleanupScheduler {

    private final OrdersRepository ordersRepository;
    private final KakaoPayService kakaoPayService;
    private final NotificationService notificationService;

    // 주문 -> 결제가 30분동안 안되어있을 것들 삭제
    // PENDING_PAYMENT 상태가 30분동안 지속되면 해당데이터 삭제
    @Transactional
    @Scheduled(fixedRate = 1800000) // 30분에 한번
    public void deleteUnpaidOrders(){

        log.info("30분동안 미결제 데이터들 삭제 스케줄러 시작");

        LocalDateTime deadLine = LocalDateTime.now().minusMinutes(30);

        ordersRepository.deleteAllUnPaidOrders(OrderState.PENDING_PAYMENT, deadLine);
        log.info("30분동안 미결제 데이터들 삭제 스케줄러 종료");
    }

    // 결제완료상태인데 -> 운송장번호 입력안한경우 3일내에
    // 결제취소메서드, 주문취소 메서드
    @Transactional
    @Scheduled(fixedRate = 300000) // 300초에 한번 즉 5분에 한번
    public void cancelPaidOrdersWithTrackingNumberOver3Days(){

        log.info("결제 3일내에 운송장번호 입력안된거 주문 취소 실행");

        LocalDateTime deadLine = LocalDateTime.now().minusDays(3);

        List<Orders> paidOrders = ordersRepository.findAllPaidOrders(OrderState.PAID, deadLine);

        for(Orders order : paidOrders) {
            PaymentMethod paymentMethod = order.getPaymentMethod();
            // 결제취소, 주문취소
            if (paymentMethod.equals(PaymentMethod.KAKAO)) {
                kakaoPayService.cancel(order.getId());
            }

            order.updateOrderState(OrderState.CANCELLED);
            order.getProduct().updateProductState(ProductState.ON_SALE);

            User buyerUser = order.getUser();

            Long currentPoint = buyerUser.getPoint();
            Long usedPoint = order.getUsedPoint();

            buyerUser.updatePoint(currentPoint + usedPoint);

            notificationService.sendOrderCancelMessage(order);
        }

        log.info("결제 3일내에 운송장번호 입력안된거 주문 취소 종료");
    }

    // 판매자가 운송장 번호 입력 후 (orderState = READY_FOR_SHIPMENT) 상태일 때 -> 구매자가 7일이내에 구매확정 안누르면 자동 구매확정
    @Transactional
    @Scheduled(fixedRate = 300000)
    public void autoConfirmOrdersAfterTrackingNumberEntered(){

        log.info("자동 구매확정 실행");
        LocalDateTime deadLine = LocalDateTime.now().minusDays(7);
        List<Orders> readyForShipmentOrders =
                ordersRepository.findAllReadyForShipmentOrDirect(OrderState.READY_FOR_SHIPMENT, OrderState.DIRECT, deadLine);

        for(Orders order : readyForShipmentOrders){
            order.updateOrderState(OrderState.COMPLETED);
            order.getProduct().updateProductState(ProductState.SOLD_OUT);

            User sellerUser = order.getProduct().getUser();

            Long totalAmount = order.getTotalAmount();
            Long currentPoint = sellerUser.getPoint();
            sellerUser.updatePoint(currentPoint + totalAmount);

            notificationService.sendPurchasesConfirmationMessage(order);
        }

        log.info("자동 구매확정 종료");
    }
}
