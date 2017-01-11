package com.herprogramacion.alquileres;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList titulo=new ArrayList();
    ArrayList descripcion=new ArrayList();
    ArrayList imagen=new ArrayList();

    TextView tvtitulo , tvdescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.listView);



        descargarImagen();
    }

    private void descargarImagen() {
        titulo.clear();
        descripcion.clear();
        imagen.clear();
        final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
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
        LayoutInflater LayoutInflater;
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

}
