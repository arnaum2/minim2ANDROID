package com.example.buzzblitz_android_cliente.Models;

public class UsuarioEnviar {


    private String id;
    private String name;
    private String pswd;
    private String mail;

    private int tarrosMiel;
    private int flor;
    private int mejorPuntuacion;
    private int numPartidas;
    private int floreGold;

    private String pregunta;
    private String respuesta;

    public UsuarioEnviar() {}

    public UsuarioEnviar(String id, String name, String pswd, String mail, String q, String a) { // Primera vez que entras
        this.id = id;
        this.name = name;
        this.pswd = pswd;
        this.mail = mail;
        this.tarrosMiel = 0;
        this.flor = 0;
        this.mejorPuntuacion = 0;
        this.numPartidas = 0;
        this.floreGold = 0;
        this.pregunta = q;
        this.respuesta = a;
    }
    // --- Getters y Setters básicos ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getTarrosMiel() {
        return tarrosMiel;
    }

    public void setTarrosMiel(int tarrosMiel) {
        this.tarrosMiel = tarrosMiel;
    }

    public int getFlor() {
        return flor;
    }

    public void setFlor(int flor) {
        this.flor = flor;
    }

    public int getMejorPuntuacion() {
        return mejorPuntuacion;
    }

    public void setMejorPuntuacion(int mejorPuntuacion) {
        this.mejorPuntuacion = mejorPuntuacion;
    }

    public int getNumPartidas() {
        return numPartidas;
    }

    public void setNumPartidas(int numPartidas) {
        this.numPartidas = numPartidas;
    }

    public int getFloreGold() {
        return floreGold;
    }

    public void setFloreGold(int floreGold) {
        this.floreGold = floreGold;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
