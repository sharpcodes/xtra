package org.rest.automation.Utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.rest.automation.concurrency.SpecLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileLoaderTest {

    List<File> files;


    @Test
    public void testFileTraversal() {

        try {
            //file1
            File file = new File("src/main/test/level1/level2/level3");
            file.mkdirs();
            File childFile = new File("src/main/test/level1/level2/level3/level3.xml");
            childFile.createNewFile();


            File file2 = new File("src/main/test/level1/level2/level2.xml");
            file2.createNewFile();

            File file3 = new File("src/main/test/level1/level1.xml");
            file3.createNewFile();

            files = new ArrayList<File>();

            File fileRoot = new File("src/main/test");

            SpecLoader.processDirectory(files, fileRoot);

            Assert.assertEquals(3, files.size());

        } catch (Exception e) {
            Assert.fail();

        }


    }

    @After
    public void cleanUp() {

        for (File file : files) {
            file.delete();
        }
        File file = new File("src/main/test/level1/level2/level3");
        file.delete();

        File file2 = new File("src/main/test/level1/level2");
        file2.delete();

        File file3 = new File("src/main/test/level1");
        file3.delete();

        File file4 = new File("src/main/test");
        file4.delete();


    }


}
