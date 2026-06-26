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
    private final String homeUrl = "https://pwthor.live/study";
    
    // ==========================================
    // EXPIRY SYSTEM CONFIGURATION
    // ==========================================
    // Is line par aapko apna expiry time Unix Timestamp (Milliseconds) me dalna hai.
    // Current target: 1787580111000L
    private final long EXPIRY_TIME_MS = 1782521070000L; 
    // ==========================================

    private Handler urlCheckHandler = new Handler();
    private Runnable urlCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 1. Sabse pehle check karega ki app expire toh nahi hui
        if (isAppExpired()) {
            redirectToTelegramAndExit();
            return; // Aage ka WebView load nahi hoga
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

                        "}, 300); " + // Har 300ms me checking chalegi dynamic elements ke liye
                        
                        // Performance backup: 10 seconds baad interval clear ho jayega
                        "setTimeout(function() { clearInterval(runDOMElementsFix); }, 10000); " +
                        
                        "})()";

                // Javascript inject karna
                view.loadUrl(jsCode);
            }
        });

        // Background real-time listener (Checks Next.js state shifts every 500ms)
        urlCheckRunnable = new Runnable() {
            @Override
            public void run() {
                // Real-time loop me bhi check karega agar runtime me expire ho jaye
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

        // App opens directly to your requested specific batch page permanently
        webView.loadUrl(homeUrl);
    }

    // App expiry check karne ka helper method
    private boolean isAppExpired() {
        return System.currentTimeMillis() >= EXPIRY_TIME_MS;
    }

    // Expire hone par direct Telegram open karne aur app close karne ka method
    private void redirectToTelegramAndExit() {
        try {
            Toast.makeText(this, "App validity expired!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetTelegram));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            // Fallback
        }
        finish(); // App activity ko terminate kar dega
    }

    private boolean checkAndRedirect(String url) {
        String urlLower = url.toLowerCase();
        
        // Force System External Launch for Allowed Links
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

        // Verification condition to check if the exact main batches path is opened
        boolean isMainBatchesPage = urlLower.endsWith("") || urlLower.endsWith("");

        // STRICT PERMANENT BLOCK MATRIX (Specific batch path is completely safe)
        if (urlLower.contains("t.me/pw_thor") || urlLower.contains("t.me/pwthor1") ||
            urlLower.contains("/contact") || urlLower.contains("/study/donate") ||
            isMainBatchesPage) {
            
            try {
                webView.stopLoading();
                // If user hits blocked page, fallback instantly to your custom home batch link
                webView.loadUrl(homeUrl); 
                
                // Instantly redirect to Telegram
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
                    
