package com.example.parcial1android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parcial1android.Controlador.CtlUsuario;

public class Registro_Usuario_Activity extends AppCompatActivity {
    private EditText nombre;
    private EditText apellido;
    private EditText username;
    private EditText password;
    private EditText verificarPassword;

    CtlUsuario gestionUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__usuario_);

        nombre = (EditText) findViewById(R.id.txtNombre);
        apellido = (EditText) findViewById(R.id.txtApellido);
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        verificarPassword = (EditText) findViewById(R.id.txtValidarPassword);
        gestionUsuarios = new CtlUsuario(this);
    }

    public void RegistrarUsuario(View v){
        String nombreUser = nombre.getText().toString();
        String apellidoUser = apellido.getText().toString();
        String usernameUser = username.getText().toString();
        String passwordUser = password.getText().toString();
        String verificarPasswordUser = verificarPassword.getText().toString();
         if(!nombreUser.equals("")&&!apellidoUser.equals("")&&!usernameUser.equals("")&&!passwordUser.equals("")
                &&!verificarPasswordUser.equals("")){

            if(passwordUser.equals(verificarPasswordUser)){
                if(gestionUsuarios.guardar(nombreUser, apellidoUser, usernameUser, passwordUser)){
                    Toast.makeText(getApplicationContext(), "Usuario registrado con exito!.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Este nombre de usuario ya existe.", Toast.LENGTH_SHORT).show();
                    username.setText("");
                }
            }else{
                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                verificarPassword.setText("");
            }
        }else{
             Toast.makeText(getApplicationContext(), "Rellenar todos los campos.", Toast.LENGTH_SHORT).show();
         }
    }
    public void RegresarLogin(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}