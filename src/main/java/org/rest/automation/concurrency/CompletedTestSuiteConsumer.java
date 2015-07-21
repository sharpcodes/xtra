package org.rest.automation.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.decorators.TestResultDecorator;
import org.rest.automation.model.Environment;
import org.rest.automation.model.TestSuite;

import java.util.concurrent.*;

/**
 * Initiates a threadpool that consumes the {@link TestSuite} that has been
 * processed by {@link TestSuiteConsumerRunner} and then eventually
 * marshall them into the response xml
 */

public class CompletedTestSuiteConsumer implements ThreadTask {

    private static Logger LOGGER = LogManager.getLogger(CompletedTestSuiteConsumer.class);

    private BlockingQueue<TestSuite> completedTestPipeline;
    private TestResultDecorator<TestSuite> resultDecorator;
    private CountDownLatch countDownLatch;
    private int taskSize;
    public BlockingQueue<TestSuite> getCompletedTestPipeline() {
        return completedTestPipeline;
    }

    public void setCompletedTestPipeline(BlockingQueue<TestSuite> completedTestPipeline) {
        this.completedTestPipeline = completedTestPipeline;
    }

    public TestResultDecorator<TestSuite> getResultDecorator() {
        return resultDecorator;
    }

    public void setResultDecorator(TestResultDecorator<TestSuite> resultDecorator) {
        this.resultDecorator = resultDecorator;
    }


    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        boolean pipelineActive = true;
        TestSuite testSuite;
        CountDownLatch taskLatch = new CountDownLatch(taskSize);
        LOGGER.info("waiting for testsuite to be available for decoration ");
        while (pipelineActive) {
            try {
                testSuite = completedTestPipeline.poll(15l, TimeUnit.SECONDS);
                if (testSuite == null && !checkForActivepipeLine()) {
                    pipelineActive = false;
                    LOGGER.info("received shutdown signal");
                } else if (testSuite != null && testSuite.getId() != null && testSuite.getId().equals("error")) {
                    taskLatch.countDown();
                } else {
                    LOGGER.info("received  testsuite for decoration " + testSuite.getId());
                    executorService.submit(new CompletedTestSuiteWorker(resultDecorator, testSuite, taskLatch));
                }
            } catch (InterruptedException e) {
                LOGGER.info("decorator thread interrupted");
            } catch (Exception e) {
                LOGGER.error("Error when decortating testsuites", e);
                taskLatch.countDown();
            }
        }
        try {
            taskLatch.await();
            LOGGER.info("Shutting down");
            executorService.shutdown();
            countDownLatch.countDown();
        } catch (InterruptedException e) {
            LOGGER.info("decorator thread interrupted");

        }
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void setTaskSize(int size) {
        this.taskSize = size;
    }


    public void loadEnvironment(Environment environment) {
        resultDecorator.setOutputPath(environment.getCommandLine().getOptionValue("output"));
        resultDecorator.setEnvironment(environment);
    }

    public boolean checkForActivepipeLine() {
        try {
            LOGGER.debug("Scanning if upstream process is complete");
            TestSuite testSuite = completedTestPipeline.poll(30l, TimeUnit.SECONDS);
            if (testSuite == null) {
                return false;
            } else {
                return true;
            }
        } catch (InterruptedException e) {

        }
        return false;
    }

}
