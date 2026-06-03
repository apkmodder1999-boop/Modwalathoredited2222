// CRITICAL: Is line ko mat badalna! Jo aapki purani file me sabse pehli line (package ...) likhi thi, use hi yahan rehne dena.
package com.example.yourapp; 

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
        
        // Setup direct native view instance interface
        myWebView = new WebView(this);
        setContentView(myWebView);

        // Core system rules execution configurations to allow script rendering
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setDatabaseEnabled(true);
        
        // Fake Mobile Chrome client layout identification tag string
        myWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");

        // SMART LINK HIJACKING MATRIX (303 Interception Rules)
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString().toLowerCase();

                // Intercept execution constraints criteria matching targets
                if (url.contains("notjitu.in") || 
                    url.contains("t.me/notjitu2") || 
                    url.contains("t.me/notjitu")) {
                    
                    try {
                        // Force external device system app handler to capture target community channel
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+SDQNy0c8-p1iNDBl"));
                        startActivity(intent);
                        return true; // Halt native browser display layer pipelines completely
                    } catch (Exception e) {
                        // Fallback browser load safety in case device lacks custom apps
                        return false;
                    }
                }
                return false;
            }
        });

        // Your Exact Custom Target Web Interface Node
        String homeUrl = "https://www.notjitu.in/study-v2/batches/subject?batchid=6a0ae06d427dcbb4d1b4e73f&name=Victory%20Reloaded%202027%20(Class%2010th%20ICSE)";
        myWebView.loadUrl(homeUrl);
    }

    @Override
    public void onBackPressed() {
        if (myWebView != null && myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
