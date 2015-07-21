package org.rest.automation.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.model.TestSuite;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 *
 * <p/>
 * Worker class that marshalls specs to {@link TestSuite}
 */
public class MarshallerWorker implements Runnable {

    private static Logger LOGGER = LogManager.getLogger(MarshallerWorker.class);


    private File file;
    private BlockingQueue<TestSuite> testSuitePipeline;
    private CountDownLatch countDownLatch;


    public MarshallerWorker(File file, BlockingQueue<TestSuite> testSuitePipeline, CountDownLatch countDownLatch) {
        this.file = file;
        this.testSuitePipeline = testSuitePipeline;
        this.countDownLatch = countDownLatch;
    }

    public void run(){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TestSuite.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            TestSuite suite = (TestSuite) jaxbUnmarshaller.unmarshal(file);
            suite.setFilePath(file.getParent());
            LOGGER.info("file unmarshalled successfully-->" + file.getName());
            testSuitePipeline.put(suite);
        } catch (JAXBException e) {
            LOGGER.error("error when unmarshalling-->" + file.getName(),e);
            errorEntry();
        }
        catch (InterruptedException e){
            LOGGER.error("Thread interrupted");
        }
        catch (Exception e){
            errorEntry();
            LOGGER.error("Unexpected Error",e);
        }
        finally {
            countDownLatch.countDown();
        }


    }

    public void errorEntry(){
        LOGGER.debug("Adding error entry");
        TestSuite testSuite=new TestSuite();
        testSuite.setId("error");
        try {
            testSuitePipeline.put(testSuite);
        } catch (InterruptedException e){
            LOGGER.debug("Thread interrupted");
        }

    }




}
