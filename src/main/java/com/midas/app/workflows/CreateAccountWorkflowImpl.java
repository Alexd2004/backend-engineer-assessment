package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import io.temporal.workflow.Workflow;

public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {
  // Create a activity stub
  private final AccountActivity accountActivity = Workflow.newActivityStub(AccountActivity.class);

  @Override
  public Account createAccount(Account details) {
    Account savedAccount = accountActivity.saveAccount(details);
    return accountActivity.createPaymentAccount(savedAccount);
  }
}
