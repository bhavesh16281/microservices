package com.bhavesh16281.accounts.service;

import com.bhavesh16281.accounts.dto.CustomerDTO;
import org.springframework.web.bind.annotation.RequestParam;

public interface AccountsService {

    void createAccount(CustomerDTO customerDTO);
    CustomerDTO getCustomerByPhone(String phone);
    boolean updateAccount(CustomerDTO customerDTO);
    boolean deleteAccount(String phone);
}
