
package com.example.connectverseproject;
import java.time.LocalDateTime;

public class post implements Comparable<post> {
    int postId;
    private String content;
    private String interest;
    private LocalDateTime datetime;
    private int owner;
    int likes;

    public post(int postId,String content, String interest, LocalDateTime datetime) {
        this.content = content;
        this.postId=postId;
        this.interest = interest;
        this.datetime = datetime;

    }
    public post(String content, String interest, LocalDateTime datetime) {
        this.content = content;
        this.interest = interest;
        this.datetime = datetime;

    }

    // Getters
    public int getPostId() { return this.postId; }
    public String getContent() { return content; }
    public String getInterest() { return interest; }
    public LocalDateTime getDatetime() { return datetime; }
    public int getowner() { return owner; }
    public void setOwner(int o) { owner=o; }
    @Override
    public int compareTo(post other) {
        return other.getDatetime().compareTo(this.datetime);
    }
}