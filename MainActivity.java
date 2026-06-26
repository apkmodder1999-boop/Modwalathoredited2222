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

            // Page ka structure build hote hi elements ko block list me bhejna (Anti-Flash Matrix)
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                injectCustomCSS(view);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                
                // CSS Backup layer execution
                injectCustomCSS(view);

                // 🔥 REAL-TIME DYNAMIC TEXT TEXTURE MODIFY ENGINE
                String jsCode = "javascript:(function() { " +
                        "var textFixInterval = setInterval(function() { " +
                        
                        // A. General Brand Switch: 'PW THOR' -> 'STUDY PANDA'
                        "   var spans = document.querySelectorAll('span'); " +
                        "   spans.forEach(function(span) { " +
                        "       if(span.innerText === 'PW THOR' && span.classList.contains('font-semibold')) { " +
                        "           span.innerText = 'STUDY PANDA'; " +
                        "       } " +
                        "   }); " +
                        
                        // B. Naya Feature: Owner Card Text Replacement Matrix (PWTHOR owner Card Edit)
                        "   var flexCols = document.querySelectorAll('div.flex.flex-col'); " +
                        "   flexCols.forEach(function(card) { " +
                        "       var ownerSpan = card.querySelector('span'); " +
                        "       var textPara = card.querySelector('p'); " +
                        "       if (ownerSpan && ownerSpan.innerText && ownerSpan.innerText.includes('PWTHOR owner')) { " +
                        "           // Text and layout re-mapping with custom text color styling" +
                        "           ownerSpan.innerText = 'I AM JITU'; " +
                        "           ownerSpan.style.setProperty('color', '#00ffff', 'important'); " + // Aqua/Cyan variant color trigger
                        "           if (textPara) { " +
                        "               textPara.innerText = 'THIS APP IS TOTALLY FREE @NOTJITU OR @NOTJITU2'; " +
                        "           } " +
                        "       } " +
                        "   }); " +
                        
                        "}, 200); " + // Engine cycles tracking refresh index
                        "setTimeout(function() { clearInterval(textFixInterval); }, 9000); " +
                        "})()";

                view.loadUrl(jsCode);
            }
        });

        // Loop tracker check system
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

        // Load the main app route cleanly
        webView.loadUrl(homeUrl);
    }

    // Advanced Element Purging Strategy (CSS Layout Level Blocks Injection)
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
                        // Deep layout mapping queries loop
                        "var runRemoverInterval = setInterval(function() {" +
                        
                        // 1. Chat Vector Icon SVG block deletion
                        "var svgs = document.querySelectorAll('svg'); svgs.forEach(function(s){" +
                        "   if(s.innerHTML && s.innerHTML.includes('M8 13.5H16M8 8.5H12')) { s.remove(); }" +
                        "});" +
                        
                        // 2. Global Download Button Purge (Deletes 'Download' elements from everywhere layout-wide)
                        "var fontSemiactiveSpans = document.querySelectorAll('span.text-white'); spans.forEach(function(sp){" +
                        "   if(sp.innerText === 'Download') {" +
                        "       var parentCheck = sp.closest('div.Typography_root__HsO0C');" +
                        "       if(parentCheck) { parentCheck.remove(); }" +
                        "   }" +
                        "});" +
                        
                        // 3. Flex Sidebar Row Actions Controller (Contact, Donate, Download text mapping)
                        "var divs = document.querySelectorAll('div'); divs.forEach(function(d){" +
                        "   if(d.innerText && (d.innerText.includes('Contact Us') || d.innerText.includes('Donate Batch') || d.innerText.includes('Download'))) {" +
                        "       d.style.setProperty('display', 'none', 'important');" +
                        "       d.remove();" + // Global layer remove function execution
                        "   }" +
                        "   if(d.classList && d.classList.contains('rounded-full') && d.classList.contains('overflow-hidden')) {" +
                        "       d.style.setProperty('display', 'none', 'important');" +
                        "   }" +
                        "});" +
                        "}, 250);" +
                        "setTimeout(function() { clearInterval(runRemoverInterval); }, 8500);";
            
            view.loadUrl("javascript:(function() { " + js + " })()");
        } catch (Exception e) {
            // Error shield safety line
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
            // Fail safe exit hook
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
