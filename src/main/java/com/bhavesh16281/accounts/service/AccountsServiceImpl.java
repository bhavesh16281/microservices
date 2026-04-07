package com.bhavesh16281.accounts.service;

import com.bhavesh16281.accounts.constants.AccountsConstants;
import com.bhavesh16281.accounts.dto.AccountsDTO;
import com.bhavesh16281.accounts.dto.CustomerDTO;
import com.bhavesh16281.accounts.entity.Accounts;
import com.bhavesh16281.accounts.entity.Customer;
import com.bhavesh16281.accounts.exception.CustomerAlreadyExistsException;
import com.bhavesh16281.accounts.exception.ResourceNotFoundException;
import com.bhavesh16281.accounts.mapper.AccountsMapper;
import com.bhavesh16281.accounts.mapper.CustomerMapper;
import com.bhavesh16281.accounts.repository.AccountsRepository;
import com.bhavesh16281.accounts.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDTO customerDTO) {

        Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByPhone(customer.getPhone());

        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer with phone number " + customer.getPhone() + " already exists.");
        }

        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createAccount(savedCustomer));
    }

    @Override
    public CustomerDTO getCustomerByPhone(String phone) {

        Customer customer = customerRepository.findByPhone(phone).orElseThrow(
                () -> new ResourceNotFoundException("Customer","mobileNumber",phone)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account","customerId",customer.getCustomerId().toString())
        );

        CustomerDTO customerDto =  CustomerMapper.mapToCustomerDto(customer, new CustomerDTO());
        customerDto.setAccountsDTO(AccountsMapper.mapToAccountsDto(accounts, new AccountsDTO()));

        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDTO customerDto) {

        boolean isUpdated = false;
        AccountsDTO accountsDto = customerDto.getAccountsDTO();
        if(accountsDto != null){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account","accountNumber",accountsDto.getAccountNumber().toString())
            );

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer","customerId",customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String phone) {
        Customer customer = customerRepository.findByPhone(phone).orElseThrow(
                () -> new ResourceNotFoundException("Customer","mobileNumber",phone)
        );

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }


    private Accounts createAccount(Customer customer){

        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNum = 10000000L+ new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNum);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }

}
