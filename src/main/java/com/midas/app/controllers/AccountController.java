package com.midas.app.controllers;

import com.midas.app.activities.AccountActivityImpl;
import com.midas.app.generated.api.AccountsApi;
import com.midas.app.generated.model.AccountDto;
import com.midas.app.generated.model.CreateAccountDto;
import com.midas.app.mappers.Mapper;
import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.services.AccountService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AccountController implements AccountsApi {
  private final AccountService accountService;
  private final Logger logger = LoggerFactory.getLogger(AccountController.class);
  private final AccountRepository accountRepository;
  private final AccountActivityImpl accountActivityImpl;
  ProviderType providerType;

  /**
   * POST /accounts : Create a new user account Creates a new user account with the given details
   * and attaches a supported payment provider such as &#39;stripe&#39;.
   *
   * @param createAccountDto User account details (required)
   * @return User account created (status code 201)
   */
  @Override
  public ResponseEntity<AccountDto> createUserAccount(CreateAccountDto createAccountDto) {
    logger.info("Creating account for user with email: {}", createAccountDto.getEmail());

    var account =
        accountService.createAccount(
            Account.builder()
                .firstName(createAccountDto.getFirstName())
                .lastName(createAccountDto.getLastName())
                .email(createAccountDto.getEmail())
                .providerType(providerType.STRIPE)
                .build());

    return new ResponseEntity<>(Mapper.toAccountDto(account), HttpStatus.CREATED);
  }

  /**
   * GET /accounts : Get list of user accounts Returns a list of user accounts.
   *
   * @return List of user accounts (status code 200)
   */
  @Override
  public ResponseEntity<List<AccountDto>> getUserAccounts() {
    logger.info("Retrieving all accounts");

    var accounts = accountService.getAccounts();
    var accountsDto = accounts.stream().map(Mapper::toAccountDto).toList();

    return new ResponseEntity<>(accountsDto, HttpStatus.OK);
  }

  /**
   * PATCH /accounts/{accountId} : Retrives user account, Updates and returns it
   *
   * @param optionalAccount holds the account that needs to be updated
   * @param accountDto holds the updated information
   * @return Updated Account (status code 200)
   */
  @Override
  public ResponseEntity<AccountDto> updateUserAccount(
      @PathVariable String accountId, @RequestBody AccountDto accountDto) {

    logger.info(" Updating account with the id of: {}", accountId);

    if (accountId == null) {
      logger.error(" accountId is null");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Optional<Account> optionalAccount = accountRepository.findById(accountId);
    if (!optionalAccount.isPresent()) {
      logger.error(" Account with the id of {} is not found", accountId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    Account updatedAccount = accountService.updateAccount(optionalAccount, accountDto);
    accountActivityImpl.saveAccount(updatedAccount);

    return new ResponseEntity<>(Mapper.toAccountDto(updatedAccount), HttpStatus.CREATED);
  }
}
