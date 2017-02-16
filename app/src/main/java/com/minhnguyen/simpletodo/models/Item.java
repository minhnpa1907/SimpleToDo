package com.minhnguyen.simpletodo.models;

/**
 * Created by MINH NPA on 24 Sep 2016.
 */

public class Item {
    private int id;
    private String date;
    private String subject;
    private String content;
    private String color;
    private String status;

    public Item() {
    }

    public Item(int id, String date, String subject,
                String content, String color, String status) {
        super();
        this.id = id;
        this.date = date;
        this.subject = subject;
        this.content = content;
        this.color = color;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
