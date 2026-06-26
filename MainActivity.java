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
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webView;
    private final String targetTelegram = "https://t.me/notjitu2";
    
    // PERFECT PERMANENT HOME URL Set Here
    private final String homeUrl = "https://pwthor.live/study/batches";
    
    // ==========================================
    // EXPIRY SYSTEM CONFIGURATION
    // ==========================================
    private final long EXPIRY_TIME_MS = 1782552308000L; 
    // ==========================================

    private Handler urlCheckHandler = new Handler();
    private Runnable urlCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 1. Sabse pehle check karega ki app expire toh nahi hui
        if (isAppExpired()) {
            redirectToTelegramAndExit();
            return; 
        }

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

            // =======================================================
            // 🔥 EMBEDDED FEATURE: DOM ELEMENT EDIT & REMOVE MATRIX
            // =======================================================
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                String jsCode = "javascript:(function() { " +
                        "var runDOMElementsFix = setInterval(function() { " +
                        
                        // 1. Logo image ko remove karna [alt='PW THOR']
                        "var logoImg = document.querySelector(\"img[alt='PW THOR']\"); " +
                        "if(logoImg) { logoImg.remove(); } " +

                        // 2. Contact Us aur Donate Batch div ko completely remove karna
                        "var divs = document.querySelectorAll('div'); " +
                        "divs.forEach(function(div) { " +
                        "   if(div.innerText && div.innerText.includes('Contact Us')) { div.remove(); } " +
                        "   if(div.innerText && div.innerText.includes('Donate Batch')) { div.remove(); } " +
                        "}); " +

                        // 3. Avatar text 'TH' span ko remove karna aur 'PW THOR' ko 'STUDY PANDA' me badalna
                        "var spans = document.querySelectorAll('span'); " +
                        "spans.forEach(function(span) { " +
                        "   if(span.innerText === 'TH' && span.classList.contains('bg-muted')) { span.remove(); } " +
                        "   if(span.innerText === 'PW THOR' && span.classList.contains('font-semibold')) { " +
                        "       span.innerText = 'STUDY PANDA'; " +
                        "   } " +
                        "}); " +

                        "}, 300); " + 
                        
                        // Performance backup
                        "setTimeout(function() { clearInterval(runDOMElementsFix); }, 10000); " +
                        
                        "})()";

                view.loadUrl(jsCode);
            }
        });

        // Background real-time listener (Checks Next.js state shifts every 500ms)
        urlCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAppExpired()) {
                    redirectToTelegramAndExit();
                    return;
                }
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

        // App opens homeUrl directly
        webView.loadUrl(homeUrl);
    }

    private boolean isAppExpired() {
        return System.currentTimeMillis() >= EXPIRY_TIME_MS;
    }

    private void redirectToTelegramAndExit() {
        try {
            Toast.makeText(this, "App validity expired!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetTelegram));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            // Fallback
        }
        finish(); 
    }

    private boolean checkAndRedirect(String url) {
        String urlLower = url.toLowerCase();
        
        // Allowed links external launch bypass
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

        // FIXED: Empty string logic fixed so it doesn't auto-block your homeUrl
        boolean isMainBatchesPage = urlLower.endsWith("/study/batches") || urlLower.endsWith("/study/batches/");

        // STRICT PERMANENT BLOCK MATRIX
        if (urlLower.contains("t.me/pw_thor") || urlLower.contains("t.me/pwthor1") ||
            urlLower.contains("/contact") || urlLower.contains("/study/donate")) {
            
            try {
                webView.stopLoading();
                webView.loadUrl(homeUrl); 
                
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
