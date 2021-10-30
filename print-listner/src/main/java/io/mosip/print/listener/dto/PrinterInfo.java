package io.mosip.print.listener.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PrinterInfo {
    private String Name;
    private String Default;
    private String PrinterPaperNames;
    private String PrinterState;
    private String PrinterStatus;
    private String WorkOffline;

    public void setValue(String key, String value) {
        switch (key) {
            case "Name" :
                this.Name = value;
                break;

            case "Default" :
                this.Default = value;
                break;

            case "PrinterPaperNames" :
                this.PrinterPaperNames = value;
                break;

            case "PrinterState" :
                this.PrinterState = value;
                break;

            case "PrinterStatus" :
                this.PrinterStatus = value;
                break;

            case "WorkOffline" :
                this.WorkOffline = value;
                break;
        }
    }
}
