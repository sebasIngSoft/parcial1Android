package com.example.parcial1android.Modelo;

import java.io.Serializable;

public class Ubicacion implements Serializable {
    private int id_ubicacion;
    private String nombre;
    private String descripcion;
    private int color;
    private double latitud;
    private double longitud;
    private String username_fk;

    public Ubicacion() {
    }

    public Ubicacion(int id_ubicacion, String nombre, String descripcion, int color, double latitud, double longitud, String username_fk) {
        this.id_ubicacion = id_ubicacion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.color = color;
        this.latitud = latitud;
        this.longitud = longitud;
        this.username_fk = username_fk;
    }

    public int getId_ubicacion() {
        return id_ubicacion;
    }

    public void setId_ubicacion(int id_ubicacion) {
        this.id_ubicacion = id_ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getUsername_fk() {
        return username_fk;
    }

    public void setUsername_fk(String username_fk) {
        this.username_fk = username_fk;
    }
}
