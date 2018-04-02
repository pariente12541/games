package com.example.rafa.games;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Registrar_Nueva_Password extends AppCompatActivity {
    //EditText para registrar nueva password
    //EditText nuevaPassword, confirmarNuevaPassword;
    EditText contraseña, contraseñar;
    //Boton para enviar nueva contraseña
    Button restablecerPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar__nueva__password);

        contraseña=findViewById(R.id.contraseña_restablecer);
        contraseñar=findViewById(R.id.contraseñar_restablecer);
        restablecerPassword=findViewById(R.id.restablecerPassword_btn);

        restablecerPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCcontraseñas()!=true){
                    Snackbar snackbar=Snackbar.make(view,"No son iguales",Snackbar.LENGTH_SHORT).setActionTextColor(Color.BLUE);
                    snackbar.show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Contraseñas iguales",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean verificarCcontraseñas() {
        boolean iguales=true;
        if (!contraseña.getText().toString().equals(contraseñar.getText().toString())) {
            iguales=false;
        }
        return iguales;
    }


}
