package com.example.rafa.games;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login_Xbox extends AppCompatActivity implements View.OnClickListener{
    Button btnIngresar;
    TextView txtRegistrar,txtRecuperar;
    EditText usuarioEdit,passwordEdit;
    int estado;
    String IP = "http://pruebareten.esy.es/Oscar/";
    //Rutas de los servicios web
    String Select = IP + "select_For_Login.php";       //URL del script php para obtener los alumnos de la tabla Direcciones
    ObtenerWebService hiloConexionSeleccion;
    private ProgressDialog progressDialog;
    Intent nuevo,recuperar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__xbox);
        btnIngresar=(Button)findViewById(R.id.ingresar_btn);
        txtRegistrar=(TextView)findViewById(R.id.registrar_usuario_txt);
        txtRecuperar=(TextView)findViewById(R.id.recuperar_password_txt);
        usuarioEdit=(EditText)findViewById(R.id.nombre_usuario_txt);
        passwordEdit=(EditText)findViewById(R.id.password_usuario_txt);
        btnIngresar.setOnClickListener(this);
        txtRegistrar.setOnClickListener(this);
        txtRecuperar.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);

    }
    private void SelectForLoginBD(String...p)  //Método para insertar La dirección en el Floating button de emergencias
    {
        hiloConexionSeleccion=new ObtenerWebService();
        progressDialog.setMessage("Verificando e ingresando");
        progressDialog.show();
        progressDialog.setCancelable(false);
        hiloConexionSeleccion.execute(p[0],p[1],p[2]);

        /*
        p[0]=URL de lo que se va a hacer (select,insert, update o delete)
        p[1]=Usuario
        p[2]=Contraseña
        */

    }
    @Override
    public void onClick(View view) {
        int id=view.getId();

        switch (id)
        {
            case R.id.ingresar_btn:
                String usuarioStr=usuarioEdit.getText().toString();
                usuarioStr.replaceAll("\\s","");
                String passwordStr=passwordEdit.getText().toString();
               SelectForLoginBD(Select,usuarioStr,passwordStr);

                break;
            case R.id.registrar_usuario_txt:
                nuevo=new Intent(this,Registrar_Usuario.class);
                startActivity(nuevo);
                break;

            case R.id.recuperar_password_txt:
                recuperar=new Intent(this,Recuperar_Password.class);
                startActivity(recuperar);
                break;
        }
    }


    public class ObtenerWebService extends AsyncTask<String,Void,String>
    {
        String enlace;


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("1"))
            {
                nuevo=new Intent(getApplicationContext(),principal.class);
                startActivity(nuevo);
                Toast.makeText(getApplicationContext(),"Correcto",Toast.LENGTH_LONG).show();
            }else if(s.equals("2"))
            {
                Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecta",Toast.LENGTH_LONG).show();
            }else if(s.equals("exception"))
            {
                Toast.makeText(getApplicationContext(),"Se produjo un error, por favor inténtelo de nuevo",Toast.LENGTH_LONG).show();
            }else if(s.equals("3"))
            {
                Toast.makeText(getApplicationContext(),"Se necesita blabla",Toast.LENGTH_LONG).show();

            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(String... params) {

            String cadena=params[0];
            URL url=null;   //Url de la información


            if(params[0]==Select) //Consultar por Id
            {
                enlace=params[0];
                String ret="";
                enlace=enlace+"?Usuario="+params[1];
                enlace=enlace+"&Password="+params[2];
                Log.println(Log.ASSERT,"Enlace: ",enlace);


                try
                {
                    url= new URL(enlace);
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0"+
                            " (Linux; Android 1.5; es-Es) Ejemplo HTTP");
                    int respuesta=connection.getResponseCode();
                    StringBuilder result=new StringBuilder();
                    if(respuesta==HttpURLConnection.HTTP_OK)
                    {
                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        BufferedReader reader= new BufferedReader(new InputStreamReader(in));
                        String line;
                        while((line=reader.readLine())!=null)
                        {
                            result.append(line);
                        }
                        JSONObject respuestaJSON= new JSONObject(result.toString());
                        String resultJSON=respuestaJSON.getString("estado");

                        if(resultJSON.equals("1")) {

                            ret="1";    //Correcto, ingresar

                        }else if (resultJSON.equals("2"))
                        {
                            ret="2";    //Usuario o contraseña incorrectos
                        }else if(resultJSON.equals("3"))
                        {
                            ret="3";
                        }


                    }else
                    {
                        ret="4";
                    }

                } catch (MalformedURLException e) {
                    ret="exception";
                    e.printStackTrace();
                } catch (IOException e) {
                    ret="exception";
                    e.printStackTrace();
                } catch (JSONException e) {
                    ret="exception";
                    e.printStackTrace();
                }

                return ret;
            }

            return null;
        }
    }
}
