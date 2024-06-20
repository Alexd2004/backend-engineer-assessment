package com.midas.app.activities;

import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.repositories.AccountRepository;

public class AccountActivityImpl implements AccountActivity {
  AccountRepository accountRepository;
  StripePaymentProvider stripePaymentProvider;

  @Override
  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  @Override
  public Account createPaymentAccount(Account account) {

    CreateAccount accountDetails =
        CreateAccount.builder()
            .userId(account.getId().toString())
            .firstName(account.getFirstName())
            .lastName(account.getLastName())
            .email(account.getEmail())
            .build();

    Account updatedAccount = stripePaymentProvider.createAccount(accountDetails);

    // Ensures all information stays the same
    updatedAccount.setId(account.getId());
    updatedAccount.setEmail(account.getEmail());
    updatedAccount.setFirstName(account.getFirstName());
    updatedAccount.setLastName(account.getLastName());
    updatedAccount.setProviderType(account.getProviderType());

    return accountRepository.save(updatedAccount);
  }
}
