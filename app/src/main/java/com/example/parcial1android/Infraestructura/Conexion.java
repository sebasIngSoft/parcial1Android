package com.example.parcial1android.Infraestructura;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Conexion extends SQLiteOpenHelper {
    private static final String database = "UbicacionesMapa.db";
    /*Para manipular el registro que retorna la BD*/
    private static final SQLiteDatabase.CursorFactory factory = null;
    private static final int version = 1;

    /*Instancia de la bd*/
    SQLiteDatabase bd;

    /* Constructor si uno quiere especificar otra bd*/
    public Conexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*Para usar la bd establecida*/
    public Conexion(Context context){
        super(context, database,factory,
                version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table usuario(" +
                "nombre text, "+
                "username text primary key,"+
                "password text"+
                ")"
        );
        db.execSQL("create table ubicacion(" +
                "id_ubicacion integer primary key AUTOINCREMENT,"+
                "nombre text, "+
                "descripcion text,"+
                "color int,"+
                "latitud double,"+
                "longitud double,"+
                "username_fk text REFERENCES usuario ON DELETE CASCADE" +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists usuario");
        db.execSQL("drop table if exists ubicacion");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    public void cerrarConexion() { bd.close(); }

    public boolean ejecutarInsert(String tabla, ContentValues registro){
        try{
            bd = this.getWritableDatabase();

            int res = (int) bd.insert(tabla, null, registro);
            cerrarConexion();
            if(res != -1){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    public boolean ejecutarDelete(String tabla, String condicion){
        bd = this.getWritableDatabase();
        int cant =  bd.delete(tabla, condicion,null);
        cerrarConexion();
        if(cant >= -1){
            return true;
        }else{
            return false;
        }
    }

    public boolean ejecutarUpdate(String tabla, String condicion, ContentValues registro){
        try{
            bd = this.getWritableDatabase();

            int cant =  bd.update(tabla, registro, condicion, null);
            cerrarConexion();
            if(cant == 1){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    public Cursor ejecutarSearch(String consulta){
        try{
            bd = this.getWritableDatabase();
            Cursor fila =  bd.rawQuery(consulta, null);
            return fila;
        }catch (Exception e){
            cerrarConexion();
            return null;
        }
    }
}
