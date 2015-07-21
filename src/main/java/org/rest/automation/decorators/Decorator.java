package org.rest.automation.decorators;


public interface Decorator<T> {

    void formatResult(T t);

    void setOutputPath(String outputPath);


}
