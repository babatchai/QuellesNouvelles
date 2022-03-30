package ci.babatchai.nouvelleslocales.outils;

import android.graphics.Color;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import org.assertj.core.util.Hexadecimals;

import java.lang.reflect.Field;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.WebViewManager;

public class ResourceTools {
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getWaitPage(){
        String html = "<html><head></head><body><h3>Chargement de la page...</h3></body></html>";
        return html;
    }

    public static String getGradientStep(int step, int totalSteps) {
        Color color1 = Color.valueOf(Color.parseColor("#FFFF00"));
        Color color2 = Color.valueOf(Color.parseColor("#00FFFF"));
        Color color3 = null;
        String hexOut = null;
        int steps = totalSteps;

        for (int i = 0; i < steps; i++) {
            float ratio = (float) i / (float) steps;
            float red = (color2.red() * ratio + color1.red() * (1 - ratio));
            float green = (color2.green() * ratio + color1.green() * (1 - ratio));
            float blue = (color2.blue() * ratio + color1.blue() * (1 - ratio));
            color3 = Color.valueOf(red, green, blue);
            String hex = String.format("#%06x", color3.toArgb() & 0xFFFFFF);
            hexOut=hex.toUpperCase();
        }
        return hexOut;
    }

    public static LinearLayout getMuteOverlay(){
        LinearLayout muteOverlay = MainActivity.getActivity().findViewById(R.id.rss_tts_overlay);
        if (muteOverlay == null){
            muteOverlay = MainActivity.getActivity().findViewById(R.id.api_tts_overlay);
        }
        return muteOverlay;
    }

    public static LinearLayout getWebOverlay(){
        LinearLayout webOverlay = MainActivity.getActivity().findViewById(R.id.rss_overlay);
        if (webOverlay == null){
            webOverlay = MainActivity.getActivity().findViewById(R.id.api_overlay);
        }
        return webOverlay;
    }

    public static WebView getActiveWebView(){
        WebView  webView = MainActivity.getActivity().findViewById(R.id.rss_web_viewer);
        if (webView == null) {
            webView = MainActivity.getActivity().findViewById(R.id.api_web_viewer);
        }
        return webView;
    }

    public static Button getActiveBackButton() {
        Button closeWebViewBtn = null;
        closeWebViewBtn = MainActivity.getActivity().findViewById(R.id.rss_web_view_close_btn);
        if (closeWebViewBtn == null) {
            closeWebViewBtn = MainActivity.getActivity().findViewById(R.id.api_web_view_close_btn);
        }

        return closeWebViewBtn;
    }

    public static  Button getWebViewCloseButton(){
        Button closeWebViewBtn = null;
        closeWebViewBtn = MainActivity.getActivity().findViewById(R.id.rss_web_view_close_btn);
        if (closeWebViewBtn == null) {
            closeWebViewBtn = MainActivity.getActivity().findViewById(R.id.api_web_view_close_btn);
        }
        return closeWebViewBtn;
    }
}
