package com.midas.app;

import static com.midas.app.workflows.CreateAccountWorkflow.QUEUE_NAME;

import com.midas.app.activities.AccountActivityImpl;
import com.midas.app.workflows.CreateAccountWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MidasApplication {

  public static void main(String[] args) {
    SpringApplication.run(MidasApplication.class, args);

    WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();
    WorkflowClient client = WorkflowClient.newInstance(serviceStub);
    WorkerFactory factory = WorkerFactory.newInstance(client);
    Worker worker = factory.newWorker(QUEUE_NAME);

    worker.registerWorkflowImplementationTypes(CreateAccountWorkflowImpl.class);
    worker.registerActivitiesImplementations(new AccountActivityImpl());

    factory.start();
  }
}
