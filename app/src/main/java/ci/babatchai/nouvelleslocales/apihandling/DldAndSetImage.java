package ci.babatchai.nouvelleslocales.apihandling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class DldAndSetImage {
    InputStream inputStreamObject = null;

    public void setImage(String path, ImageView view){


            Thread ImgThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        URL url = new URL(path.replace("\"", ""));
                        HttpURLConnection connection = null;
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.addRequestProperty("Accept", "image/*");
                        connection.addRequestProperty("Content-Type", "image/*");

                        inputStreamObject = connection.getInputStream();
                        // BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
                        // String[] splitted = path.split(Pattern.quote("/"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            Bitmap b = BitmapFactory.decodeStream(inputStreamObject);
            if(b != null) {
                b.setDensity(Bitmap.DENSITY_NONE);
                Drawable d = new BitmapDrawable(b);
                view.setImageDrawable(d);
            }
    }
}
