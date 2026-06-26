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

            // Page loading start hote hi dynamic stylesheets inject honge
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                injectCustomDOMFix(view);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                injectCustomDOMFix(view);
            }
        });

        // Background dynamic routing security listener (Safely loops every 1 second)
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

        // Load the main secure layout path
        webView.loadUrl(homeUrl);
    }

    // Dynamic Engine targeting text nodes, structural updates, and deep styling overrides
    private void injectCustomDOMFix(WebView view) {
        try {
            String js = "var runDynamicFixer = setInterval(function() {" +
                        
                        // 1. GLOBAL TEXT REPLACEMENTS ('PW THOR' -> 'STUDY PANDA' & 'PWTHOR owner' text block updates)
                        "   var elements = document.getElementsByTagName('*');" +
                        "   for (var i = 0; i < elements.length; i++) {" +
                        "       var el = elements[i];" +
                        "       for (var j = 0; j < el.childNodes.length; j++) {" +
                        "           var node = el.childNodes[j];" +
                        "           if (node.nodeType === 3) {" + // Text Node process flag
                        "               var text = node.nodeValue;" +
                        "               " +
                        "               // A. Brand Text Replace" +
                        "               if (text.trim() === 'PW THOR') {" +
                        "                   node.nodeValue = 'STUDY PANDA';" +
                        "               }" +
                        "               " +
                        "               // B. Card Title Text Replace" +
                        "               if (text.trim() === 'PWTHOR owner') {" +
                        "                   node.nodeValue = 'I AM JITU';" +
                        "                   el.style.setProperty('color', '#00ffff', 'important');" + // Dynamic Aqua Force Style Injection
                        "               }" +
                        "               " +
                        "               // C. Card Desc Text Replace" +
                        "               if (text.trim() === 'education must be free for eveyone @pwthor') {" +
                        "                   node.nodeValue = 'THIS APP IS TOTALLY FREE @NOTJITU OR @NOTJITU2';" +
                        "                   el.style.setProperty('color', '#e5e7eb', 'important');" +
                        "               }" +
                        "           }" +
                        "       }" +
                        "       " +
                        "       // 2. UNIVERSAL DOWNLOAD ELEMENT DELETION MATRIX" +
                        "       if (el.innerText && el.innerText.trim() === 'Download') {" +
                        "           var parentNode = el.closest('.Typography_root__HsO0C') || el.closest('div.flex') || el;" +
                        "           if(parentNode) { parentNode.remove(); }" +
                        "       }" +
                        "   }" +

                        // 3. REMOVE IMAGES, SIDEBAR BLOCKS AND CHAT VECTOR ICONS
                        "   var imgLogo = document.querySelector(\"img[alt='PW THOR']\");" +
                        "   if(imgLogo) { imgLogo.remove(); }" +
                        "   " +
                        "   var mutedSpans = document.querySelectorAll('span.bg-muted');" +
                        "   mutedSpans.forEach(function(sp) { if(sp.innerText === 'TH') { sp.remove(); } });" +
                        "   " +
                        "   var avatarDivs = document.querySelectorAll('div.rounded-full.overflow-hidden');" +
                        "   avatarDivs.forEach(function(av) { av.remove(); });" +
                        "   " +
                        "   var chatIcons = document.querySelectorAll('svg');" +
                        "   chatIcons.forEach(function(svg) {" +
                        "       if (svg.innerHTML && svg.innerHTML.includes('M8 13.5H16M8 8.5H12')) { svg.remove(); }" +
                        "   });" +
                        "   " +
                        "   var layoutDivs = document.querySelectorAll('div');" +
                        "   layoutDivs.forEach(function(div) {" +
                        "       if (div.innerText && (div.innerText.includes('Contact Us') || div.innerText.includes('Donate Batch'))) {" +
                        "           div.remove();" +
                        "       }" +
                        "   });" +

                        "}, 150);" + // Dynamic 150ms cycle speed keeps layout changes invisible to the naked eye
                        
                        // Safety timeout to save phone battery after initial page render cycle
                        "setTimeout(function() { clearInterval(runDynamicFixer); }, 10000);";
            
            view.loadUrl("javascript:(function() { " + js + " })()");
        } catch (Exception e) {
            // Anti-crash suppression line
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
