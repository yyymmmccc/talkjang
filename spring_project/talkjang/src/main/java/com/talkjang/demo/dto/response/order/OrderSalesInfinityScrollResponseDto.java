package com.talkjang.demo.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSalesInfinityScrollResponseDto {

    private List<OrderSalesResponseDto> list;
    private Boolean hasNext;
    private Long lastIndex;

    public static OrderSalesInfinityScrollResponseDto of(List<OrderSalesResponseDto> list, Boolean hasNext, Long lastIndex){
        return OrderSalesInfinityScrollResponseDto.builder()
                .list(list)
                .hasNext(hasNext)
                .lastIndex(lastIndex)
                .build();
    }
}
