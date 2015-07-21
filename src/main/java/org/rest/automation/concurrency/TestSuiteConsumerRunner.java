package org.rest.automation.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.executors.TestSuiteExecutor;
import org.rest.automation.model.Environment;
import org.rest.automation.model.TestSuite;

import java.util.concurrent.*;

/**
 * Sets up a threadpool that consumes {@link TestSuite} produced
 * by {@link FileConsumerTestSuiteProducer}
 * and populates the processed {@link TestSuite} instances
 * for {@link CompletedTestSuiteConsumer} to consume
 */
public class TestSuiteConsumerRunner implements ThreadTask {
    private static Logger LOGGER = LogManager.getLogger(TestSuiteConsumerRunner.class);

    private TestSuiteExecutor testSuiteExecutor;

    private CountDownLatch countDownLatch;

    private int taskSize;

    public TestSuiteExecutor getTestSuiteExecutor() {
        return testSuiteExecutor;
    }

    public void setTestSuiteExecutor(TestSuiteExecutor testSuiteExecutor) {
        this.testSuiteExecutor = testSuiteExecutor;
    }

    private BlockingQueue<TestSuite> testSuitePipeline;

    public BlockingQueue<TestSuite> getTestSuitePipeline() {
        return testSuitePipeline;
    }

    public void setTestSuitePipeline(BlockingQueue<TestSuite> testSuitePipeline) {
        this.testSuitePipeline = testSuitePipeline;
    }

    private BlockingQueue<TestSuite> completedTestPipeline;

    public BlockingQueue<TestSuite> getCompletedTestPipeline() {
        return completedTestPipeline;
    }

    public void setCompletedTestPipeline(BlockingQueue<TestSuite> completedTestPipeline) {
        this.completedTestPipeline = completedTestPipeline;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch=countDownLatch;
    }

    public void setTaskSize(int size) {
        this.taskSize=size;
    }


    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch taskCountDownLatch=new CountDownLatch(taskSize);
        boolean pipelineActive=true;
        try {
            TestSuite testSuite;
            LOGGER.debug("waiting for test suite to be availble ");
            while (pipelineActive) {
                testSuite = testSuitePipeline.poll(25l,TimeUnit.SECONDS);
                //look for completion signal from process upstream
                if (testSuite==null || (testSuite.getId()!=null && testSuite.getId().equals("end"))){
                    LOGGER.debug("received signal for shutdown ");
                    pipelineActive=false;

                }else if(testSuite!=null && (testSuite.getId()!=null && testSuite.getId().equals("error"))){
                    errorEntry();
                    taskCountDownLatch.countDown();
                }
                else {
                    LOGGER.debug("received testsuite " + testSuite.getId());
                   executorService.submit(new TestSuiteWorker(testSuiteExecutor, testSuite,completedTestPipeline,taskCountDownLatch));
                }
            }

            LOGGER.debug("waiting for all testsuite executions to be done ");
            taskCountDownLatch.await();
            LOGGER.debug("Shutting down");

            /*
            signal to the main flow manager that its done
             */
            countDownLatch.countDown();
            /*
            shutting down the testsuite executor that releases the http client resources
             */
            testSuiteExecutor.shutDown();

            executorService.shutdown();
        } catch (Exception e) {
            LOGGER.info("test suite processing thread interrupted",e);
        }  finally {

        }

    }
    public void loadEnvironment(Environment environment) {
        testSuiteExecutor.setEnvironment(environment);
    }


    public void errorEntry(){
        TestSuite testSuite=new TestSuite();
        testSuite.setId("error");
        try {
            completedTestPipeline.put(testSuite);
        } catch (InterruptedException e){
            LOGGER.debug("Thread interrupted");
        }

    }


}
