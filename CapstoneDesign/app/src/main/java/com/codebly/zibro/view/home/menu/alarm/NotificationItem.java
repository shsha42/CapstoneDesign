package com.codebly.zibro.view.home.menu.alarm;

public class NotificationItem {
    private String dateTime;
    private String message;

    public NotificationItem(String dateTime, String message) {
        this.dateTime = dateTime;
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }
}
