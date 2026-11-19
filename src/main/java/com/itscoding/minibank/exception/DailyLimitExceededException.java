package com.itscoding.minibank.exception;

import java.math.BigDecimal;

// 13장 1일 이체 한도 초과 — 메시지에 한도와 오늘 사용액을 함께 담아 사용자가 다음 행동을 정할 수 있게 한다
public class DailyLimitExceededException extends RuntimeException {
    public DailyLimitExceededException(String accountNumber, BigDecimal limit, BigDecimal usedToday) {
        super(String.format("1일 이체 한도를 초과했습니다. 계좌=%s, 한도=%,d원, 오늘 사용액=%,d원",
                accountNumber, limit.longValue(), usedToday.longValue()));
    }
}