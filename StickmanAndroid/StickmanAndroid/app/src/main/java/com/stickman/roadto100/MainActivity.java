package com.stickman.roadto100;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.webkit.WebViewAssetLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String HOST = "appassets.androidplatform.net";
    private static final String START_URL = "https://" + HOST + "/assets/game/index.html";

    private WebView webView;

    /** CDN URL -> bundled file in assets/libs/ (used for fully offline play). */
    private static final Map<String, String> OFFLINE_LIBS = new HashMap<>();
    static {
        OFFLINE_LIBS.put("https://cdnjs.cloudflare.com/ajax/libs/three.js/r160/three.min.js", "libs/three.min.js");
        OFFLINE_LIBS.put("https://cdn.jsdelivr.net/npm/three@0.160.0/build/three.min.js", "libs/three.min.js");
        OFFLINE_LIBS.put("https://cdn.jsdelivr.net/npm/@dimforge/rapier3d-compat@0.14.0/rapier.es.js", "libs/rapier.es.js");
        OFFLINE_LIBS.put("https://esm.sh/@dimforge/rapier3d-compat@0.14.0", "libs/rapier.es.js");
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        webView = new WebView(this);
        webView.setBackgroundColor(0xFF070B16);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);                 // localStorage = game saves
        s.setMediaPlaybackRequiresUserGesture(false); // sound effects
        s.setAllowFileAccess(false);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.setLongClickable(false);
        webView.setOnLongClickListener(v -> true);
        webView.setHapticFeedbackEnabled(false);

        final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                String local = OFFLINE_LIBS.get(url);
                if (local != null) {
                    try {
                        InputStream in = getAssets().open(local);
                        WebResourceResponse r = new WebResourceResponse("text/javascript", "UTF-8", in);
                        Map<String, String> h = new HashMap<>();
                        h.put("Access-Control-Allow-Origin", "*");
                        r.setResponseHeaders(h);
                        return r;
                    } catch (IOException ignored) {
                        // not bundled -> fall through to the network
                    }
                }
                return loader.shouldInterceptRequest(request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !HOST.equals(request.getUrl().getHost());
            }
        });
        webView.setWebChromeClient(new WebChromeClient());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Exit the game?")
                        .setPositiveButton("Exit", (d, w) -> finish())
                        .setNegativeButton("Stay", null)
                        .show();
            }
        });

        if (savedInstanceState != null) webView.restoreState(savedInstanceState);
        else webView.loadUrl(START_URL);
    }

    private void goImmersive() {
        WindowInsetsControllerCompat c = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        c.hide(WindowInsetsCompat.Type.systemBars());
        c.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }

    @Override protected void onResume()  { super.onResume(); goImmersive(); webView.onResume(); }
    @Override protected void onPause()   { webView.onPause(); super.onPause(); }
    @Override public void onWindowFocusChanged(boolean f) { super.onWindowFocusChanged(f); if (f) goImmersive(); }
    @Override protected void onSaveInstanceState(Bundle o) { super.onSaveInstanceState(o); webView.saveState(o); }
    @Override protected void onDestroy() { webView.destroy(); super.onDestroy(); }
}
