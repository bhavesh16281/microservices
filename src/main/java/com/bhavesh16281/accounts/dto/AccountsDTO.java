package com.bhavesh16281.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountsDTO {

    @Pattern(regexp = "(^|[0-9]{10})",message = "Account number must be 10 digits")
    @NotEmpty(message = "Account number cannot be null or empty")
    private Long accountNumber;

    @NotEmpty(message = "Account type cannot be null")
    private String accountType;

    @NotEmpty(message = "Branch Address cannot be null or empty")
    private String branchAddress;
}
