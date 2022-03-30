package ci.babatchai.nouvelleslocales;

import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.LinearLayoutCompat;

import java.lang.reflect.Method;

import ci.babatchai.nouvelleslocales.outils.ResourceTools;
import ci.babatchai.nouvelleslocales.tts.SpeakService;

public class WebViewManager {
    Thread waiter;
    WebView webView;
    LinearLayout webViewWrapper;

    WebViewWatcherListener webViewWatcherListener;
    public static interface WebViewWatcherListener {
        void setFinished(boolean finished);
    }

    public LinearLayout displayWebView(String webViewUrl) {
        webView = ResourceTools.getActiveWebView();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                ImageButton webRead = MainActivity.getActivity().findViewById(R.id.rss_read_Web_btn);
                if (webRead == null) {
                    webRead = MainActivity.getActivity().findViewById(R.id.api_read_Web_btn);
                }
                webRead.setVisibility(View.VISIBLE);

            }
        });


        webViewWrapper = MainActivity.getActivity().findViewById(R.id.api_web_view_wrapper);
        if (webViewWrapper == null) {
            webViewWrapper = MainActivity.getActivity().findViewById(R.id.rss_web_view_wrapper);
        }
        webViewWrapper.setVisibility(View.VISIBLE);

        webView.loadUrl(webViewUrl);

        waiter = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true)
                        if (webViewWrapper.getVisibility() == View.VISIBLE) {
                            MainActivity.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    webViewWrapper.bringToFront();
                                }
                            });
                            Thread.sleep(3000);
                        } else {
                            webViewWatcherListener.setFinished(true);

                        }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        waiter.start();

        Button closeWebViewBtn = ResourceTools.getWebViewCloseButton();
        closeWebViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.speakService.abortSpeetch();
                webView.loadData(ResourceTools.getWaitPage(), "text/html", "UTF-8");
                webViewWrapper.setVisibility(View.GONE);
            }
        });
        return webViewWrapper;
    }

    public static void textSelect(String textToSelect, WebView viewer){
        if (textToSelect != null && !textToSelect.equals("")) {
            MainActivity.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int i = viewer.findAll(textToSelect);
                    try {
                        Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                        m.invoke(viewer, true);

                    } catch (Throwable ignored) {
                    }
                    // webView.loadUrl("javascript:document.getElementsByClassName('"+aID+"')[0].scrollIntoView()");
                }
            });
        }
    }

    public void setWebViewWatcherListener(WebViewWatcherListener webViewWatcherListener){
        this.webViewWatcherListener = webViewWatcherListener;
    }

}
