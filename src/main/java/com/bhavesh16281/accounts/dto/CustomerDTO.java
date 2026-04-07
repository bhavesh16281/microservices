package com.bhavesh16281.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    @NotEmpty(message = "Name cannot be null or empty")
    @Size(min = 5,max = 30,message = "The length of name must be between 5 and 30")
    private String name;

    @NotEmpty(message = "Email address cannot be null or empty")
    @Email(message = "Enter valid email")
    private String email;

    @Pattern(regexp = "(^|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String phone;
    private AccountsDTO accountsDTO;
}
