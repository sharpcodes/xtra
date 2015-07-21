package org.rest.automation.decorators;

import org.rest.automation.model.Environment;

import java.util.List;


public class TestResultDecorator<T> {

    public List<Decorator<T>> decorators;
    private String outputPath;
    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void formatResult(T t) {
        if(decorators!=null) {
            for (Decorator<T> decorator : decorators) {
                decorator.setOutputPath(outputPath);
                if (decorator instanceof JUnitResultDecorator) {
                    if (environment.isJunitReportingEnabled()) {
                        decorator.formatResult(t);
                    }

                } else {
                    decorator.formatResult(t);
                }
            }
        }

    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public List<Decorator<T>> getDecorators() {
        return decorators;
    }

    public void setDecorators(List<Decorator<T>> decorators) {
        this.decorators = decorators;
    }
}
