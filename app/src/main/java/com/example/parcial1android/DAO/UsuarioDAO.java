package com.example.parcial1android.DAO;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.parcial1android.Infraestructura.Conexion;
import com.example.parcial1android.Modelo.Usuario;

public class UsuarioDAO {
    Conexion conex;

    public UsuarioDAO(Activity activity) {
        conex = new Conexion(activity);
    }

    public boolean guardar(Usuario usuario) {
        ContentValues registro = new ContentValues();
        registro.put("nombre", usuario.getNombre());
        registro.put("apellido", usuario.getApellido());
        registro.put("username", usuario.getUsername());
        registro.put("password", usuario.getPassword());
        return conex.ejecutarInsert("usuario", registro);
    }

    public Usuario buscar(String username, String password) {
        Usuario usuario = null;
        String consulta = "select nombre, apellido, username, password "
                + "from usuario where "
                + "username = '" + username + "' and password = '"+password+"'";
        Cursor temp = conex.ejecutarSearch(consulta);

        if (temp.getCount() > 0) {
            temp.moveToFirst();
            usuario = new Usuario(temp.getString(0),
                    temp.getString(1), temp.getString(2),temp.getString(3));
        }
        conex.cerrarConexion();
        return usuario;
    }

    public boolean eliminar(Usuario usuario) {
        String tabla = "usuario";
        String condicion = "username ='" + usuario.getUsername()+"'";
        return conex.ejecutarDelete(tabla, condicion);
    }

    public boolean modifcar(Usuario usuario) {
        String tabla = "usuario";
        String condicion = "username ='" + usuario.getUsername()+"'";

        ContentValues registro = new ContentValues();

        registro.put("nombre", usuario.getNombre());
        registro.put("apellido", usuario.getApellido());
        registro.put("username", usuario.getUsername());
        registro.put("password", usuario.getPassword());
        return conex.ejecutarUpdate(tabla, condicion, registro);
    }

    public Usuario buscarNombre(String username) {
        Usuario usuario = null;
        String consulta = "select nombre, apellido, username, password "
                + "from usuario where "
                + "username = '" + username + "'";
        Cursor temp = conex.ejecutarSearch(consulta);

        if (temp.getCount() > 0) {
            temp.moveToFirst();
            usuario = new Usuario(temp.getString(0),
                    temp.getString(1), temp.getString(2),temp.getString(3));
        }
        conex.cerrarConexion();
        return usuario;
    }
}
