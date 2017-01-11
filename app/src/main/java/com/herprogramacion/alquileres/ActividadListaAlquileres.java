package com.herprogramacion.alquileres;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;
import com.herprogramacion.alquileres.provider.Contrato.Alquileres;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
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

public class ActividadListaAlquileres extends AppCompatActivity implements AdaptadorAlquileres.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    private RecyclerView listaUI;
    private LinearLayoutManager linearLayoutManager;
    private AdaptadorAlquileres adaptador;

    private ListView listView;
    ArrayList titulo=new ArrayList();
    ArrayList descripcion=new ArrayList();
    ArrayList imagen=new ArrayList();

    String IP = "http://berthy-sw.esy.es/web/consultas/casas.php";
    ObtenerWebService hiloconexion;
    JSONArray alumnosJSON;
    List<String> latitud= new ArrayList<String>();
    List<String> longitud= new ArrayList<String>();
    List<String> precio= new ArrayList<String>();
    List<String> ids= new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_lista_alquileres);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=(ListView)findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getApplicationContext(),"Posicion "+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),InmuebleActivity.class);
                String dato=String.valueOf(position+1);
                // Poner el Id de la imagen como extra en la intención
              intent.putExtra("dato",dato);

                // Aquí pasaremos el parámetro de la intención creada previamente
                startActivity(intent);
            }
        });


        descargarImagen();

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Filtro...", Snackbar.LENGTH_LONG)
                     .setAction("Acción", null).show();
            }
        });
*/
        // Preparar lista
        /*listaUI = (RecyclerView) findViewById(R.id.lista);
        listaUI.setHasFixedSize(true);
/
        linearLayoutManager = new LinearLayoutManager(this);
        listaUI.setLayoutManager(linearLayoutManager);

        adaptador = new AdaptadorAlquileres(this, this);
        listaUI.setAdapter(adaptador);

        // Iniciar loader*/
        getSupportLoaderManager().restartLoader(1, null, this);

    }

    private void descargarImagen() {
        titulo.clear();
        descripcion.clear();
        imagen.clear();
        final ProgressDialog progressDialog=new ProgressDialog(ActividadListaAlquileres.this);
        progressDialog.setMessage("Cargando Imagenes...");
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://berthy-sw.esy.es/web/documento/ver_documento.php/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if(statusCode==200){
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(new String(responseBody));
                        for(int i=0; i<jsonArray.length();i++){
                            imagen.add(jsonArray.getJSONObject(i).getString("Foto"));
                            titulo.add(jsonArray.getJSONObject(i).getString("IdInmueble"));
                            descripcion.add(jsonArray.getJSONObject(i).getString("Descripcion"));


                        }
                        listView.setAdapter(new ImagenAdapter(getApplicationContext()));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private class ImagenAdapter extends BaseAdapter {
        Context ctx;
        android.view.LayoutInflater LayoutInflater;
        SmartImageView smartImageView;
        TextView tvtitulo , tvdescripcion;
        String Titulo;
        String Descripcion;


        public ImagenAdapter(Context applicationContext) {
            this.ctx=applicationContext;
            LayoutInflater=(LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagen.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewGroup ViewGroup = (ViewGroup) LayoutInflater.inflate(R.layout.activity_main_items, null);
            smartImageView=(SmartImageView) ViewGroup.findViewById(R.id.imagen1);
            tvtitulo=(TextView)findViewById(R.id.tvtitulo);
            tvdescripcion=(TextView)findViewById(R.id.tvdescripcion);


            String urlfinal="http://berthy-sw.esy.es/images/"+imagen.get(i).toString();
            Rect rect=new Rect(smartImageView.getLeft(),smartImageView.getTop(),smartImageView.getRight(),smartImageView.getBottom());
            smartImageView.setImageUrl(urlfinal,rect);
            //  Titulo=titulo.get(i).toString();
            //  tvtitulo.setText(titulo.get(i).toString());

            //  tvdescripcion.setText(descripcion.get(i).toString());
            return ViewGroup;
        }
    }
    public void onStart() {
        super.onStart();
        hiloconexion = new ObtenerWebService();
        String cadenallamada = IP + "?idalumno=0";
        hiloconexion.execute(cadenallamada, "1");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actividad_lista_alquileres, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.cuenta:
                startActivity(new Intent(this, webActivity.class));
               // startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.btnbuscar:
                Intent intent1 = new Intent(this, Maps2Activity.class);
                intent1.putStringArrayListExtra("LATITUD", (ArrayList<String>) latitud);
                intent1.putStringArrayListExtra("LONGITUD", (ArrayList<String>) longitud);
                intent1.putStringArrayListExtra("PRECIO", (ArrayList<String>) precio);
                intent1.putStringArrayListExtra("IDS", (ArrayList<String>) ids);

                startActivity(intent1);
                break;

            case R.id.btnAnuncios:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putStringArrayListExtra("LATITUD", (ArrayList<String>) latitud);
                intent.putStringArrayListExtra("LONGITUD", (ArrayList<String>) longitud);
                intent.putStringArrayListExtra("PRECIO", (ArrayList<String>) precio);
                intent.putStringArrayListExtra("IDS", (ArrayList<String>) ids);



                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

   @Override
    public void onClick(AdaptadorAlquileres.ViewHolder holder, String idAlquiler) {
        Snackbar.make(findViewById(android.R.id.content), ":id = " + idAlquiler,
                Snackbar.LENGTH_LONG).show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Alquileres.URI_CONTENIDO, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (adaptador != null) {
            adaptador.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    public class ObtenerWebService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;
            String devuelve = "";

            if (params[1] == "1") {   //consulta por id
                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){


                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON



                        if (resultJSON=="1"){      // hay alumnos a mostrar
                            alumnosJSON = respuestaJSON.getJSONArray("alumnos");   // estado es el nombre del campo en el JSON
                            for(int i=0;i<alumnosJSON.length();i++){
                                devuelve = devuelve+alumnosJSON.getJSONObject(i).getString("Latitud") + " " +alumnosJSON.getJSONObject(i).getString("Longitud") + "\n";
                                latitud.add(alumnosJSON.getJSONObject(i).getString("Latitud"));
                                longitud.add(alumnosJSON.getJSONObject(i).getString("Longitud"));
                                precio.add(alumnosJSON.getJSONObject(i).getString("Precio"));
                                ids.add(alumnosJSON.getJSONObject(i).getString("IDS"));


                            }

                        }
                        else if (resultJSON=="2"){
                            devuelve = "No hay alumnos";
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
            //respuesta.setText(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
