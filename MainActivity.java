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
    private final String homeUrl = "https://pwthor.live/study/batches/69d8916116a832cea75df23c";
    private final long EXPIRY_TIME_MS = 1814268335000L;

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
                String url = request.getUrl().toString();
                return checkAndRedirect(url);
            }

            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                injectCustomCSS(view);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                injectCustomCSS(view);

                // ULTRA FLASH MODE - 40ms LOOP WITH HIGH-PERFORMANCE FILTERS
                String jsCode = "javascript:(function() { " +
                        "setInterval(function() { " +
                        
                            // 1. FAST TEXT REPLACEMENT (PW THOR -> STUDY PANDA)
                            "var textNodes = document.querySelectorAll('span, p, div, h1, h2, h3, b, strong'); " +
                            "for (var i = 0; i < textNodes.length; i++) { " +
                                "var el = textNodes[i]; " +
                                "if(el.children.length === 0 && el.innerText && el.innerText.trim() === 'PW THOR') { " +
                                    "el.innerText = 'SANKALP 2028'; " +
                                "} " +
                            "} " +

                            // 2. NEW BLUE LOGO IMAGE REMOVER (Instant kill via src url)
                            "var badLogos = document.querySelectorAll(\"img[src*='pwthor.site/logo.png']\"); " +
                            "for (var j = 0; j < badLogos.length; j++) { " +
                                "var container = badLogos[j].closest('div.rounded-full') || badLogos[j].parentElement; " +
                                "if(container) { container.style.setProperty('display', 'none', 'important'); } " +
                            "} " +

                            // 3. TARGETED AVATAR CONTAINER REMOVER (NEW ELEMENT FIXED)
                            "var avatars = document.querySelectorAll('div.w-10.h-10.rounded-full.overflow-hidden'); " +
                            "for (var aIndex = 0; aIndex < avatars.length; aIndex++) { " +
                                "avatars[aIndex].style.setProperty('display', 'none', 'important'); " +
                            "} " +

                            // 4. TEXT-BASED ELEMENT ASSASSIN (Sidebar, 3-dot Download, Comments, Popups)
                            "var killList = ['Contact Us', 'Download', 'PWTHOR owner', '@pwthor', 'Join Our Community', 'Telegram Community !!']; " +
                            "var targetElements = document.querySelectorAll('div, span, a, li, button, p'); " +
                            "for (var k = 0; k < targetElements.length; k++) { " +
                                "var element = targetElements[k]; " +
                                "if (element.children.length === 0 && element.innerText) { " +
                                    "var txt = element.innerText.trim(); " +
                                    "for (var m = 0; m < killList.length; m++) { " +
                                        "if (txt === killList[m] || txt.includes(killList[m])) { " +
                                            "var box = element.closest('div[class*=\"flex\"], a, li, button, div[role=\"dialog\"]') || element.parentElement; " +
                                            "if (box && box.tagName !== 'BODY' && box.tagName !== 'HTML') { " +
                                                "box.style.setProperty('display', 'none', 'important'); " +
                                            "} " +
                                        "} " +
                                    "} " +
                                "} " +
                            "} " +

                            // 5. MODAL POPUP BACKUP KILLER
                            "var dialogs = document.querySelectorAll('div[role=\"dialog\"]'); " +
                            "for (var n = 0; n < dialogs.length; n++) { dialogs[n].style.setProperty('display', 'none', 'important'); } " +

                        "}, 100); " + // 100ms FLASH SPEED
                "})()";

                view.loadUrl(jsCode);
            }
        });

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

        webView.loadUrl(homeUrl);
    }

    private void injectCustomCSS(WebView view) {
        try {
            String css = "img[alt='PW THOR'], .bg-muted { display: none !important; }" +
                    "div[class*='cursor-pointer']:has(span:contains('Contact Us')), " +
                    "div[class*='cursor-pointer']:has(span:contains('Donate Batch')) { display: none !important; }";
            String js = "var style = document.getElementById('custom-css-injection');" +
                    "if(!style) {" +
                    " style = document.createElement('style');" +
                    " style.id = 'custom-css-injection';" +
                    " style.innerHTML = \"" +
                    " img[alt='PW THOR'], span.bg-muted { display: none !important; } " +
                    " div.flex.items-center:has(svg.lucide-contact), div.flex.items-center:has(svg.lucide-heart) { display: none !important; }" +
                    " \";" +
                    " document.head.appendChild(style);" +
                    "}";
            view.loadUrl("javascript:(function() { " + js + " })()");
        } catch (Exception e) {}
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
        if (urlLower.contains("download.pwthor.live") || url.equals(targetTelegram)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            } catch (Exception e) { return false; }
        }

        boolean endsWithBatches = urlLower.endsWith("/study/batches") || urlLower.endsWith("/study/batches");
        
        if (urlLower.contains("t.me/pw_thor") || urlLower.contains("t.me/pwthor1") ||
                urlLower.contains("/contact") || urlLower.contains("/end")) {
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
