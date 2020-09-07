package com.example.parcial1android.Controlador;

import android.app.Activity;

import com.example.parcial1android.DAO.UsuarioDAO;
import com.example.parcial1android.Modelo.Usuario;

import java.io.Serializable;

public class CtlUsuario implements Serializable {
    UsuarioDAO dao;
    public static Usuario usuario;

    public CtlUsuario(Activity activity){
        dao = new UsuarioDAO(activity);
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        CtlUsuario.usuario = usuario;
    }

    public boolean guardar(String nombre,
                           String apellido, String username, String password){
        Usuario usuario = new Usuario(nombre, apellido, username, password);
        return dao.guardar(usuario);
    }

    public Usuario buscar(String username, String password){
        usuario = dao.buscar(username,password);
        return usuario;
    }

    public boolean eliminar(String username){
        Usuario usuario = new Usuario("","",username,"");
        return dao.eliminar(usuario);
    }

    public boolean modificar(String nombre,
                             String apellido, String username, String password){
        Usuario usuario = new Usuario(nombre, apellido, username, password);
        return dao.modifcar(usuario);
    }
}
