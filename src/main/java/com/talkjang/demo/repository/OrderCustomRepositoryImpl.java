package com.talkjang.demo.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.talkjang.demo.common.enums.OrderState;
import com.talkjang.demo.dto.response.order.OrderPurchasesResponseDto;
import com.talkjang.demo.dto.response.order.OrderSalesResponseDto;
import com.talkjang.demo.dto.response.order.QOrderPurchasesResponseDto;
import com.talkjang.demo.dto.response.order.QOrderSalesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.talkjang.demo.entity.QOrders.orders;
import static com.talkjang.demo.entity.QProduct.product;
import static com.talkjang.demo.entity.QProductImage.productImage;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public List<OrderPurchasesResponseDto> findPurchasesByUserId(String userId, String state, Long lastIndex, Long pageSize){

        return jpaQueryFactory
                .select(new QOrderPurchasesResponseDto(
                        orders.id,
                        orders.paymentCompletedAt,
                        orders.state,
                        productImage.imageUrl,
                        orders.product.name,
                        orders.product.price,
                        orders.product.user.id,
                        orders.product.user.shopName,
                        orders.tradeMethod
                ))
                .from(orders)
                .join(orders.product, product)
                .leftJoin(productImage)
                .on(product.id.eq(productImage.product.id).and(productImage.thumbnail.eq(true)))
                .where(orders.user.id.eq(userId)
                        .and(orders.state.ne(OrderState.PENDING_PAYMENT)) // 결제를 아직 하지않은것들 뺴고
                        .and(lastIndex != null ? orders.id.lt(lastIndex) : null)
                )
                .limit(pageSize + 1)
                .orderBy(orders.id.desc())
                .fetch();
    }

    public List<OrderSalesResponseDto> findSalesByUserId(String userId, String state, Long lastIndex, Long pageSize){

        return jpaQueryFactory
                .select(new QOrderSalesResponseDto(
                        orders.id,
                        orders.paymentCompletedAt,
                        orders.state,
                        productImage.imageUrl,
                        orders.product.name,
                        orders.product.price,
                        orders.user.id,
                        orders.user.shopName,
                        orders.tradeMethod
                ))
                .from(orders)
                .join(orders.product, product)
                .leftJoin(productImage)
                .on(product.id.eq(productImage.product.id).and(productImage.thumbnail.eq(true)))
                .where(orders.product.user.id.eq(userId)
                        .and(orders.state.ne(OrderState.PENDING_PAYMENT)) // 결제를 아직 하지않은것들 뺴고
                        .and(lastIndex != null ? orders.id.lt(lastIndex) : null)
                )
                .limit(pageSize + 1)
                .orderBy(orders.id.desc())
                .fetch();
    }
}
