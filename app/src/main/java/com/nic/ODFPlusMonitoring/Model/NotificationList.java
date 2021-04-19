package com.nic.ODFPlusMonitoring.Model;

public class NotificationList {
    private String tittle;
    private String description;
    private String date;
    private String time;

    public String getTime() {
        return time;
    }

    public NotificationList setTime(String time) {
        this.time = time;
        return this;
    }

    public String getDate() {
        return date;
    }

    public NotificationList setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTittle() {
        return tittle;
    }

    public NotificationList setTittle(String tittle) {
        this.tittle = tittle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public NotificationList setDescription(String description) {
        this.description = description;
        return this;
    }
}
