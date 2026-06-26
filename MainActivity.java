package com.example.webwrapper; // NOTE: Agar aapki repo me package name alag hai, toh pehli line vahi rehne dena.

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
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

            // Page load hona shuru hote hi hum custom CSS inject kar denge taaki original elements user ko dikhein hi nahi!
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                injectCustomCSS(view);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                
                // CSS backup ke liye onPageFinished par bhi inject kar dete hain
                injectCustomCSS(view);

                // UI Modifications Matrix (Includes text fix, rebranding & specific element replacements)
                String jsCode = "javascript:(function() { " +
                        "var textFixInterval = setInterval(function() { " +
                        // 1. Original text replacement matrix ("PW THOR" -> "STUDY PANDA")
                        "   var spans = document.querySelectorAll('span'); " +
                        "   spans.forEach(function(span) { " +
                        "       if(span.innerText === 'PW THOR' && span.classList.contains('font-semibold')) { " +
                        "           span.innerText = 'STUDY PANDA'; " +
                        "       } " +
                        "   }); " +
                        
                        // 2. Naya Modification Loop: Owner section target matrix
                        "   var flexDivs = document.querySelectorAll('div.flex.flex-col'); " +
                        "   flexDivs.forEach(function(div) { " +
                        "       var ownerSpan = div.querySelector('span.text-sm'); " +
                        "       var para = div.querySelector('p.text-sm'); " +
                        "       if(ownerSpan && ownerSpan.innerText.includes('PWTHOR owner')) { " +
                        "           ownerSpan.innerText = 'I AM JITU'; " +
                        "           ownerSpan.classList.remove('text-blue-400'); " +
                        "           ownerSpan.classList.add('text-aqua-400'); " +
                        "           ownerSpan.style.color = '#00ffff'; " + // Aqua backup style injection
                        "           if(para) { " +
                        "               para.innerText = 'THIS APP IS TOTALLY FREE @NOTJITU OR @NOTJITU2'; " +
                        "           } " +
                        "       } " +
                        "   }); " +
                        
                        // 3. Dynamic Node Removal: Download Typography element drop check
                        "   var downloadDivs = document.querySelectorAll('div[class*=\"Typography_root__HsO0C\"][class*=\"Typography_subHeading__v4fFR\"]'); " +
                        "   downloadDivs.forEach(function(div) { " +
                        "       var span = div.querySelector('span.text-white'); " +
                        "       if(span && span.innerText.trim() === 'Download') { " +
                        "           div.style.setProperty('display', 'none', 'important'); " +
                        "       } " +
                        "   }); " +
                        "}, 200); " +
                        "setTimeout(function() { clearInterval(textFixInterval); }, 8000); " +
                        "})()";

                view.loadUrl(jsCode);
            }
        });

        // Background real-time listener (Checks state safely without crashing)
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
                urlCheckHandler.postDelayed(this, 1000); // 1 second interval to keep it performance-friendly
            }
        };
        urlCheckHandler.postDelayed(urlCheckRunnable, 1000);

        // App opens homeUrl directly
        webView.loadUrl(homeUrl);
    }

    // Helper method to hide elements instantly via CSS rule injection before rendering
    private void injectCustomCSS(WebView view) {
        try {
            // Hiding logic updated with New Download Element selectors
            String css = "img[alt='PW THOR'], .bg-muted { display: none !important; }" +
                         "div[class*='cursor-pointer']:has(span:contains('Contact Us')), " +
                         "div[class*='cursor-pointer']:has(span:contains('Donate Batch')) { display: none !important; }" +
                         "div[class*='Typography_root__HsO0C'][class*='Typography_subHeading__v4fFR']:has(span:contains('Download')) { display: none !important; }";
            
            // Safe universal JavaScript fallback inside CSS injection context to hide targets explicitly
            String js = "var style = document.getElementById('custom-css-injection');" +
                        "if(!style) {" +
                        "   style = document.createElement('style');" +
                        "   style.id = 'custom-css-injection';" +
                        "   style.innerHTML = \"" +
                        "       img[alt='PW THOR'], span.bg-muted { display: none !important; } " +
                        "       div.flex.items-center:has(svg.lucide-contact), div.flex.items-center:has(svg.lucide-heart) { display: none !important; }" +
                        "       div[class*='Typography_root__HsO0C'][class*='Typography_subHeading__v4fFR'] { display: none !important; }" + // Force drop download typography layout rule
                        "   \";" +
                        "   document.head.appendChild(style);" +
                        "}" +
                        // Double check to forcefully drop them if dynamic DOM updates bypass stylesheets
                        "var divs = document.querySelectorAll('div'); divs.forEach(function(d){" +
                        "   if(d.innerText && (d.innerText.includes('Contact Us') || d.innerText.includes('Donate Batch'))) { d.style.setProperty('display', 'none', 'important'); }" +
                        "   if(d.classList.contains('Typography_root__HsO0C') && d.innerText && d.innerText.includes('Download')) { d.style.setProperty('display', 'none', 'important'); }" +
                        "});";
            
            view.loadUrl("javascript:(function() { " + js + " })()");
        } catch (Exception e) {
            // Suppress error
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

        boolean isMainBatchesPage = urlLower.endsWith("/study/batches") || urlLower.endsWith("/study/batches/");

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
