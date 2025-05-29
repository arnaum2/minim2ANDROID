package com.example.buzzblitz_android_cliente.Models;

public class Usulogin {

    private String idoname;
    private String pswd;


    public Usulogin() {}
    public Usulogin(String idoname, String pswd) {

        this.pswd = pswd;
        this.idoname = idoname;
    }

    public void setIdoname(String id) {
        this.idoname = id;
    }



    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getPswd() {
        return pswd;
    }
    public String getIdoname() {
        return idoname;
    }

}

