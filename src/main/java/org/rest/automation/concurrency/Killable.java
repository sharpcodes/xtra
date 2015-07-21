package org.rest.automation.concurrency;

import java.util.concurrent.CountDownLatch;


public interface Killable {

    public void setCountDownLatch(CountDownLatch countDownLatch);
    public void setTaskSize(int size);

}
