package org.vijay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GoEuroTestApplication.class)
@IntegrationTest
public class GoEuroTestApplicationIntegrationTests {

    @Autowired
    private GoEuroTestApplication goEuroTestApplication;

    @Test
    public void testRunNullParams() throws Exception {
        File file = new File(goEuroTestApplication.getOutputFilePath());
        if (file.exists()) {
            file.delete();
        }
        goEuroTestApplication.run(null);
        assertFalse(file.exists());
    }

    @Test
    public void testRunInvalidParams() throws Exception {
        File file = new File(goEuroTestApplication.getOutputFilePath());
        if (file.exists()) {
            file.delete();
        }
        goEuroTestApplication.run("Berlin");
        assertFalse(file.exists());
        goEuroTestApplication.run("", "");
        assertFalse(file.exists());
    }

    @Test
    public void testRunValidParams() throws Exception {
        File file = new File(goEuroTestApplication.getOutputFilePath());
        if (file.exists()) {
            file.delete();
        }
        goEuroTestApplication.run("Berlin", "");
        assertTrue(file.exists());
    }
}
