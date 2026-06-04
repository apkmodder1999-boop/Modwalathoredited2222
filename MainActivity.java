package com.example.webwrapper; // NOTE: Agar aapki repo me package name alag hai, toh pehli line vahi rehne dena.

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;
    private final String targetTelegram = "https://t.me/+SDQNy0c8-p1iNDBl";
    // PERMANENT HOME URL
    private final String homeUrl = "https://pwthor.live/study/batches/6a0ae06d427dcbb4d1b4e73f";
    private Handler urlCheckHandler = new Handler();
    private Runnable urlCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                return checkAndRedirect(url);
            }
        });

        // Background loop checking url every 500ms
        urlCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (webView != null) {
                    String currentUrl = webView.getUrl();
                    if (currentUrl != null) {
                        checkAndRedirect(currentUrl);
                    }
                }
                urlCheckHandler.postDelayed(this, 500);
            }
        };
        urlCheckHandler.postDelayed(urlCheckRunnable, 500);

        // Directly open your specific batch URL permanently
        webView.loadUrl(homeUrl);
    }

    private boolean checkAndRedirect(String url) {
        String urlLower = url.toLowerCase();
        
        // Allowed External Links
        if (urlLower.contains("download.pwthor.live") || url.equals(targetTelegram)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                return false; 
            }
        }

        // Check if user is navigating to the exact main batches list page
        boolean isMainBatchesPage = urlLower.endsWith("/study/batches") || urlLower.endsWith("/study/batches/");

        // STRICT PERMANENT BLOCK LIST (Specific batch page is completely safe and excluded)
        if (urlLower.contains("t.me/pw_thor") || urlLower.contains("t.me/pw_thor1") ||
            urlLower.contains("/contact") || urlLower.contains("/study/donate") ||
            isMainBatchesPage) {
            
            try {
                webView.stopLoading();
                webView.loadUrl("https://pwthor.live/study"); // Redirect inside app to fallback zone
                
                // Force bounce to your Telegram channel
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetTelegram));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (urlCheckHandler != null && urlCheckRunnable != null) {
            urlCheckHandler.removeCallbacks(urlCheckRunnable);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            moveTaskToBack(true);
        }
    }
                }
