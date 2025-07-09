package com.talkjang.demo.service;

import com.talkjang.demo.common.TradeMessageBuilder;
import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.common.enums.TradeMethod;
import com.talkjang.demo.entity.ChatMessage;
import com.talkjang.demo.entity.ChatRoom;
import com.talkjang.demo.entity.Orders;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.ChatMessageRepository;
import com.talkjang.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final String ADMIN_USER_ID = "admin";

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;

    public String sendPaymentCompleteMessage(Orders orders) {

        TradeMethod tradeMethod = orders.getTradeMethod();

        String buyerId = orders.getUser().getId();
        String sellerId = orders.getProduct().getUser().getId();
        String autoCancelDeadline = getPaymentDeadline(orders);

        String messageToBuyer = TradeMessageBuilder.generatePaymentCompletedMessageToBuyer(orders, tradeMethod, autoCancelDeadline);
        String messageToSeller = TradeMessageBuilder.generatePaymentCompletedMessageToSeller(orders, tradeMethod, autoCancelDeadline);

        ChatRoom chatRoom = chatRoomService.createChatRoom(buyerId, sellerId);

        sendChatMessage(chatRoom, buyerId, messageToBuyer);
        sendChatMessage(chatRoom, sellerId, messageToSeller);

        return sellerId;
    }

    public String sendOrderCancelMessage(Orders orders){

        // 해당 주문에 판매자, 구매자를 꺼냄 -> 채팅방을 만듦 -> 채팅방메시지에 해당 메시지 저장
        String buyerId = orders.getUser().getId();
        String sellerId = orders.getProduct().getUser().getId();

        ChatRoom chatRoom = chatRoomService.createChatRoom(buyerId, sellerId);

        String messageToBuyer = TradeMessageBuilder.generateOrderCancelMessageToBuyer(orders);
        String messageToSeller = TradeMessageBuilder.generateOrderCancelMessageToSeller(orders);

        sendChatMessage(chatRoom, buyerId, messageToBuyer);
        sendChatMessage(chatRoom, sellerId, messageToSeller);

        return sellerId;
    }

    public void sendTrackingNumberMessage(Orders orders) {

        String buyerId = orders.getUser().getId();
        String sellerId = orders.getProduct().getUser().getId();

        // 해당 채팅방 구매자에게 판매자분이 운송장번호를 등록했어요 -> 운송장 번호 확인하러가기
        ChatRoom chatRoom = chatRoomService.createChatRoom(buyerId, sellerId);

        sendChatMessage(chatRoom, buyerId, TradeMessageBuilder.generateTrackingRegisteredMessageToBuyer(orders));
    }

    public void sendPurchasesConfirmationMessage(Orders orders) {

        String buyerId = orders.getUser().getId();
        String sellerId = orders.getProduct().getUser().getId();

        // 해당 채팅방 구매자에게 판매자분이 운송장번호를 등록했어요 -> 운송장 번호 확인하러가기
        ChatRoom chatRoom = chatRoomService.createChatRoom(buyerId, sellerId);

        sendChatMessage(chatRoom, buyerId, TradeMessageBuilder.generatePurchaseConfirmationMessageToBuyer(orders));
        sendChatMessage(chatRoom, sellerId, TradeMessageBuilder.generatePurchaseConfirmationMessageToSeller(orders));
    }

    public void sendDirectTradeAcceptMessage(Orders orders){

        String buyerId = orders.getUser().getId();
        String sellerId = orders.getProduct().getUser().getId();

        ChatRoom chatRoom = chatRoomService.createChatRoom(buyerId, sellerId);

        sendChatMessage(chatRoom, buyerId, TradeMessageBuilder.generateDirectTradeAcceptMessageToBuyer(orders));
        sendChatMessage(chatRoom, sellerId, TradeMessageBuilder.generateDirectTradeAcceptMessageToSeller(orders));

    }

    private void sendChatMessage(ChatRoom chatRoom, String receiverId, String content) {
        User sender = findByUserOrThrow(ADMIN_USER_ID);
        User receiver = findByUserOrThrow(receiverId);

        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .receiver(receiver)
                .sender(sender)
                .content(content)
                .build());
    }

    public User findByUserOrThrow(String userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    private String getPaymentDeadline(Orders orders) {

        LocalDateTime paymentCompletedAt = orders.getPaymentCompletedAt();
        LocalDateTime plus3Days = paymentCompletedAt.plusDays(3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = plus3Days.format(formatter);

        return formattedDate;
    }
}
