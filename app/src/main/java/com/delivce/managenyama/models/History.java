package com.delivce.managenyama.models;

public class History {

    long id;
    int historyType;
    String reportStmt;

    public History() {
    }

    public History(long id, int historyType, String reportStmt) {
        this.id = id;
        this.historyType = historyType;
        this.reportStmt = reportStmt;
    }

    public History(int historyType, String reportStmt) {
        this.historyType = historyType;
        this.reportStmt = reportStmt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHistoryType() {
        return historyType;
    }

    public void setHistoryType(int historyType) {
        this.historyType = historyType;
    }

    public String getReportStmt() {
        return reportStmt;
    }

    public void setReportStmt(String reportStmt) {
        this.reportStmt = reportStmt;
    }
}
