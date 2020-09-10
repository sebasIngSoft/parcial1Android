package com.example.parcial1android.DAO;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.ListAdapter;

import com.example.parcial1android.Infraestructura.Conexion;
import com.example.parcial1android.Modelo.Ubicacion;

import java.util.ArrayList;
import java.util.List;

public class UbicacionDAO {
    Conexion conex;

    public UbicacionDAO(Activity activity) {
        conex = new Conexion(activity);
    }

    public boolean guardar(Ubicacion ubicacion) {
        ContentValues registro = new ContentValues();
        registro.put("nombre", ubicacion.getNombre());
        registro.put("descripcion", ubicacion.getDescripcion());
        registro.put("color", ubicacion.getColor());
        registro.put("latitud", ubicacion.getLatitud());
        registro.put("longitud", ubicacion.getLongitud());
        registro.put("username_fk", ubicacion.getUsername_fk());
        return conex.ejecutarInsert("ubicacion", registro);
    }

    public Ubicacion buscar(String nombre, String username_fk) {
        Ubicacion ubicacion = null;
        String consulta = "select id_ubicacion, nombre, descripcion,color,latitud,longitud, username_fk"
                + " from ubicacion where "
                + " nombre = '" + nombre + "' and username_fk='"+username_fk+"'";
        Cursor temp = conex.ejecutarSearch(consulta);

        if (temp.getCount() > 0) {
            temp.moveToFirst();
            ubicacion = new Ubicacion(temp.getInt(0),
                    temp.getString(1), temp.getString(2),Integer.parseInt(temp.getString(3)),Double.parseDouble(temp.getString(4)),Double.parseDouble(temp.getString(5)),temp.getString(6));
        }
        conex.cerrarConexion();
        return ubicacion;
    }

    public boolean eliminar(Ubicacion ubicacion) {
        String tabla = "ubicacion";
        String condicion = "username_fk ='" + ubicacion.getUsername_fk()+"' and id_ubicacion="+ubicacion.getId_ubicacion();
        return conex.ejecutarDelete(tabla, condicion);
    }

    public boolean modificar(Ubicacion ubicacion) {
        String tabla = "ubicacion";
        String condicion = "username_fk ='" + ubicacion.getUsername_fk()+"' and id_ubicacion=" +ubicacion.getId_ubicacion()+"";

        ContentValues registro = new ContentValues();

        registro.put("nombre", ubicacion.getNombre());
        registro.put("descripcion", ubicacion.getDescripcion());
        registro.put("color", ubicacion.getColor());
        return conex.ejecutarUpdate(tabla, condicion, registro);
    }

    public List<Ubicacion> listarUbicacionesUsuario(String username){
        List<Ubicacion> lista = new ArrayList<>();
        String consulta = "select * from ubicacion where username_fk='"+username+"'";
        Cursor temp = conex.ejecutarSearch(consulta);

        if(temp.moveToFirst()){
            do{
                Ubicacion ubicacion = new Ubicacion(temp.getInt(0),
                        temp.getString(1), temp.getString(2),Integer.parseInt(temp.getString(3)),Double.parseDouble(temp.getString(4)),Double.parseDouble(temp.getString(5)),temp.getString(6));
                lista.add(ubicacion);
            }while(temp.moveToNext());
        }
        return lista;
    }
    public List<String> listarUbicacionesString(String username){
        List<String> lista = new ArrayList<>();
        String consulta = "select * from ubicacion where username_fk='"+username+"'";
        Cursor temp = conex.ejecutarSearch(consulta);

        if(temp.moveToFirst()){
            do{
                lista.add("Nombre : "+temp.getString(1)+" Descripcion : "+temp.getString(2));
            }while(temp.moveToNext());
        }
        return lista;
    }

    public Ubicacion buscarPorLongitudLatitud(double longitud, double latitud) {
        Ubicacion ubicacion = null;
        String consulta = "select id_ubicacion, nombre, descripcion,color,latitud,longitud, username_fk"
                + " from ubicacion where "
                + " latitud = " + latitud + " and longitud="+longitud+"";
        Cursor temp = conex.ejecutarSearch(consulta);

        if (temp.getCount()>0){
            temp.moveToFirst();
            ubicacion = new Ubicacion(temp.getInt(0),temp.getString(1),temp.getString(2),temp.getInt(3),temp.getDouble(4),temp.getDouble(5),temp.getString(6));
        }
        conex.cerrarConexion();
        return ubicacion;
    }
}
