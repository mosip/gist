package io.mosip.print.listener.util;

import com.profesorfalken.wmi4java.WMI4Java;
import com.profesorfalken.wmi4java.WMIClass;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.print.listener.activemq.ActiveMQListener;
import io.mosip.print.listener.constant.LogMessageTypeConstant;
import io.mosip.print.listener.constant.LoggerFileConstant;
import io.mosip.print.listener.controller.base.BaseController;
import io.mosip.print.listener.controller.base.FXComponents;
import io.mosip.print.listener.dto.PrinterInfo;
import io.mosip.print.listener.exception.ExceptionUtils;
import io.mosip.print.listener.exception.PlatformErrorMessages;
import io.mosip.print.listener.logger.PrintListenerLogger;
import io.mosip.print.listener.service.impl.ClientServiceImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterState;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.*;

@Component
public class PrinterUtil {

    @Autowired
    private ActiveMQListener activeMQListener;

    @Autowired
    private Environment env;

    @Autowired
    FXComponents fxComponents;

    /** The print logger. */
    Logger clientLogger = PrintListenerLogger.getLogger(ClientServiceImpl.class);
    LinkedHashMap<String, PrinterInfo> printerInfoMap = new LinkedHashMap<>();

    public Boolean initiatePrint(String fileName) throws Exception{
        PDDocument document = null;
        File pdfFile = null;

        try {
            String filePath = env.getProperty("partner.pdf.download.path");
            if(!filePath.endsWith("/"))
                filePath = filePath + "/";

            pdfFile = new File( filePath + fileName);

            if(pdfFile == null || !pdfFile.exists()) {
                clientLogger.error(LoggerFileConstant.SESSIONID.toString(),
                        LoggerFileConstant.REGISTRATIONID.toString(), "Print Failed",
                                PlatformErrorMessages.PRT_ERROR.getCode() + " File " + fileName + " : Not found in the file storage");
                throw  new Exception("File " + fileName + " : Not found in the file storage");
            }

 //           PrintServiceAttributeSet set = printer.getAttributes();
  //          PrinterState state = printer.getAttribute(PrinterState.class);

            if (pdfFile != null) {
                PrintService printer = isPrinterOnLine();
                if (printer != null) {
                    document = PDDocument.load(pdfFile);
                    PrinterJob job = PrinterJob.getPrinterJob();
                    job.setPageable(new PDFPageable(document));
                    job.setPrintService(printer);
                    job.print();
                } else {
                    clientLogger.error(LoggerFileConstant.SESSIONID.toString(),
                            LoggerFileConstant.REGISTRATIONID.toString(), "Print Failed" ,
                                    PlatformErrorMessages.PRT_NOT_FOUND.getCode() + " " + PlatformErrorMessages.PRT_NOT_FOUND.getMessage());
                    //System.exit(1);
                }
            } else {
                clientLogger.error(LoggerFileConstant.SESSIONID.toString(),
                        LoggerFileConstant.REGISTRATIONID.toString(), "Print Failed",
                                PlatformErrorMessages.PDF_NOT_FOUND.getCode() + " " + PlatformErrorMessages.PDF_NOT_FOUND.getMessage());
                throw  new Exception(PlatformErrorMessages.PRT_NOT_FOUND.getMessage());
            }

            return true;
        } finally {
            if (document != null)
                document.close();
        }
    }

    public PrintService isPrinterOnLine() {
        try {
            BaseController.setPrinterOnline(false);
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService printer = null;
            String printerName = env.getProperty("partner.printer.name");

            if(printerName == null || printerName.isEmpty()) {
                 throw  new Exception(ApplicationResourceContext.getInstance().getLabelBundle().getString(PlatformErrorMessages.PRT_NOT_CONFIG.getCode()));
            }

            for (PrintService printService : printServices) {
                if (printService.getName().equals(printerName)) {
//                    PrintListenerLogger.println(LogMessageTypeConstant.INFO,
 //                           String.format(ApplicationResourceContext.getInstance().getLabelBundle().getString("message.printer.name"), printerName));
                    printer = printService;
                    break;
                }
            }

            if(printer != null) {
        if (printerInfoMap.containsKey(printerName)) {
            PrinterInfo info = printerInfoMap.get(printerName);
            if (info.getWorkOffline().toLowerCase().equals("false")) {
                        BaseController.setPrinterOnline(true);
                        return printer;
            } else {
                        throw  new Exception(ApplicationResourceContext.getInstance().getLabelBundle().getString(PlatformErrorMessages.PRT_OFFLINE.getCode()));
            }
        } else {
                    throw  new Exception(ApplicationResourceContext.getInstance().getLabelBundle().getString(PlatformErrorMessages.PRT_NOT_FOUND.getCode()));
                }
            } else {
                throw  new Exception(ApplicationResourceContext.getInstance().getLabelBundle().getString(PlatformErrorMessages.PRT_NOT_FOUND.getCode()));
        }
        } catch (Exception e) {
            clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "PrinterUtil","ERROR : " + e.getMessage());
            clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "PrinterUtil","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
            PrintListenerLogger.println(LogMessageTypeConstant.ERROR, e.getMessage());

            //System.exit(1);
        }
        return null;
    }

    public boolean isPrintArchievePathExist() {
        try {
        String filePath = env.getProperty("partner.pdf.download.path");

        if(filePath != null && !filePath.isEmpty()) {
            File pdfFile = new File(filePath);

            if(pdfFile.exists() && pdfFile.isDirectory())
                return true;
                else
                    throw  new Exception(PlatformErrorMessages.PRT_PATH_NOT_FOUND.getMessage());
            }
        } catch(Exception e) {
            clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "PrinterUtil","ERROR : " + e.getMessage());
            clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "PrinterUtil","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
            PrintListenerLogger.println(LogMessageTypeConstant.ERROR, e.getMessage());
            //System.exit(1);
        }
        return false;
    }

    public void printerHealthCheck() {
        boolean printRequired  = env.getProperty("mosip.print.pdf.printing.required", boolean.class);

        if(printRequired) {
            fxComponents.setTimer(new Timer("Print Health Check"));
            fxComponents.getTimer().schedule(new TimerTask() {

                @Override
                public void run() {
                    String printerDetails = WMI4Java
                            .get()
                            .properties(Arrays.asList("Name", "Default", "PrinterPaperNames", "PrinterState", "PrinterStatus",
                                    "WorkOffline"))
                            .getRawWMIObjectOutput(WMIClass.WIN32_PRINTER);

                    String[] values = printerDetails.replace("\r", "").split("\\n");

                    String printerName = null;
                    for (String val : values) {
                        if (!val.isEmpty()) {
                            String key = val.split(":")[0].trim();
                            String value = val.split(":")[1].trim();

                            if (key.equals("Name")) {
                                printerName = value;
                                if (!printerInfoMap.containsKey(printerName)) {
                                    PrinterInfo info = new PrinterInfo();
                                    info.setName(printerName);
                                    printerInfoMap.put(printerName, info);
                                }
                            }
                            printerInfoMap.get(printerName).setValue(key, value);
                        }
                    }
                    clientLogger.info(LoggerFileConstant.SESSIONID.toString(),
                            LoggerFileConstant.REGISTRATIONID.toString(), "PRINTER HEALTH CHECK - " + new Date(), printerInfoMap.toString());
                    isPrinterOnLine();
                }
            }, 0, 1 * 5 * 1000);
            activeMQListener.runQueue();
        } else {
            activeMQListener.runQueue();
        }
    }
}
