package com.itscoding.minibank.dto;
 
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountCreateRequestDto {

    @NotNull(message = "고객 ID는 필수입니다.")
    private Long customerId;

    @NotNull(message = "초기 입금액은 필수입니다.")
    @PositiveOrZero(message = "초기 입금액은 0 이상이어야 합니다.")
    private BigDecimal initialBalance;

}
