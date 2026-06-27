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

            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                startNuclearObserver(view);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                startNuclearObserver(view);
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
                urlCheckHandler.postDelayed(this, 1000); 
            }
        };
        urlCheckHandler.postDelayed(urlCheckRunnable, 1000);

        // App opens homeUrl directly
        webView.loadUrl(homeUrl);
    }

    // =========================================================
    // THE NUCLEAR ENGINE (UPDATED TO KILL POPUPS & SIDEBAR)
    // =========================================================
    private void startNuclearObserver(WebView view) {
        String nuclearScript = "javascript:(function() {" +
            "if (window.hasNuclearWatcher) return;" +
            "window.hasNuclearWatcher = true;" +

            "function annihilate() {" +
               // A. Rename PW Thor to Study Panda
               "var walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null, false);" +
               "var node;" +
               "while(node = walker.nextNode()) {" +
                   "if(node.nodeValue && node.nodeValue.trim() === 'PW THOR') { node.nodeValue = 'STUDY PANDA'; }" +
               "}" +

               // B. Target Text Hider (Sidebar, Buttons, Owner Info)
               "var badWords = ['Telegram Community !!', 'Join Our Community', 'Donate Batch', 'Contact Us', 'PWTHOR owner', '@pwthor', 'Download'];" +
               
               "var allElements = document.querySelectorAll('div, span, a, button, p');" +
               "allElements.forEach(function(el) {" +
                   "if (el.children.length === 0 && el.innerText) {" +
                       "var text = el.innerText.trim();" +
                       "badWords.forEach(function(word) {" +
                           "if (text.includes(word)) {" +
                               "var card = el.closest('div[class*=\"p-\"], div[class*=\"gap-\"], button, a, li') || el.parentElement;" +
                               "if (card && card.tagName !== 'BODY') {" +
                                   "card.style.setProperty('display', 'none', 'important');" +
                               "}" +
                           "}" +
                       "});" +
                   "}" +
               "});" +

               // C. Instant Popup Dialog Killer
               "var modals = document.querySelectorAll('div[role=\"dialog\"], div[class*=\"modal\"], div[class*=\"popup\"]');" +
               "modals.forEach(function(m) { m.style.setProperty('display', 'none', 'important'); });" +
            "}" +

            "annihilate();" +

            "var observer = new MutationObserver(function() { annihilate(); });" +
            "observer.observe(document.documentElement, {childList: true, subtree: true});" +
        "})()";

        view.loadUrl(nuclearScript);
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
