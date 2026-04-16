package com.vocalify.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private FrameLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- LAYOUT UTAMA ---
        FrameLayout rootLayout = new FrameLayout(this);
        setContentView(rootLayout);

        // --- 1. SETTING WEBVIEW ---
        webView = new WebView(this);
        webView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        webView.setVisibility(View.INVISIBLE); // Sembunyikan dulu sampai web siap
        rootLayout.addView(webView);

        // --- 2. BUAT SPLASH SCREEN (Logo di Tengah) ---
        splashLayout = new FrameLayout(this);
        splashLayout.setBackgroundColor(Color.parseColor("#0f172a")); // Warna background gelap (sesuai web lo)
        
        ImageView logo = new ImageView(this);
        logo.setImageResource(R.mipmap.ic_launcher); // Ambil icon app lo
        FrameLayout.LayoutParams logoParams = new FrameLayout.LayoutParams(300, 300);
        logoParams.gravity = android.view.Gravity.CENTER;
        splashLayout.addView(logo);
        
        rootLayout.addView(splashLayout);

        // --- 3. MINTA IZIN NOTIFIKASI ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // --- 4. KONFIGURASI WEBVIEW ---
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36");

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Hilangkan Splash Screen dan munculkan Web setelah loading selesai
                splashLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });

        webView.addJavascriptInterface(new WebAppInterface(), "AndroidShare");
        webView.loadUrl("https://vocalify.my.id/id/");
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void share(String title, String text, String url) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text + " " + url);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, title));
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) { webView.goBack(); } 
        else { super.onBackPressed(); }
    }
            }

