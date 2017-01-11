package com.herprogramacion.alquileres;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InmuebleActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWebView;
    TextView texto;
    String dato;
    String direccion;
    String direccion1;
    Button boton;
    String Latitud;
    String Longitud;



    String IP ="http://berthy-sw.esy.es/web/consultas/obtener_alumno_por_id.php";
   ObtenerWebService hiloconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inmueble);
      //  texto = (TextView) findViewById(R.id.texto);


        mWebView = (WebView) findViewById(R.id.web);
        boton = (Button) findViewById(R.id.boton);
        boton.setOnClickListener(this);
// Activamos javascript


    }

    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.boton:
                Intent intent= new Intent(this, MapsIActivity.class);
              //  String dato = peso.getText().toString();
             //   String dato2="1";

                intent.putExtra("LATITUD",Latitud);
                intent.putExtra("LONGITUD",Longitud);
                startActivity(intent);

                break;

        }

    }

    // INI AGREGADO
    public class MyAppWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

// Url base de la APP (al salir de esta url, abre el navegador) poner como se muestra, sin http://
            if(Uri.parse(url).getHost().endsWith(direccion1)) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }

    public void onStart(){
        super.onStart();

        Intent intent =getIntent();
        Bundle extras =intent.getExtras();



        if(extras!=null){
             dato =extras.getString("dato");
            WebSettings webSettings = mWebView.getSettings();
            //  webSettings.setjavascriptEnabled(true);
// Url que carga la app (webview)


            mWebView.loadUrl("http://berthy-sw.esy.es/index.php?r=anuncio/view&id="+dato);
// Forzamos el webview para que abra los enlaces internos dentro de la la APP
            mWebView.setWebViewClient(new WebViewClient());
// Forzamos el webview para que abra los enlaces externos en el navegador
            mWebView.setWebViewClient(new MyAppWebViewClient());
// FIN AGREGADO

            direccion1="berthy-sw.esy.es/index.php?r=anuncio/view&id="+dato;
           // texto.setText(direccion);
            hiloconexion = new ObtenerWebService();
            String cadenallamada = IP + "?idalumno="+dato;
            hiloconexion.execute(cadenallamada,"1");
        }

        // texto.setText(Latitud);

    }
    public class ObtenerWebService extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url= null;
            String devuelve ="";

            if(params[1]=="1"){   //consulta por id
                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent","Mozilla/49.0.2" + "(Linux; Android 2.1; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line);
                        }

                        JSONObject respuestaJSON = new JSONObject(result.toString());

                        String resultJSON = respuestaJSON.getString("estado");

                        if (resultJSON =="1" ){
                            Latitud  = respuestaJSON.getJSONObject("alumno").getString("Latitud");
                            Longitud  = respuestaJSON.getJSONObject("alumno").getString("Longitud");


                        } else if (resultJSON == "2") {
                            devuelve = "No hay clientes";
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return devuelve;
            }
            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            /*resultado.setText(s);*/
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
