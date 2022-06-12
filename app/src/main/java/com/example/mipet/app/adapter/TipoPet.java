package com.example.mipet.app.adapter;

public class TipoPet {
    private String tipo;
    private int imagenTipo;

    public TipoPet(int imagenTipo, String tipo) {
        this.tipo = tipo;
        this.imagenTipo = imagenTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getImagenTipo() {
        return imagenTipo;
    }

    public void setImagenTipo(int imagenTipo) {
        this.imagenTipo = imagenTipo;
    }
}
