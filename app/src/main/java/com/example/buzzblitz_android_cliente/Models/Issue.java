package com.example.buzzblitz_android_cliente.Models;

public class Issue {
    String date;
    String titol;
    String informer;
    String message;

    public Issue(){

    }

    public Issue(String date, String titol, String sender, String message){
        this.date = date;
        this.titol = titol;
        this.informer = sender;
        this.message = message;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setTitol(String titol){
        this.titol = titol;
    }
    public String getTitol(){
        return this.titol;
    }
    public String getInformer() {
        return informer;
    }

    public void setInformer(String informer) {
        this.informer = informer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}