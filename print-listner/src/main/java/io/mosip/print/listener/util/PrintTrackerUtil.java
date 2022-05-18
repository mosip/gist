package io.mosip.print.listener.util;

import io.mosip.print.listener.constant.PrintTransactionStatus;
import io.mosip.print.listener.entity.PrintTracker;
import io.mosip.print.listener.repository.PrintTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.PrinterState;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Component
public class PrintTrackerUtil {

    @Autowired
    private PrintTrackerRepository printTrackerRepository;

    public void writeIntoPrintTracker(String[] args) {
        Optional<PrintTracker> printTrackerOPtional = printTrackerRepository.findById(args[0].toString());
        PrintTracker printTracker;

        if (!printTrackerOPtional.isEmpty() && printTrackerOPtional.isPresent()) {
            printTracker = printTrackerOPtional.get();
        } else {
            printTracker = new PrintTracker();
        }

        printTracker.setPrintId(args[0].toString());
        printTracker.setPrintStatus(args[2].toString());
        printTracker.setPrintTime(LocalDateTime.now());
        printTracker.setRid(args[1].toString());

        if (args[2].toString().equals(PrintTransactionStatus.SENT_FOR_PRINTING.toString())) {
            printTracker.setIsPrinted(null);
            printTracker.setComments(null);
        }

        if(args[4] != null)
            printTracker.setComments(args[4].toString());
        if(args[3] != null)
            printTracker.setComments(args[3].toString());
        printTrackerRepository.saveAndFlush(printTracker);
    }
}
