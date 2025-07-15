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
public class OrderPurchasesInfinityScrollResponseDto {

    private List<OrderPurchasesResponseDto> list;
    private Boolean hasNext;
    private Long lastIndex;

    public static OrderPurchasesInfinityScrollResponseDto of(List<OrderPurchasesResponseDto> list, Boolean hasNext, Long lastIndex){
        return OrderPurchasesInfinityScrollResponseDto.builder()
                .list(list)
                .hasNext(hasNext)
                .lastIndex(lastIndex)
                .build();
    }
}
