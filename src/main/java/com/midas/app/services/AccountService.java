package com.midas.app.services;

import com.midas.app.generated.model.AccountDto;
import com.midas.app.models.Account;
import java.util.List;
import java.util.Optional;

public interface AccountService {
  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createAccount(Account details);

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  List<Account> getAccounts();

  /**
   * Updates a already created account in the system
   *
   * @param optionalAccount holds the account that needs to be updated
   * @param accountDto holds the updated information
   * @return Account
   */
  Account updateAccount(Optional<Account> optionalAccount, AccountDto accountDto);
}
