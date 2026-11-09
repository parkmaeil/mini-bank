package com.itscoding.minibank.dto;
 
import com.itscoding.minibank.validation.AccountNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
 
import java.math.BigDecimal;
 
@Getter
@Setter
public class TransferRequestDto {

    @AccountNumber
    @NotBlank(message = "출금 계좌번호는 필수입니다.")
    private String fromAccountNumber;

    @AccountNumber
    @NotBlank(message = "입금 계좌번호는 필수입니다.")
    private String toAccountNumber;
 
    @NotNull(message = "이체 금액은 필수입니다.")
    @Positive(message = "이체 금액은 0원보다 커야 합니다.")
    private BigDecimal amount;
}
