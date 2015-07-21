package org.rest.automation.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.model.Environment;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;


public class SpecFileProducer implements ThreadTask {

    private static Logger LOGGER = LogManager.getLogger(SpecFileProducer.class);

    private Environment environment;

    private BlockingQueue<File> filePileline;

    private int taskSize;

    private CountDownLatch countDownLatch;

    public BlockingQueue<File> getFilePileline() {
        return filePileline;
    }

    public void setFilePileline(BlockingQueue<File> filePileline) {
        this.filePileline = filePileline;
    }

    public void loadEnvironment(Environment environment) {
    this.environment=environment;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
    this.countDownLatch=countDownLatch;
    }

    public void setTaskSize(int size) {
     this.taskSize=size;
    }

    public void run() {
        for (File file : environment.getSpecList()) {
            LOGGER.debug("Adding file to pipeline " + file.getName());
            try {
                filePileline.put(file);
            }catch (InterruptedException e){
                LOGGER.error("thread interrupted");
            }
        }
        countDownLatch.countDown();

    }
}
