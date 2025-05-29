package com.example.buzzblitz_android_cliente.Models;

public class Compra {
    private String usuarioId;
    private String objeto;

    public Compra() { }

    public Compra(String usuarioId, String objeto) {
        this.usuarioId = usuarioId;
        this.objeto = objeto;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }
}

