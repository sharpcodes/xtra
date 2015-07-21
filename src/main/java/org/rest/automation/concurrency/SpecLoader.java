package org.rest.automation.concurrency;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.model.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;


public class SpecLoader {

    private static Logger LOGGER = LogManager.getLogger(SpecLoader.class);


    public String getFilePathRoot() {
        return filePathRoot;
    }

    public void setFilePathRoot(String filePathRoot) {
        this.filePathRoot = filePathRoot;
    }

    private String filePathRoot;


    private List<File> fileList;

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }




    public  void loadSpecs() {
        List<File> fileList = new ArrayList<File>();

        File rootFolder = new File(filePathRoot);
        if (rootFolder.isDirectory()) {
            processDirectory(fileList, rootFolder);
        }
        if (rootFolder.isFile()) {
            fileList.add(rootFolder);
        }

       this.fileList=fileList;
    }


    /**
     * Function to load all files with their paths from the root directory
     *
     * @param files     container to hold all files that are being retrieved
     * @param directory the directory from which to start the search of files
     */
    public static void processDirectory(List<File> files, File directory) {

        if (directory != null && directory.isDirectory()) {
            List<File> fileData = new ArrayList<File>(Arrays.asList(directory.listFiles()));
            for (File fileEntry : fileData) {
                if (fileEntry.isFile() && fileEntry.getName().endsWith("xml")) {
                    LOGGER.debug("added file =" + fileEntry.getName() + " " + fileEntry.getPath());
                    files.add(fileEntry);
                }
                if (fileEntry.isDirectory()) {
                    processDirectory(files, fileEntry);
                }
            }

        }


    }

    /**
     * builds the directory structure along with the file when passed to the method
     *
     * @param directory name of the directory
     * @param file      name of the file to be created
     * @return
     */
    public static File getFileForDestination(String directory, String file) {

        String dir = directory;
        File fileDir = new File(dir);
        fileDir.mkdirs();
        File destinationFile = new File(fileDir, file);
        return destinationFile;

    }


    public static void makeDirectory(String path) {
        LOGGER.info("path is " + path);
        File file = new File(path);
        file.mkdirs();

    }


    public void loadEnvironment(Environment environment) {
        LOGGER.debug("setting file path " + environment.getCommandLine().getOptionValue("spec"));
        setFilePathRoot(environment.getCommandLine().getOptionValue("spec"));
        loadSpecs();
        environment.setSpecList(fileList);

    }


}
