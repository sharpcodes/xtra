package org.rest.automation.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.executors.TestSuiteExecutor;
import org.rest.automation.model.TestSuite;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * Worker thread that executes the {@link TestSuite} which allows for test case executions
 */
public class TestSuiteWorker implements Callable<TestSuite>,Killable {

    private static Logger LOGGER = LogManager.getLogger(TestSuiteWorker.class);

    private TestSuiteExecutor testSuiteExecutor;

    private TestSuite testSuite;

    private BlockingQueue<TestSuite> testSuitePipeline;

    private int taskSize;

    private CountDownLatch taskCountDownLatch;

    public BlockingQueue<TestSuite> getTestSuitePipeline() {
        return testSuitePipeline;
    }

    public void setTestSuitePipeline(BlockingQueue<TestSuite> testSuitePipeline) {
        this.testSuitePipeline = testSuitePipeline;
    }

    public TestSuite getTestSuite() {
        return testSuite;
    }

    public void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public TestSuiteExecutor getTestSuiteExecutor() {
        return testSuiteExecutor;
    }

    public TestSuiteWorker(TestSuiteExecutor testSuiteExecutor, TestSuite testSuite, BlockingQueue<TestSuite> testSuitePipeline, CountDownLatch taskCountDownLatch) {
        this.testSuiteExecutor = testSuiteExecutor;
        this.testSuite = testSuite;
        this.testSuitePipeline = testSuitePipeline;
        this.taskCountDownLatch = taskCountDownLatch;
    }

    public TestSuite call(){
        LOGGER.info("Executing test suite " + testSuite.getId());
        try {
            testSuiteExecutor.execute(testSuite);
            testSuitePipeline.put(testSuite);
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted  when executing testSuiteId" + testSuite.getId());
        }
        catch (Exception e){
            LOGGER.error("Error occured ",e);
            LOGGER.debug("Adding error entry");
            errorEntry();
        }
        finally {
            taskCountDownLatch.countDown();

        }
        LOGGER.info("completed execution of testsuite " + testSuite.getId());
        return testSuite;

    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {

    }

    public void setTaskSize(int size) {

    }

    public void errorEntry(){
        TestSuite testSuite=new TestSuite();
        testSuite.setId("error");
        try {
            testSuitePipeline.put(testSuite);
        } catch (InterruptedException e){
            LOGGER.debug("Thread interrupted");
        }

    }


}
