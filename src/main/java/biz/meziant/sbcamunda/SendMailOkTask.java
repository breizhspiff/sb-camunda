package biz.meziant.sbcamunda;

import org.camunda.bpm.client.ExternalTaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMailOkTask {

    private final static Logger logger = LoggerFactory.getLogger(SendMailOkTask.class);


    public static void main(String args[]) {
        logger.info("External task client \"SendMailOkTask\" start...");

        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:9000/rest")
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe("ok-email")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    try {
                        logger.info("OK email sent");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }

}
