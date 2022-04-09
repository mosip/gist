package io.mosip.print.listener.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class Helpers {

    public static String readFileFromResources(String filename) throws URISyntaxException, IOException {
        InputStream inputStream = Helpers.class.getClassLoader().getResourceAsStream(filename);
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        return writer.toString();
    }

    public static InputStream readStreamFromResources(String filename) {
        InputStream inputStream = Helpers.class.getClassLoader().getResourceAsStream(filename);
        return inputStream;
    }

    public static String readFileFromLocalPath(String filename) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filename));
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "UTF-8");
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
