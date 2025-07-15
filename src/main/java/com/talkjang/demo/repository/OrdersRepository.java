package com.talkjang.demo.repository;

import com.talkjang.demo.common.enums.OrderState;
import com.talkjang.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;


public interface OrdersRepository extends JpaRepository<Orders, Long>, OrderCustomRepository {

    @Modifying
    @Query(
            value = "DELETE FROM Orders o" +
                    "   WHERE o.state = :orderState AND o.createdAt < :deadLine"
    )
    void deleteAllUnPaidOrders(@Param("orderState") OrderState orderState, @Param("deadLine") LocalDateTime deadLine);

    @Query(
            "SELECT o" +
            "   FROM Orders o" +
            "   JOIN FETCH o.product p" +
            "   JOIN FETCH o.user u" +
            "   WHERE o.state = :orderState AND o.paymentCompletedAt < :deadLine"
    )
    List<Orders> findAllPaidOrders(@Param("orderState") OrderState orderState, @Param("deadLine") LocalDateTime deadLine);

    @Query(
            value = "SELECT o" +
                    "   FROM Orders o" +
                    "   JOIN FETCH o.product p" +
                    "   JOIN FETCH o.user u" +
                    "   WHERE (o.state = :stateDirect OR o.state = :stateReadyForShipment) AND o.trackingNumberRegisteredAt < :deadLine"
    )
    List<Orders> findAllReadyForShipmentOrDirect(@Param("stateReadyForShipment") OrderState stateReadyForShipment,
                                                 @Param("stateDirect") OrderState stateDirect,
                                                 @Param("deadLine") LocalDateTime deadLine);
}
