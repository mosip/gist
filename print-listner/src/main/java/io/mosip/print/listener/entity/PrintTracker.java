package io.mosip.print.listener.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class PrintTracker {
    @Id
    @Column(name = "print_id", nullable = false)
    private String printId;

    @Column(name = "rid", nullable = false)
    private String rid;

    @Column(name = "print_status", nullable = true)
    private String printStatus;

    @Column(name = "is_printed", nullable = true)
    private String isPrinted;

    @Column(name = "print_time", nullable = true)
    private LocalDateTime printTime;

    @Column(name = "Comments", nullable = true)
    private String comments;

    public String getPrintId() {
        return printId;
    }

    public void setPrintId(String printId) {
        this.printId = printId;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(String isPrinted) {
        this.isPrinted = isPrinted;
    }

    public LocalDateTime getPrintTime() {
        return printTime;
    }

    public void setPrintTime(LocalDateTime printTime) {
        this.printTime = printTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
