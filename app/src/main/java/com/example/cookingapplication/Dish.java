package com.example.cookingapplication;

import java.io.Serializable;

public class Dish implements Serializable {


    private int id;
    private String title;
    private String describle;
    private String url_image;
    private String resources;
    private String steps;
    private String auth;
    private String time;
    private String serving;

    public Dish(){
        this.id=0;
        this.title="";
        this.time="";
        this.auth="";
        this.describle="";
        this.resources="";
        this.steps="";
        this.serving="";
        this.url_image="";
    }
    public  Dish(int id,String title,String auth,String time,String url_image){
        this.id=id;
        this.title=title;
        this.time=time;
        this.auth=auth;
        this.describle="";
        this.resources="";
        this.steps="";
        this.serving="";
        this.url_image=url_image;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescrible() {
        return describle;
    }

    public void setDescrible(String describle) {
        this.describle = describle;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

}
