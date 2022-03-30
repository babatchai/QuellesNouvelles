package ci.babatchai.nouvelleslocales.outils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Pattern;

public class NetworkingTools {
    public static boolean isDeviceConnToInternet(Context ctx) {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)ctx.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connected;
    }

    public static String getDomainFromUrl(String url){
        String[] urlSplit = url.split(Pattern.quote("/"));
        return urlSplit[0] + "//" + urlSplit[2];
    }
}
