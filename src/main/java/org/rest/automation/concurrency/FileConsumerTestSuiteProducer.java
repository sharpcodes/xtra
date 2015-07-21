package org.rest.automation.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.model.Environment;
import org.rest.automation.model.TestSuite;

import java.io.File;
import java.util.concurrent.*;

/**
 * A Runnable object that sets up a threadpool to consume spec files from a pipeline.
 * The pipeline is populated by {@link SpecLoader} thread upstream
 *
 */
public class FileConsumerTestSuiteProducer implements ThreadTask{

    private static Logger LOGGER = LogManager.getLogger(FileConsumerTestSuiteProducer.class);

    private BlockingQueue<File> filePileline;

    private BlockingQueue<TestSuite> testSuitePipeline;

    private CountDownLatch countDownLatch;

    private int taskSize;

    public BlockingQueue<TestSuite> getTestSuitePipeline() {
        return testSuitePipeline;
    }

    public void setTestSuitePipeline(BlockingQueue<TestSuite> testSuitePipeline) {
        this.testSuitePipeline = testSuitePipeline;
    }

    public BlockingQueue<File> getFilePileline() {
        return filePileline;
    }

    public void setFilePileline(BlockingQueue<File> filePileline) {
        this.filePileline = filePileline;
    }


    /**
     * Consume the {@link File} produced by {@link SpecLoader}
     *  and populate the next pipeline for {@link TestSuiteConsumerRunner} process to consume
     */
    public void run() {
        File file = null;
        boolean pipelineActive = true;

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch taskLatch = new CountDownLatch(taskSize);

        LOGGER.debug("waiting for file to be available ");
        while (pipelineActive) {
            try {
                file = filePileline.poll(5l, TimeUnit.SECONDS);
                //check for signal that indicates the end of upstream process
                if (file == null || file.getName().equals("end")) {
                    pipelineActive = false;
                } else {
                    LOGGER.info("processing spec-->" + file.getName());
                    executorService.submit(new MarshallerWorker(file, testSuitePipeline, taskLatch));

                }

            }
            catch(InterruptedException e){
            LOGGER.info("Thread interrupted");
        }
            catch (Exception e){
                LOGGER.error("unexpected error ",e);
                taskLatch.countDown();
            }


    }
        try {
         /*
            wait for all tasks to be completed
             */
            taskLatch.await();
        }catch (InterruptedException e){
            LOGGER.error("unexpected thread interruption error");
        }
        countDownLatch.countDown();
        executorService.shutdown();
        LOGGER.info("Shutting down");
    }
    public void setCountDownLatch(CountDownLatch countDownLatch) {
    this.countDownLatch=countDownLatch;
    }

    public void setTaskSize(int size) {
    this.taskSize=size;
    }


    public void loadEnvironment(Environment environment) {

    }

    public void shutDownPipeLine(){
        TestSuite testSuite=new TestSuite();
        testSuite.setId("end");
        try {
            testSuitePipeline.put(testSuite);
        }catch (InterruptedException e){
            LOGGER.debug("Thread interrupted");

        }
    }

    public void shutDownProcess(ExecutorService executorService){

    }

}
