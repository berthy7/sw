package com.herprogramacion.alquileres;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webActivity extends AppCompatActivity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = (WebView) findViewById(R.id.web);
// Activamos javascript
        WebSettings webSettings = mWebView.getSettings();
      //  webSettings.setjavascriptEnabled(true);
// Url que carga la app (webview)
        mWebView.loadUrl("http://berthy-sw.esy.es/index.php?r=site/login");
// Forzamos el webview para que abra los enlaces internos dentro de la la APP
        mWebView.setWebViewClient(new WebViewClient());
// Forzamos el webview para que abra los enlaces externos en el navegador
        mWebView.setWebViewClient(new MyAppWebViewClient());
// FIN AGREGADO
    }

    // INI AGREGADO
    @Override
// Detectar cuando se presiona el botón de retroceso
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    // INI AGREGADO
    public class MyAppWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

// Url base de la APP (al salir de esta url, abre el navegador) poner como se muestra, sin http://
            if(Uri.parse(url).getHost().endsWith("berthy-sw.esy.es")) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }
// FIN AGREGADO
}
