package org.rest.automation.Utils;

import org.junit.Assert;
import org.junit.Test;
import org.rest.automation.model.ProcessStatus;
import org.rest.automation.model.TestTask;
import org.rest.automation.specutil.NullCheckWrapperUtil;


public class NotNullWrapperUtilTest {


    @Test
    public void checkHappyPath() {

        TestTask testTask = new TestTask();
        ProcessStatus result = NullCheckWrapperUtil.checkIfNotNull(testTask, "id");
        Assert.assertEquals(result.isPassed(), false);
    }

    @Test
    public void checkInvalidFields() {

        TestTask testTask = new TestTask();
        ProcessStatus result = NullCheckWrapperUtil.checkIfNotNull(testTask, "ids");
        Assert.assertEquals(result.isPassed(), false);
        Assert.assertEquals("Unexpected error ids", result.getMessage());
    }


    @Test
    public void checkNullObject() {

        TestTask testTask = new TestTask();
        ProcessStatus result = NullCheckWrapperUtil.checkIfNotNull(null, "ids");
        Assert.assertEquals(result.isPassed(), false);

    }


}
