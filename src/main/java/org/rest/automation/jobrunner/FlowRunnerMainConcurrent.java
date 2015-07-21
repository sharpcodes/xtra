package org.rest.automation.jobrunner;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.concurrency.FlowManager;
import org.rest.automation.model.Environment;
import org.rest.automation.properties.manager.PropertiesManagerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The main runner
 */
public class FlowRunnerMainConcurrent {

    private static Logger LOGGER;


    public static void main(String[] args) {

        try {
            for(int i=0;i<args.length;i++){
                if(args[i].equals("--logLevel") && args[i+1].equalsIgnoreCase("debug")){
                    System.setProperty("log4j.configurationFile", "log4j2-debug.xml");
                }
            }

            LOGGER = LogManager.getLogger(FlowRunnerMainConcurrent.class);
            CommandLineInitializer commandLineInitializer = new CommandLineInitializer();
            commandLineInitializer.initialize();

            if(args[0].equals("--help")){
                commandLineInitializer.printHelp();
                return;
            }
            ApplicationContext ctx = new ClassPathXmlApplicationContext("app-context.xml");
            /*
            Create environment object
             */
            CommandLine commandLine = commandLineInitializer.parseArguments(args);
            Environment environment=Environment.createInstance(commandLine);

            /*
            load properties
             */
            PropertiesManagerImpl propertiesManager = (PropertiesManagerImpl) ctx.getBean("propertiesManager");

            if(commandLine.hasOption("props")) {
                propertiesManager.loadProperties(commandLine.getOptionValue("props"));
            }else{
               propertiesManager.setNotLoaded(true);
            }
            LOGGER.info("preparing flow manager for execution");
            FlowManager flowManager=(FlowManager) ctx.getBean("flowManager");

            /*
            inject environment
             */
            flowManager.injectEnvironment(environment);
            flowManager.begin();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
