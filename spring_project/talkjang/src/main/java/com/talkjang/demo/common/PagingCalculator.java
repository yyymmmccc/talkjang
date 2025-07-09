package com.talkjang.demo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class PagingCalculator {

    public static Long pageCountCalc(Long page, Long pageSize){

        return (long) (Math.ceil(page / 10.0) * pageSize * 10 + 1);
    }
}
