package com.example.webwrapper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;
    private final String targetTelegram = "https://t.me/+SDQNy0c8-p1iNDBl";

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

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                String urlLower = url.toLowerCase();
                
                // 1. FORCE EXTERNAL OPEN: download.pwthor.live and your custom Telegram invite link
                if (urlLower.contains("download.pwthor.live") || url.equals(targetTelegram)) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        // Fallback if target app is missing
                        return false; 
                    }
                }

                // 2. STRICT URL BLOCKING: Only blocks the exact paths specified
                if (urlLower.equals("https://pwthor.live/study/batches") || 
                    urlLower.equals("https://pwthor.live/study/batches/") ||
                    urlLower.equals("https://pwthor.live/contact") || 
                    urlLower.equals("https://pwthor.live/contact/") ||
                    urlLower.equals("https://pwthor.live/study/donate") || 
                    urlLower.equals("https://pwthor.live/study/donate/") ||
                    urlLower.contains("t.me/pw_thor") || 
                    urlLower.contains("t.me/pw_thor1")) {
                    
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetTelegram));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                executeInjectedSanitizer(view);
            }
        });

        webView.loadUrl("https://pwthor.live/study");
    }

    private void executeInjectedSanitizer(WebView view) {
        String js = "javascript:(function() { " +
                "const targetTg = '" + targetTelegram + "';" +
                
                // Strict equality check for current browser location path
                "function interceptRouter() { " +
                "   const path = window.location.pathname.toLowerCase();" +
                "   if (path === '/study/batches' || path === '/study/batches/' || path === '/contact' || path === '/contact/' || path === '/study/donate' || path === '/study/donate/') { " +
                "       window.location.href = targetTg;" +
                "   }" +
                "}" +
                
                "const push = history.pushState; const replace = history.replaceState;" +
                "history.pushState = function() { push.apply(this, arguments); interceptRouter(); };" +
                "history.replaceState = function() { replace.apply(this, arguments); interceptRouter(); };" +
                "window.addEventListener('popstate', interceptRouter);" +
                "window.addEventListener('hashchange', interceptRouter);" +

                "function sweepUI() { " +
                "   interceptRouter();" +
                "   document.querySelectorAll('a[href]').forEach(link => { " +
                "       const href = link.getAttribute('href');" +
                "       if (href && (href.includes('t.me/pw_thor') || href.includes('pw_thor1'))) { " +
                "           if (!href.includes('+SDQNy0c8')) { link.setAttribute('href', targetTg); }" +
                "       }" +
                "   });" +
                "   ['[class*=\"popup\"]', '[class*=\"modal\"]', '[id*=\"popup\"]', '[id*=\"modal\"]', 'div[style*=\"position: fixed\"][style*=\"z-index\"]'].forEach(sel => { " +
                "       try { " +
                "           document.querySelectorAll(sel).forEach(el => { " +
                "               if (el && el.tagName !== 'BODY' && el.tagName !== 'HTML') { el.remove(); }" +
                "           });" +
                "       } catch(e) {}" +
                "   });" +
                "}" +
                
                "sweepUI();" +
                "const obs = new MutationObserver(sweepUI);" +
                "obs.observe(document.documentElement, { childList: true, subtree: true });" +
                "})();";

        view.evaluateJavascript(js, null);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
                                       }
