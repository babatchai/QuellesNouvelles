package ci.babatchai.nouvelleslocales.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import ci.babatchai.nouvelleslocales.MainActivity;

public class UrlLists {
    private static ArrayList<UrlDataItem> urlItemList = null;
    public UrlLists() {
        if(urlItemList == null){
            urlItemList = new ArrayList<>();
            readUrlLists();
        }
    }

    public static ArrayList<UrlDataItem> getUrlData() {
        if(urlItemList == null){
            urlItemList = new ArrayList<>();
            readUrlLists();
        }
        return urlItemList;
    }

    public static UrlDataItem getItemForDomain(String domain){
        if(urlItemList == null){
            urlItemList = new ArrayList<>();
            readUrlLists();
        }
        UrlDataItem returnItem = null;
        for(UrlDataItem item : urlItemList){
            if(item.getFeedUrl().startsWith(domain)){
                returnItem = item;
                break;
            }
        }
        return returnItem;
    }

    public static UrlDataItem getItemForHome(){
        if(urlItemList == null){
            urlItemList = new ArrayList<>();
            readUrlLists();
        }
        UrlDataItem returnItem = null;
        for(UrlDataItem item : urlItemList){
            if(item.getFeedCategorie().equalsIgnoreCase("home")){
                returnItem = item;
                break;
            }
        }
        return returnItem;
    }

    private static void readUrlLists(){

        JsonElement jsonObject = JsonParser.parseString(dldUrls());
        JsonArray jsonArray = ((JsonObject) jsonObject).get("data").getAsJsonArray();
        for(JsonElement element : jsonArray) {
            UrlDataItem urlData = new UrlDataItem();
            urlData.setFeedUrl(((JsonObject) element).get("feedUrl").toString().replace("\"", ""));
            urlData.setFeedName(((JsonObject) element).get("feedName").toString().replace("\"", ""));
            urlData.setFeedGuid(((JsonObject) element).get("feedGuid").toString().replace("\"", ""));
            urlData.setArticleTagSuite(((JsonObject) element).get("articleTagSuite").toString().replace("\"", ""));
            urlData.setArticleTag(((JsonObject) element).get("articleTag").toString().replace("\"", ""));
            urlData.setArticleTagClass(((JsonObject) element).get("articleTagClass").toString().replace("\"", ""));
            urlData.setTitleTag(((JsonObject) element).get("titleTag").toString().replace("\"", ""));
            urlData.setTitleTagSuite(((JsonObject) element).get("titleTagSuite").toString().replace("\"", ""));
            urlData.setTitleTagClass(((JsonObject) element).get("titleTagClass").toString().replace("\"", ""));
            urlData.setFeedCategorie(((JsonObject) element).get("feedCategorie").toString().replace("\"", ""));
            urlData.setFeedDistiller(((JsonObject) element).get("feedDistiller").toString().replace("\"", ""));

            urlItemList.add(urlData);
        }
    }

    private static String dldUrls(){

        String json = null;
        try {
            // InputStream is = getAssets().open("data/data.json");
            InputStream is = MainActivity.getContext().getAssets().open("feeds.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
