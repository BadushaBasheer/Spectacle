package com.ecommerce.library.dto;

import com.ecommerce.library.enume.WalletTransactionType;
import com.ecommerce.library.model.Wallet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletHistoryDto {

    private Long id;

    @NotBlank
    @NotEmpty
    private double amount;

    private WalletTransactionType type;

    @NotBlank
    @NotEmpty
    private String transactionStatus;

    private Wallet wallet;

    private LocalDate transactionDate;

    private Long orderId;


}