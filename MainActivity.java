package com.example.yourapp; // NOTE: Apne original package ka naam yahan rehne dena

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Native full screen frame creation
        myWebView = new WebView(this);
        setContentView(myWebView);

        // Core browser control settings to trick server security
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setDatabaseEnabled(true);
        myWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");

        // SMART LINK HIJACKING INTERCEPTOR (WebViewClient)
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString().toLowerCase();

                // 1. Target Redirect Channels Execution (303 Interception Rules)
                if (url.contains("notjitu.in") || 
                    url.contains("t.me/notjitu2") || 
                    url.contains("t.me/notjitu")) {
                    
                    // Trigger System Intent to safely bounce user to your Main Channel link
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+SDQNy0c8-p1iNDBl"));
                    startActivity(intent);
                    return true; // Stops the app from rendering the blocked domain screen
                }

                // For all other safe standard browsing layers
                return false;
            }
        });

        // Your Premium Home Target URL Node
        String homeUrl = "https://www.notjitu.in/study-v2/batches/subject?batchid=6a0ae06d427dcbb4d1b4e73f&name=Victory%20Reloaded%202027%20(Class%2010th%20ICSE)";
        myWebView.loadUrl(homeUrl);
    }

    // Android Hardware back button override handler
    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
