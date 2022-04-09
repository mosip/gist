package io.mosip.print.listener.util;

import com.opencsv.CSVWriter;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.print.listener.logger.PrintListenerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CSVLogWriter {

    @Autowired
    Environment env;

    private static CSVWriter csvWriter;
    private static LinkedHashMap<String, String[]> logMap = new LinkedHashMap<>();
    Logger clientLogger = PrintListenerLogger.getLogger(CSVLogWriter.class);


    private CSVLogWriter() {
        File csvFile = new File("RIDPrintStatus.csv");
        try {
            if (!csvFile.exists()) {
                csvFile.createNewFile();
                csvWriter = new CSVWriter(new FileWriter(csvFile));
                String[] header = {"Print ID", "RID", "Print Status", "Status Time"};
                csvWriter.writeNext(header);
                csvWriter.flush();
            } else {
                csvWriter = new CSVWriter(new FileWriter(csvFile, true));
            }
        } catch(Exception e) {
            clientLogger.error("Print Listener", "CSVWritter", e.getMessage(), e.toString());
            System.exit(1);
        }
    }

    public static void writePrintStatus() {
        try {
            for(Map.Entry<String, String[]> entry : logMap.entrySet()) {
                csvWriter.writeNext(entry.getValue());
                csvWriter.flush();
            }
            logMap.clear();
        } catch(Exception e) {
            // Do Nothing
        }

    }

    public static void setLogMap(String key, String[] args) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String[] values = Arrays.copyOf(args, args.length + 1);
        values[3] = format.format(date);
        logMap.put(key, values);
    }
}
