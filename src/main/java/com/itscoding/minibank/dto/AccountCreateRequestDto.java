package com.itscoding.minibank.dto;
 
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
 
public class AccountCreateRequestDto {
 
    @NotBlank(message = "예금주명은 필수입니다.")
    private String ownerName;
 
    @NotNull
    @PositiveOrZero(message = "초기 입금액은 0 이상이어야 합니다.")
    private BigDecimal initialBalance;
 
    // --- Generate(⌘N / Alt+Insert)로 생성된 Getter / Setter ---
    public String getOwnerName() {
        return ownerName;
    }
 
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
 
    public BigDecimal getInitialBalance() {
        return initialBalance;
    }
 
    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
