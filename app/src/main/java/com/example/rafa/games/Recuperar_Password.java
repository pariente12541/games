package com.example.rafa.games;

import android.app.ProgressDialog;
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

public class Recuperar_Password extends AppCompatActivity implements View.OnClickListener {
    ObtenerWebService hiloConexionInsercion=null;

    String IP = "http://pruebareten.esy.es/Oscar/";
    String Insert=IP + "selectForSearch.php";

    //Componentes para enviar el codigo al correo
    EditText correo;
    Button   correobtn;

    //////Componenetes para enviar el código
    TextView codigotxt;
    EditText codigoedtxt;
    Button codigobtn;

    //Progress dialog
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar__password);


        ///Enviar el codigo al correo
        correo=(EditText)findViewById(R.id.correo_codigo);
        correobtn=(Button)findViewById(R.id.obtenerCodigo_btn);
        correobtn.setOnClickListener(this);

        /////////Enviar el codigo que se envio al correo
        codigotxt=(TextView)findViewById(R.id.textViewCodigo);
        codigoedtxt=(EditText)findViewById(R.id.codigoRecuperacion_edittxt);
        codigobtn=(Button)findViewById(R.id.enviarCodigo_btn);
        codigotxt.setVisibility(View.INVISIBLE);
        codigoedtxt.setVisibility(View.INVISIBLE);
        codigobtn.setVisibility(View.INVISIBLE);
        codigoedtxt.setEnabled(false);
        codigobtn.setEnabled(false);
        codigobtn.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
    }


    private void InsertarCodigoBD(String...p)  //Método para insertar La dirección en el Floating button de emergencias
    {
        hiloConexionInsercion=new ObtenerWebService();
        progressDialog.setMessage("Registrando");
        progressDialog.show();
        progressDialog.setCancelable(false);
        hiloConexionInsercion.execute(p[0],p[1]);
        /*
        p[0]=URL de lo que se va a hacer (insert, update o delete)
        p[1]=Correo
        */

    }
    @Override
    public void onClick(View view) {
    int id=view.getId();
    if(id==R.id.obtenerCodigo_btn)
    {
        if(!correo.getText().toString().equals(""))
        {
            InsertarCodigoBD(Insert,correo.getText().toString());
        }

    }
    if(id==R.id.enviarCodigo_btn)
    {

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
                        jsonparam.put("Correo",params[1].toString());
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
                        String claveJSON=respuestaJSON.getString("mensaje");
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
