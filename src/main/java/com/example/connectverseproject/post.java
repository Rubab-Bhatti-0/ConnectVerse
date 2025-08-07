
package com.example.connectverseproject;

public class post {
    private String content;
    private String interest;
    private String datetime;

    public post(String content, String interest, String datetime) {
        this.content = content;
        this.interest = interest;
        this.datetime = datetime;
    }

    // Getters
    public String getContent() { return content; }
    public String getInterest() { return interest; }
    public String getDatetime() { return datetime; }
}