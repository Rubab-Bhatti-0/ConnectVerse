
package com.example.connectverseproject;

import eu.hansolo.toolbox.time.DateTimes;

import java.security.Timestamp;
import java.util.Date;

public class post implements Comparable<post> {
    private String content;
    private String interest;
    private Date datetime;
    private String owner;

    public post(String content, String interest, Date datetime) {
        this.content = content;
        this.interest = interest;
        this.datetime = datetime;
    }

    // Getters
    public String getContent() { return content; }
    public String getInterest() { return interest; }
    public Date getDatetime() { return datetime; }
    public String getowner() { return owner; }
    public void setOwner(String o) { owner=o; }
    @Override
    public int compareTo(post other) {
        // Latest post should come first → descending order
        return other.getDatetime().compareTo(this.datetime);
    }
}