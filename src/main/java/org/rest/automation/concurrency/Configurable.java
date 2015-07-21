package org.rest.automation.concurrency;

import org.rest.automation.model.Environment;


public interface Configurable {
    public void loadEnvironment(Environment environment);
}
