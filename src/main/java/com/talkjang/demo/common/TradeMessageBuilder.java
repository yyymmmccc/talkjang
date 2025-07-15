package com.talkjang.demo.common;

import com.talkjang.demo.common.enums.TradeMethod;
import com.talkjang.demo.entity.Orders;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Getter
@NoArgsConstructor
public class TradeMessageBuilder {

    private static HashMap<String, String> COMPANY_MAP = new HashMap<>();

    static {
        COMPANY_MAP.put("01", "우체국택배");
        COMPANY_MAP.put("04", "CJ대한통운");
        COMPANY_MAP.put("05", "한진택배");
        COMPANY_MAP.put("06", "로젠택배");
        COMPANY_MAP.put("08", "롯데택배");
    }

    public static String generatePaymentCompletedMessageToBuyer(Orders orders, TradeMethod tradeMethod, String autoCancelDeadline) {

        String imageTag = "  <a href=\"/products/detail.html?id=" + orders.getProduct().getId() + "\">\n" +
                "    <img class=\"admin-message-image\" src=\"" + orders.getProduct().getProductImageList().get(0).getImageUrl() + "\" alt=\"\">\n" +
                "  </a>\n";

        String deadlineTag = "  <p>기한: " + autoCancelDeadline + "</p>\n" +
                "  <p>거래방법: " + (tradeMethod == TradeMethod.DELIVERY ? "택배거래" : "직거래") + "</p>\n";

        String buttonTag = "  <button class=\"trade-info-button\">\n" +
                "    <a href=\"/purchases/detail.html?orderId=" + orders.getId() + "\">거래 정보 확인</a>\n" +
                "  </button>\n";

        if (tradeMethod == TradeMethod.DELIVERY) {
            return imageTag +
                    "  <div>결제를 완료했어요!</div>\n" +
                    "<p>판매자가 운송장 번호를 등록하기 전까지는 언제든지 즉시 취소하실 수 있어요.</p>\n" +
                    "<p>* 판매자가 3일 이내에 운송장 번호를 등록하지 않으면 자동으로 환불돼요.</p>\n" +
                    deadlineTag + buttonTag;
        }

        else {
            return imageTag +
                    "  <div>결제를 완료했어요!</div>\n" +
                    "  <p>판매자가 직거래를 수락하면 거래가 진행됩니다.</p>\n" +
                    "  <p>* 판매자가 3일 이내에 수락하지 않으면 자동으로 환불돼요.</p>\n" +
                    deadlineTag + buttonTag;
        }
    }

    public static String generatePaymentCompletedMessageToSeller(Orders orders, TradeMethod tradeMethod, String autoCancelDeadline) {
        String imageTag = "  <a href=\"/products/detail.html?id=" + orders.getProduct().getId() + "\">\n" +
                "    <img class=\"admin-message-image\" src=\"" + orders.getProduct().getProductImageList().get(0).getImageUrl() + "\" alt=\"\">\n" +
                "  </a>\n";

        String deadlineTag = "  <p>기한: " + autoCancelDeadline + "</p>\n" +
                "  <p>거래방법: " + (tradeMethod == TradeMethod.DELIVERY ? "택배거래" : "직거래") + "</p>\n" +
                "  <p>요청사항: " + orders.getDeliveryRequest() + "</p>\n";

        String buttonTag;
        String message;

        if (tradeMethod == TradeMethod.DELIVERY) {
            message = "  <div>상품 판매를 축하드려요!</div>\n" +
                    "  <p>[운송장 번호 등록] 버튼을 눌러 운송장 번호를 등록해주세요.</p>\n" +
                    "  <p>3일 내 미등록 시 거래가 자동으로 취소됩니다.</p>\n";
            buttonTag = "  <button class=\"trade-info-button\">\n" +
                    "    <a href=\"/purchases/detail.html?orderId=" + orders.getId() + "\">운송장 번호 등록</a>\n" +
                    "  </button>\n";
        }

        else {
            message = "  <div>상품 판매를 축하드려요!</div>\n" +
                    "  <p>[직거래 수락하기] 버튼을 눌러 거래를 시작해주세요.</p>\n" +
                    "  <p>3일 내 미수락 시 거래가 자동으로 취소됩니다.</p>\n";
            buttonTag = "  <button class=\"trade-info-button\">\n" +
                    "    <a href=\"/purchases/detail.html?orderId=" + orders.getId() + "\">직거래 수락하기</a>\n" +
                    "  </button>\n";
        }

        return imageTag + message + deadlineTag + buttonTag;
    }

    public static String generateTrackingRegisteredMessageToBuyer(Orders orders) {
        String imageUrl = orders.getProduct().getProductImageList().get(0).getImageUrl();
        Long productId = orders.getProduct().getId();
        Long orderId = orders.getId();

        return "  <a href=\"/products/detail.html?id=" + productId + "\">\n" +
                "    <img class=\"admin-message-image\" src=\"" + imageUrl + "\" alt=\"\">\n" +
                "  </a>\n" +
                "   <div>판매자가 운송장 번호를 등록했어요!</div>\n" +
                "   <p><strong>택배사: </strong>" + COMPANY_MAP.get(orders.getCourier()) + "</p>\n" +
                "   <p><strong>운송장 번호: </strong>" + orders.getTrackingNumber() + "</p>\n" +
                "<p>상품을 받은 후 구매 확정을 해주시면 판매자에게 빠르게 정산이 이루어집니다.</p>\n" +
                "<p>장기간 구매 확정을 하지 않으실 경우, 일부 서비스 이용에 제한이 생길 수 있으니 주의해주세요.</p>\n" +
                "  <button class=\"trade-info-button\">\n" +
                "    <a href=\"/purchases/detail.html?orderId=" + orderId + "\">구매 확정 하기</a>\n" +
                "  </button>\n";
    }

    public static String generatePurchaseConfirmationMessageToBuyer(Orders orders){
        String imageUrl = orders.getProduct().getProductImageList().get(0).getImageUrl();
        Long productId = orders.getProduct().getId();
        Long orderId = orders.getId();

        return "  <a href=\"/products/detail.html?id=" + productId + "\">\n" +
                "    <img class=\"admin-message-image\" src=\"" + imageUrl + "\" alt=\"\">\n" +
                "  </a>\n" +
                "   <div>구매가 확정되었어요!</div>\n" +
                "   <p>판매자분에게 후기를 남겨보세요.</p>\n" +
                "  <button class=\"trade-info-button\">\n" +
                "    <a href=\"/purchases/detail.html?orderId=" + orderId + "\">후기 남기기</a>\n" +
                "  </button>\n";
    }

    public static String generatePurchaseConfirmationMessageToSeller(Orders orders){
        String imageUrl = orders.getProduct().getProductImageList().get(0).getImageUrl();
        Long productId = orders.getProduct().getId();
        Long orderId = orders.getId();

        return "  <a href=\"/products/detail.html?id=" + productId + "\">\n" +
                "    <img class=\"admin-message-image\" src=\"" + imageUrl + "\" alt=\"\">\n" +
                "  </a>\n" +
                "   <div>구매자분이 구매 확정을 눌렀어요!</div>\n" +
                "   <p>구매자분에게 후기를 남겨보세요.</p>\n" +
                "  <button class=\"trade-info-button\">\n" +
                "    <a href=\"/purchases/detail.html?orderId=" + orderId + "\">후기 남기기</a>\n" +
                "  </button>\n";
    }

    public static String generateOrderCancelMessageToBuyer(Orders orders){

        String imageUrl = orders.getProduct().getProductImageList().get(0).getImageUrl();
        Long productId = orders.getProduct().getId();
        Long orderId = orders.getId();

        return "  <a href=\"/products/detail.html?id=" + productId + "\">\n" +
                "    <img class=\"admin-message-image\" src=\"" + imageUrl + "\" alt=\"\">\n" +
                "  </a>\n" +
                "   <p>주문이 취소 됐어요.</p>\n" +
                "  <button class=\"trade-info-button\">\n" +
                "    <a href=\"/purchases/detail.html?orderId=" + orderId + "\">거래 정보 확인</a>\n" +
                "  </button>\n";
    }

    public static String generateOrderCancelMessageToSeller(Orders orders){

        String imageUrl = orders.getProduct().getProductImageList().get(0).getImageUrl();
        Long productId = orders.getProduct().getId();
        Long orderId = orders.getId();

        return "  <a href=\"/products/detail.html?id=" + productId + "\">\n" +
                "    <img class=\"admin-message-image\" src=\"" + imageUrl + "\" alt=\"\">\n" +
                "  </a>\n" +
                "   <p>주문이 최소 됐습니다.</p>\n" +
                "  <button class=\"trade-info-button\">\n" +
                "    <a href=\"/purchases/detail.html?orderId=" + orderId + "\">거래 정보 확인</a>\n" +
                "  </button>\n";
    }

    public static String generateDirectTradeAcceptMessageToBuyer(Orders orders){

        String imageUrl = orders.getProduct().getProductImageList().get(0).getImageUrl();
        Long productId = orders.getProduct().getId();
        Long orderId = orders.getId();

        return "  <a href=\"/products/detail.html?id=" + productId + "\">\n" +
                "    <img class=\"admin-message-image\" src=\"" + imageUrl + "\" alt=\"\">\n" +
                "  </a>\n" +
                "  <p>판매자가 직거래를 수락했어요.</p>\n" +
                "  <p>물건을 잘 받았다면 구매 확정을 눌러 주세요.</p>\n" +
                "  <button class=\"trade-info-button\">\n" +
                "    <a href=\"/purchases/detail.html?orderId=" + orderId + "\">구매 확정 하기</a>\n" +
                "  </button>\n";
    }

    public static String generateDirectTradeAcceptMessageToSeller(Orders orders){

        String imageUrl = orders.getProduct().getProductImageList().get(0).getImageUrl();
        Long productId = orders.getProduct().getId();
        Long orderId = orders.getId();

        return "  <a href=\"/products/detail.html?id=" + productId + "\">\n" +
                "    <img class=\"admin-message-image\" src=\"" + imageUrl + "\" alt=\"\">\n" +
                "  </a>\n" +
                "  <p>구매자와 만날 장소를 협의해주세요.</p>\n" +
                "  <p>구매자에게 물건을 잘 전달했으면 구매확정을 요청해주세요.</p>\n" +
                "  <button class=\"trade-info-button\">\n" +
                "    <a href=\"/purchases/detail.html?orderId=" + orderId + "\">거래 정보 확인</a>\n" +
                "  </button>\n";
    }


}
