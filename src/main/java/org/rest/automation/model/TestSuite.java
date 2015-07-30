package org.rest.automation.model;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sharpcodes on 4/6/15.
 */

@XmlRootElement(name = "testSuite")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestSuite {


    @XmlAttribute
    private String id;


    @XmlElement(name = "testTask")
    private List<TestTask> testTasks;

    @XmlTransient
    private String filePath;

    private int errorCount;


    private int failureCount;


    @XmlElementWrapper(name="results")
    private Map<String, HttpResponseWrapper> testContext = new HashMap<String, HttpResponseWrapper>();

    public Map<String, HttpResponseWrapper> getTestContext() {
        return testContext;
    }

    public void setTestContext(Map<String, HttpResponseWrapper> testContext) {
        this.testContext = testContext;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public List<TestTask> getTestTasks() {
        return testTasks;
    }


    public void setTestTasks(List<TestTask> testTasks) {
        this.testTasks = testTasks;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }
}
