package com.vocalify.app;
import android.os.Bundle;
import android.webkit.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView v, WebResourceRequest r, WebResourceError e) {
                v.loadUrl("file:///android_asset/no_internet.html");
            }
        });
        webView.loadUrl("https://vocalify.my.id/id/");
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) { webView.goBack(); } 
        else { super.onBackPressed(); }
    }
    }
