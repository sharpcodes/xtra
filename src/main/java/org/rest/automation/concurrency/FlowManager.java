package org.rest.automation.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.model.Environment;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Sets up the framework that triggers the flow of execution
 */
public class FlowManager {

    private static Logger LOGGER = LogManager.getLogger(FlowManager.class);

    private List<ThreadTask> jobRunners;
    private Environment environment;
    private SpecLoader specLoader;

    public SpecLoader getSpecLoader() {
        return specLoader;
    }

    public void setSpecLoader(SpecLoader specLoader) {
        this.specLoader = specLoader;
    }

    public List<ThreadTask> getJobRunners() {
        return jobRunners;
    }

    public void setJobRunners(List<ThreadTask> jobRunners) {
        this.jobRunners = jobRunners;
    }

    public void injectEnvironment(Environment environment){
        this.environment=environment;

    }


    public void begin(){
        LOGGER.info("Flow manager has been initialized succesfully");
        specLoader.loadEnvironment(environment);
        int taskSize=specLoader.getFileList().size();
        if(taskSize==0){
            LOGGER.debug("No spec files found. Exiting job");
            return;
        }
        LOGGER.debug("received thread tasks of size="+jobRunners.size());
        ExecutorService executorService= Executors.newFixedThreadPool(jobRunners.size());
        CountDownLatch countDownLatch=new CountDownLatch(jobRunners.size());
        for(ThreadTask threadTask:jobRunners){
            threadTask.setCountDownLatch(countDownLatch);
            threadTask.setTaskSize(taskSize);
            threadTask.loadEnvironment(environment);
            executorService.submit(threadTask);
        }
        try {
            countDownLatch.await();
            executorService.shutdown();
        }catch (InterruptedException e){
            executorService.shutdownNow();
        }

    }





}
