package ci.babatchai.nouvelleslocales.apihandling;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;
import ci.babatchai.nouvelleslocales.outils.TextCutter;

public class DldApiAndPrepare  extends AsyncTask<String, Void, Void> {
    ArrayList<HeadlineItem> articles;
    JsonArray jsonArray;

    ApiDownloadListener apiDownloadListener;

    public DldApiAndPrepare() {
        //Constructor may be parametric
    }

    public void setApiDownloadListener(ApiDownloadListener apiDownloadListener) {
        this.apiDownloadListener = apiDownloadListener;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        if (articles != null) {
            apiDownloadListener.apiDownloadedSuccessfully(articles);
        } else
            apiDownloadListener.apiDownloadFailed();
    }

    public static interface ApiDownloadListener {
        void apiDownloadedSuccessfully(ArrayList<HeadlineItem> articles);

        void apiDownloadFailed();
    }

    @Override
    protected Void doInBackground(String... strings) {
        articles = new ArrayList<>();
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Authorization", "Bearer <spotify api key>");

            InputStream inputStreamObject = connection.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JsonElement jsonObject = JsonParser.parseString(responseStrBuilder.toString());
            jsonArray = ((JsonObject) jsonObject).get("data").getAsJsonArray();
            for(JsonElement element : jsonArray){
                HeadlineItem headlineItem = new HeadlineItem();
                headlineItem.setTitle(((JsonObject)element).get("title").toString().replace("\"",""));
                headlineItem.setText(((JsonObject)element).get("description").toString().replace("\"",""));
                headlineItem.setAuthor(((JsonObject)element).get("author").toString().replace("\"",""));
                headlineItem.setLink(((JsonObject)element).get("url").toString().replace("\"",""));
                headlineItem.setSource(((JsonObject)element).get("source").toString().replace("\"",""));
                if(((JsonObject)element).get("image") != JsonNull.INSTANCE && !((JsonObject)element).get("image").equals("null") ) {
                    headlineItem.setImageUrl(((JsonObject) element).get("image").toString().replace("\"", ""));
                }
                headlineItem.setPubDate(((JsonObject)element).get("published_at").toString().replace("\"",""));
                articles.add(headlineItem);
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        apiDownloadListener.apiDownloadedSuccessfully(articles);
        return null;
    }
}


