package org.rest.automation.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.decorators.TestResultDecorator;
import org.rest.automation.model.TestSuite;

import java.util.concurrent.CountDownLatch;

/**
 * Created by sharpcodes on 6/18/15.
 *
 * Represents a worker thread marshalls {@link TestSuite} objects into XML
 */
public class CompletedTestSuiteWorker implements Runnable {

    private static Logger LOGGER = LogManager.getLogger(CompletedTestSuiteConsumer.class);

    private TestResultDecorator<TestSuite> resultDecorator;
    private TestSuite testSuite;
    private CountDownLatch countDownLatch;

    public CompletedTestSuiteWorker(TestResultDecorator<TestSuite> resultDecorator, TestSuite testSuite, CountDownLatch countDownLatch) {
        this.resultDecorator = resultDecorator;
        this.testSuite = testSuite;
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        resultDecorator.formatResult(testSuite);
        LOGGER.info("completed decoration "+testSuite.getId());
        countDownLatch.countDown();

    }
}
