package com.example.webwrapper; // NOTE: Apna package name match kar lena pehli line se

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
    private final String homeUrl = "https://pwthor.live/study";
    private final long EXPIRY_TIME_MS = 1782645968000;

    private Handler urlCheckHandler = new Handler();
    private Runnable urlCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
                return checkAndRedirect(request.getUrl().toString());
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

        urlCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAppExpired()) {
                    redirectToTelegramAndExit();
                    return;
                }
                if (webView != null && webView.getUrl() != null && !webView.getUrl().equals("about:blank")) {
                    checkAndRedirect(webView.getUrl());
                }
                urlCheckHandler.postDelayed(this, 1000); 
            }
        };
        urlCheckHandler.postDelayed(urlCheckRunnable, 1000);

        webView.loadUrl(homeUrl);
    }

    // THE NUCLEAR ENGINE
    private void startNuclearObserver(WebView view) {
        String nuclearScript = "javascript:(function() {" +
            "if (window.hasNuclearWatcher) return;" +
            "window.hasNuclearWatcher = true;" +

            // Function 1: Destroy Targets
            "function annihilate() {" +
               // A. Hide via CSS selectors (Fastest)
               "var cssTargets = document.querySelectorAll(\"img[alt='PW THOR'], span.bg-muted\");" +
               "for(var i=0; i<cssTargets.length; i++){ cssTargets[i].style.setProperty('display', 'none', 'important'); }" +

               // B. Deep Text Walk (Catches React/NextJS hidden divs)
               "var walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null, false);" +
               "var node;" +
               "while(node = walker.nextNode()) {" +
                   "var val = node.nodeValue.trim();" +
                   
                   // 1. Rename PW Thor
                   "if(val === 'PW THOR') { node.nodeValue = 'STUDY PANDA'; }" +

                   // 2. Kill Download button & Owner Card
                   "if(val === 'Download' || val.indexOf('PWTHOR owner') !== -1 || val.indexOf('@pwthor') !== -1 || val.indexOf('Contact Us') !== -1 || val.indexOf('Donate Batch') !== -1) {" +
                       "var parent = node.parentElement;" +
                       // Climb up 4 HTML tags to grab the entire outer Container box, not just the text word
                       "for(var k=0; k<4; k++) {" +
                           "if(parent && parent.parentElement && parent.tagName !== 'BODY') { parent = parent.parentElement; }" +
                       "}" +
                       "if(parent) { parent.style.setProperty('display', 'none', 'important'); }" +
                   "}" +
               "}" +
            "}" +

            // Run instantly once
            "annihilate();" +

            // Attach MutationObserver: If website injects new HTML via JS, kill it instantly
            "var observer = new MutationObserver(function(mutations) { annihilate(); });" +
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
        } catch (Exception e) {}
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
            } catch (Exception e) { return false; }
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
            } catch (Exception e) { return false; }
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
                                     
