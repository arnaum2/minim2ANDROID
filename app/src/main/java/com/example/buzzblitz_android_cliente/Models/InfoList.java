package com.example.buzzblitz_android_cliente.Models;
import java.util.List;
public class InfoList {
    private List<Info> ranking;
    private int posicionUsuario;

    public InfoList() {}

    public InfoList(List<Info> ranking, int posicionUsuario) {
        this.ranking = ranking;
        this.posicionUsuario = posicionUsuario;
    }

    public List<Info> getRanking() {
        return ranking;
    }

    public void setRanking(List<Info> ranking) {
        this.ranking = ranking;
    }

    public int getPosicionUsuario() {
        return posicionUsuario;
    }

    public void setPosicionUsuario(int posicionUsuario) {
        this.posicionUsuario = posicionUsuario;
    }
}
