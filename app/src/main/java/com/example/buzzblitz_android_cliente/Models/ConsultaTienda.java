package com.example.buzzblitz_android_cliente.Models;

import java.util.List;

public class ConsultaTienda {
    private List<Objeto> consulta;

    public ConsultaTienda() {}
    public ConsultaTienda(List<Objeto> consulta) {
        this.consulta = consulta;
    }

    public List<Objeto> getConsulta() {
        return consulta;
    }
    public void setConsulta(List<Objeto> consulta) {
        this.consulta = consulta;
    }
}
