package biz.meziant.sbcamunda;

import org.camunda.bpm.client.ExternalTaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExternalTaskHandler {

    private final static Logger logger = LoggerFactory.getLogger(ExternalTaskHandler.class);

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:9000/rest")
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe("external-topic")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    logger.info("External task done!");

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}
