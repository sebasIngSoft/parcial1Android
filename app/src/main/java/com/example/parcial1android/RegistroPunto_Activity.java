package com.example.parcial1android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parcial1android.Controlador.CtlUbicacion;
import com.example.parcial1android.Controlador.CtlUsuario;
import com.example.parcial1android.Modelo.Ubicacion;

public class RegistroPunto_Activity extends AppCompatActivity {

    private EditText txtNombrePunto,txtDescripcion;
    private Spinner spnColor;
    private Button btnEditar,btnGuardar,btnEliminar;

    CtlUbicacion ctlUbicacion;
    CtlUsuario ctlUsuario;
    Ubicacion clsUbicacion;
    Ubicacion recibirUbicacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_punto_);

        Bundle bundle = getIntent().getExtras();
        try {
            clsUbicacion = (Ubicacion) bundle.getSerializable("clsUbicacion");
        }catch (Exception e){}

        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        spnColor = (Spinner) findViewById(R.id.spnColor);
        txtNombrePunto = (EditText) findViewById(R.id.txtNombrePunto);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);

        ctlUbicacion = new CtlUbicacion(this);
        ctlUsuario = new CtlUsuario(this);


        recibirUbicacion = ctlUbicacion.getUbicacion();
        if(recibirUbicacion!=null){
            System.out.println(recibirUbicacion.getNombre());
            btnEditar.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);
            btnGuardar.setVisibility(View.GONE);
            cargarDatos();
        }
        cargarColores();
    }

    private void cargarDatos() {
        txtNombrePunto.setText(recibirUbicacion.getNombre());
        txtDescripcion.setText(recibirUbicacion.getDescripcion());
        for(int i=0;i<10;i++){
            if(i==recibirUbicacion.getColor()){
                spnColor.setSelection(i);
            }
        }
    }

    public void cargarColores() {
        String[] opciones ={"HUE_YELLOW","HUE_AZURE","HUE_BLUE","HUE_CYAN","HUE_GREEN","HUE_MAGENTA","HUE_ORANGE","HUE_RED","HUE_ROSE","HUE_VIOLET"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spnColor.setAdapter(adapter);
    }

    public void registrar(View view){
        if (txtNombrePunto.getText().toString().equals("") || txtDescripcion.getText().toString().equals("")){
            Toast.makeText(this,"Por favor complete los campos",Toast.LENGTH_SHORT).show();
        }else{
            clsUbicacion.setNombre(txtNombrePunto.getText().toString());
            clsUbicacion.setDescripcion(txtDescripcion.getText().toString());
            clsUbicacion.setColor(spnColor.getSelectedItemPosition());

            if (ctlUbicacion.guardarObjeto(clsUbicacion)){
                Toast.makeText(this,"Registro Exitoso",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Map_Activity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"No sea completado el registro",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void editar(View v){
        if (txtNombrePunto.getText().toString().equals("") || txtDescripcion.getText().toString().equals("")){
            Toast.makeText(this,"Por favor complete los campos",Toast.LENGTH_SHORT).show();
        }else{
            System.out.println();
            if (ctlUbicacion.modificar(recibirUbicacion.getId_ubicacion(), txtNombrePunto.getText().toString(),txtDescripcion.getText().toString()
            ,spnColor.getSelectedItemPosition(),recibirUbicacion.getLatitud(),recibirUbicacion.getLongitud(), recibirUbicacion.getUsername_fk())){
                Toast.makeText(this,"Modificacion Exitosa",Toast.LENGTH_SHORT).show();
                btnEditar.setVisibility(View.GONE);
                btnEliminar.setVisibility(View.GONE);
                btnGuardar.setVisibility(View.VISIBLE);
                ctlUbicacion.setUbicacion(null);
                Intent intent = new Intent(this, Map_Activity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"No se ha podido modificar la ubicación.",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void eliminar(View v){
        if(ctlUbicacion.eliminar(recibirUbicacion.getUsername_fk(),recibirUbicacion.getId_ubicacion())){
            Toast.makeText(this,"Ubicación eliminada con exito.",Toast.LENGTH_SHORT).show();
            btnEditar.setVisibility(View.GONE);
            btnEliminar.setVisibility(View.GONE);
            btnGuardar.setVisibility(View.VISIBLE);
            ctlUbicacion.setUbicacion(null);
            Intent intent = new Intent(this, Map_Activity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this,"No se pudo eliminar",Toast.LENGTH_SHORT).show();
        }
    }
    public void regresar(View v){
        Intent intent = new Intent(this, Map_Activity.class);
        startActivity(intent);
    }
}