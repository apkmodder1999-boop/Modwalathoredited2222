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
    private final long EXPIRY_TIME_MS = 1787580111000L; 
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

            // Page banna shuru hote hi elements ko layout rendering se pehle hide karna (Anti-Flash Matrix)
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                injectCustomCSS(view);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                
                // CSS Backup execution
                injectCustomCSS(view);

                // Text modification engine ("PW THOR" to "STUDY PANDA")
                String jsCode = "javascript:(function() { " +
                        "var textFixInterval = setInterval(function() { " +
                        "   var spans = document.querySelectorAll('span'); " +
                        "   spans.forEach(function(span) { " +
                        "       if(span.innerText === 'PW THOR' && span.classList.contains('font-semibold')) { " +
                        "           span.innerText = 'STUDY PANDA'; " +
                        "       } " +
                        "   }); " +
                        "}, 200); " +
                        "setTimeout(function() { clearInterval(textFixInterval); }, 8000); " +
                        "})()";

                view.loadUrl(jsCode);
            }
        });

        // Background loop execution layer
        urlCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAppExpired()) {
                    redirectToTelegramAndExit();
                    return;
                }
                if (webView != null) {
                    String currentUrl = webView.getUrl();
                    if (currentUrl != null && !currentUrl.equals("about:blank")) {
                        checkAndRedirect(currentUrl);
                    }
                }
                urlCheckHandler.postDelayed(this, 1000); 
            }
        };
        urlCheckHandler.postDelayed(urlCheckRunnable, 1000);

        // App opens homeUrl directly
        webView.loadUrl(homeUrl);
    }

    // Custom CSS injection setup targeted for exact layout blocks removal
    private void injectCustomCSS(WebView view) {
        try {
            String js = "var style = document.getElementById('custom-css-injection');" +
                        "if(!style) {" +
                        "   style = document.createElement('style');" +
                        "   style.id = 'custom-css-injection';" +
                        "   style.innerHTML = \"" +
                        "       img[alt='PW THOR'], span.bg-muted, div.rounded-full.dark\\\\:bg-foreground { display: none !important; } " +
                        "       div.flex.items-center:has(svg.lucide-contact), div.flex.items-center:has(svg.lucide-heart) { display: none !important; }" +
                        "   \";" +
                        "   document.head.appendChild(style);" +
                        "}" +
                        // Deep DOM dynamic processing queries loop
                        "var runRemoverInterval = setInterval(function() {" +
                        // A. Chat Vector Icon SVG target removal via path coordinate validation
                        "var svgs = document.querySelectorAll('svg'); svgs.forEach(function(s){" +
                        "   if(s.innerHTML && s.innerHTML.includes('M8 13.5H16M8 8.5H12')) { s.remove(); }" +
                        "});" +
                        // B. Sidebar Download Button container identification & purge
                        "var divs = document.querySelectorAll('div'); divs.forEach(function(d){" +
                        "   if(d.innerText && (d.innerText.includes('Contact Us') || d.innerText.includes('Donate Batch') || d.innerText.includes('Download'))) {" +
                        "       d.style.setProperty('display', 'none', 'important');" +
                        "   }" +
                        "   if(d.classList && d.classList.contains('rounded-full') && d.classList.contains('overflow-hidden')) {" +
                        "       d.style.setProperty('display', 'none', 'important');" +
                        "   }" +
                        "});" +
                        "}, 250);" +
                        "setTimeout(function() { clearInterval(runRemoverInterval); }, 8000);";
            
            view.loadUrl("javascript:(function() { " + js + " })()");
        } catch (Exception e) {
            // Safe logging catch
        }
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
        
        if (urlLower.contains("static.pw.live") || url.equals(targetTelegram)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                return false; 
            }
        }

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
