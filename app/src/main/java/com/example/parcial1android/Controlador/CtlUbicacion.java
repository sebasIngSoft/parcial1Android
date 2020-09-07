package com.example.parcial1android.Controlador;

import android.app.Activity;

import com.example.parcial1android.DAO.UbicacionDAO;
import com.example.parcial1android.Modelo.Ubicacion;

import java.io.Serializable;
import java.util.List;

public class CtlUbicacion implements Serializable {
    UbicacionDAO dao;
    public static Ubicacion ubicacion;

    public CtlUbicacion(Activity activity){
        dao = new UbicacionDAO(activity);
    }

    public static Ubicacion getUbicacion() {
        return ubicacion;
    }

    public static void setUbicacion(Ubicacion ubicacion) {
        CtlUbicacion.ubicacion = ubicacion;
    }

    public boolean guardar(int id_ubicacion, String nombre,
                           String descripcion,int color,double latitud,double longitud, String username_fk){
        Ubicacion ubicacion = new Ubicacion(0, nombre, descripcion,color,latitud,longitud,username_fk);
        return dao.guardar(ubicacion);
    }

    public Ubicacion buscar(String username, String nombre){
        ubicacion = dao.buscar(nombre, username);
        return ubicacion;
    }

    public boolean eliminar(String username_fk, int id_ubicacion){
        Ubicacion ubicacion = new Ubicacion(id_ubicacion,"","",0,0,0,username_fk);
        return dao.eliminar(ubicacion);
    }

    public boolean modificar(int id_ubicacion, String nombre,
                             String descripcion,int color,double latitud,double longitud, String username_fk){
        Ubicacion ubicacion = new Ubicacion(id_ubicacion,nombre, descripcion,color,latitud,longitud,username_fk);
        return dao.modificar(ubicacion);
    }

    public List<Ubicacion> listaUbicacionesUsuario(String username){
        return dao.listarUbicacionesUsuario(username);
    }
    public List<String> listarUbicacionesString(String username){
        return dao.listarUbicacionesString(username);
    }
}
