
package com.example.connectverseproject;

import eu.hansolo.toolbox.time.DateTimes;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class post implements Comparable<post> {
    private String content;
    private String interest;
    private LocalDateTime datetime;
    private int owner;
    int likes;

    public post(String content, String interest, LocalDateTime datetime) {
        this.content = content;
        this.interest = interest;
        this.datetime = datetime;

    }

    // Getters
    public int getLikes() { return this.likes; }
    //public boolean getisLike() { return this.isLike; }
    public String getContent() { return content; }
    public String getInterest() { return interest; }
    public LocalDateTime getDatetime() { return datetime; }
    public int getowner() { return owner; }
    public void setOwner(int o) { owner=o; }
    @Override
    public int compareTo(post other) {
        // Latest post should come first → descending order
        return other.getDatetime().compareTo(this.datetime);
    }
}