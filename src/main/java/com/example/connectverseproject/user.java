
package com.example.connectverseproject;

import java.util.*;

public class user {
    private int id;
    private String name;
    private String email;
    private List<post> posts;
    private String About;
    private String password;
    public user(int id, String name, String email,String about,String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.About=about;
        this.password=password;
        this.posts = new LinkedList<>();

    }
    public user( String name, String email,String about,String password) {
        this.name = name;
        this.email = email;
        this.About=about;
        this.password=password;
        this.posts = new LinkedList<>();

    }


  // add at start
    public void addPost(post po) {
        posts.addFirst(po);
    }


    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<post> getPosts() { return posts; }
    public String getAbout() { return About; }
    public String getPassword() { return password; }
    public void setusername(String u) { name=u; }
    public void setPassword(String p) { password=p; }
    public void setAbout(String p) { this.About=p; }

}