package org.rest.automation.model;

import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.util.List;

/**
 * Created by sharpcodes on 5/20/15.
 */
public class Environment {

    private boolean junitReportingEnabled;
    private CommandLine commandLine;
    private List<File> specList;

    public List<File> getSpecList() {
        return specList;
    }

    public void setSpecList(List<File> specList) {
        this.specList = specList;
    }

    private Environment() {
    }

    public static Environment createInstance(CommandLine commandLine) {

        Environment environment = new Environment();
        environment.commandLine = commandLine;
        if (commandLine.hasOption("junit")) {
            environment.junitReportingEnabled = true;
        }

        return environment;

    }

    public boolean isJunitReportingEnabled() {
        return junitReportingEnabled;
    }

    public void setJunitReportingEnabled(boolean junitReportingEnabled) {
        this.junitReportingEnabled = junitReportingEnabled;
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }
}
