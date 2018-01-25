package com.example.rafa.games;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
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

public class Registrar_Usuario extends AppCompatActivity implements View.OnClickListener{
    EditText usuario,contraseña,contraseñar,correo,telefono;
    Button registrarmebtn;
    String usuariostr,contraseñastr,contraseñarstr,correostr,telefonostr;
    Activity activity;
    ////IP de la URl del sitio que almacena los ficheros Php para conectarse con el JSON
    String IP = "http://pruebareten.esy.es/Oscar/";
    //Rutas de los servicios web
    String Insert = IP + "insertar_Registrar_Usuario.php";       //URL del script php para obtener los alumnos de la tabla Direcciones
    ObtenerWebService hiloConexionInsercion;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar__usuario);
        usuario=(EditText)findViewById(R.id.usuario_registro);
        contraseña=(EditText)findViewById(R.id.contraseña_registro);
        contraseñar=(EditText)findViewById(R.id.contraseñar_registro);
        correo=(EditText)findViewById(R.id.correo_registro);
        telefono=(EditText)findViewById(R.id.telefono_registro);
        registrarmebtn=(Button)findViewById(R.id.registrarmebtn_registro);
        registrarmebtn.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);

    }
    public boolean verificarCamposCompletos()   //Verifica que todos los campos esten llenos y retorna booleano
    {
        boolean todos=true;
        if(usuario.getText().toString().equals("") || contraseña.getText().toString().equals("") ||
                contraseñar.getText().toString().equals("") || correo.getText().toString().equals("") ||
                telefono.getText().toString().equals(""))
        {
            todos=false;
        }
        return todos;
    }
    public boolean verificarCcontraseñas() //Verifica que las contraseñas del edittext sean las mismas y retornar boolean
    {
        boolean correcta=false;
        if(contraseña.getText().toString().equals(contraseñar.getText().toString()))
        {
            correcta=true;
        }

        return correcta;
    }
    private void InsertarUsuarioBD(String...p)  //Método para insertar La dirección en el Floating button de emergencias
    {
        hiloConexionInsercion=new ObtenerWebService();
        progressDialog.setMessage("Registrando");
        progressDialog.show();
       progressDialog.setCancelable(false);

        hiloConexionInsercion.execute(p[0],p[1],p[2],p[3],p[4]);

        /*
        p[0]=URL de lo que se va a hacer (insert, update o delete)
        p[1]=Usuario
        p[2]=Contraseña
        p[3]=Correo
        p[4]=Telefono
        */

    }

    @Override
    public void onClick(View view) {
    int id=view.getId();

    if(id==R.id.registrarmebtn_registro)
    {
        if(verificarCamposCompletos())
        {
            if(verificarCcontraseñas())
            {
                usuariostr=usuario.getText().toString();
                contraseñastr=contraseñar.getText().toString();
                correostr=correo.getText().toString();
                telefonostr=telefono.getText().toString();
                //
                InsertarUsuarioBD(Insert,usuariostr,contraseñastr,correostr,telefonostr);
            }else
            {
                Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
            }
        }else
        {
            //Para que se vea mas estético, seria mejor un alert
            Toast.makeText(this,"Por favor, llene todos los campos e intentelo de nuevo",Toast.LENGTH_LONG).show();
        }
    }

    }

    public class ObtenerWebService extends AsyncTask<String,Void,String>
    {

        // LinkedList<Marker> listaMarcadores=new ;
        private String HTTPConexion(URL url, String cadena, JSONObject respuestaJSON)
        {
            String answer="";
            try
            {
                url= new URL(cadena);
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
                    respuestaJSON= new JSONObject(result.toString());
                    String resultJSON=respuestaJSON.getString("estado");
                    answer=resultJSON;

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return answer;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("1"))
            {
                Toast.makeText(getApplicationContext(),"Se ha registrado con exito",Toast.LENGTH_LONG).show();
            }else if(s.equals("2"))
            {
                Toast.makeText(getApplicationContext(),"El usuario ya existe, " +
                        "por favor intente con otro nombre de usuario",Toast.LENGTH_LONG).show();
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
            int devuelve=0;
            if(params[0]==Insert)    //Insertar dirección
            {
                try
                {
                    HttpURLConnection Urlconn;
                    DataOutputStream printout;
                    DataInputStream input;
                    url=new URL(cadena);
                    Urlconn=(HttpURLConnection)url.openConnection();
                    Urlconn.setDoInput(true);
                    Urlconn.setDoOutput(true);
                    Urlconn.setUseCaches(false);
                    Urlconn.setRequestProperty("Content-Type","application/json");
                    Urlconn.setRequestProperty("Accept","application/json");
                    Urlconn.connect();

                    JSONObject jsonparam=new JSONObject();

                    if(params[0].equals(Insert))
                    {
                        jsonparam.put("Usuario",params[1].toString());
                        jsonparam.put("Password",params[2].toString());
                        jsonparam.put("Correo",params[3].toString());
                        jsonparam.put("Telefono",params[4].toString());
                        Log.println(Log.ASSERT,"Log","Entra en el put");

                    }

                    // Devuelve 1 si la inserción fue correcta, si esta duplicado el usuario, devuelve un 2
                    OutputStream os=Urlconn.getOutputStream();
                    BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    writer.write(jsonparam.toString());
                    writer.flush();
                    writer.close();
                    int respuesta=Urlconn.getResponseCode();
                    StringBuilder result=new StringBuilder();
                    if(respuesta==HttpURLConnection.HTTP_OK)
                    {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(Urlconn.getInputStream()));
                        while((line=br.readLine())!=null)
                        {
                            result.append(line);
                        }
                        JSONObject respuestaJSON= new JSONObject(result.toString());
                        String resultJSON=respuestaJSON.getString("estado");
                        Log.println(Log.ASSERT,"Log 2",resultJSON);
                        if(resultJSON.equals("1"))
                        {
                            devuelve=1; //Direccion insertada correctamente
                        }else if(resultJSON.equals("2"))
                        {
                            devuelve=2; //Usuario duplicado, no se han insertado registros
                        }
                    }else
                    {
                        devuelve=3; //No hay conexion a internet
                    }



                } catch (MalformedURLException e) {
                    e.printStackTrace();


                } catch (IOException e) {
                    e.printStackTrace();


                } catch (JSONException e) {
                    e.printStackTrace();

                }
                return devuelve+"";
            }
            return null;
        }
    }


}