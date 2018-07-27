package biz.meziant.sbcamunda;

import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SbCamundaApplication.class})
@Deployment(resources = "bet.bpmn")
public class SbCamundaApplicationTestsWithCoverage extends AbstractProcessEngineRuleTest {

    @Before
    public void setUp() {
        autoMock("bet.bpmn");
    }

    @Rule
    @ClassRule
    public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();


    @Test
    public void start_and_finish_process() {

        Map<String, Object> variables = new HashMap<String, Object>();
        final ProcessInstance processInstance = rule.getRuntimeService().startProcessInstanceByKey("bet", variables);

        List<LockedExternalTask> tasks = rule.getExternalTaskService().fetchAndLock(10, "externalWorkerId")
                .topic("external-topic", 60L * 1000L)
                .execute();

        for (LockedExternalTask task : tasks) {
            try {
                rule.getExternalTaskService().complete(task.getId(), "externalWorkerId");
            } catch (Exception e) {
                //... handle exception
            }
        }

        Assert.assertNotNull(processInstance);
    }
}
