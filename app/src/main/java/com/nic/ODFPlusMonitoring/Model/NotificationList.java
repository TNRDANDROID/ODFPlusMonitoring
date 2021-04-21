package com.nic.ODFPlusMonitoring.Model;

public class NotificationList {
    private String tittle;
    private String description;
    private String date;
    private String time;
    private String notification_date;
    private String note_entry_id;
    private String notification;

    public String getNotification_date() {
        return notification_date;
    }

    public NotificationList setNotification_date(String notification_date) {
        this.notification_date = notification_date;
        return this;
    }

    public String getNote_entry_id() {
        return note_entry_id;
    }

    public NotificationList setNote_entry_id(String note_entry_id) {
        this.note_entry_id = note_entry_id;
        return this;
    }

    public String getNotification() {
        return notification;
    }

    public NotificationList setNotification(String notification) {
        this.notification = notification;
        return this;
    }

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
