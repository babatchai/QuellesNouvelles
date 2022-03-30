package ci.babatchai.nouvelleslocales.rsshandling;

import android.accounts.NetworkErrorException;
import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;
import ci.babatchai.nouvelleslocales.outils.NetworkingTools;
import ci.babatchai.nouvelleslocales.ui.home.NoNetworkFragment;

public class DldRssAndPrepare extends AsyncTask<String, Void, ArrayList<HeadlineItem>>{
    ArrayList<HeadlineItem> articles;

    RssDownloadListener rssDownloadListener;
    public DldRssAndPrepare(Fragment caller)  {
        if(!NetworkingTools.isDeviceConnToInternet(MainActivity.getContext())){
            caller.getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_navigation,
                            new NoNetworkFragment())
                    .commitNow();
        }
    }

    public void setRssDownloadListener(RssDownloadListener rssDownloadListener) {
        this.rssDownloadListener = rssDownloadListener;
    }

    @Override
    protected void onPostExecute(ArrayList<HeadlineItem> articles) {
        super.onPostExecute(articles);
        if(articles != null)
        {
            rssDownloadListener.rssDownloadedSuccessfully(articles);
        }
        else
            rssDownloadListener.rssDownloadFailed();
    }

    public static interface RssDownloadListener {
        void rssDownloadedSuccessfully(ArrayList<HeadlineItem> articles);
        void rssDownloadFailed();
    }
    
    @Override
    protected ArrayList<HeadlineItem> doInBackground(String... strings) {
                                                                                                articles = new ArrayList<>();
        if(strings[0] == null)
            return articles;

        Document doc = null;
        HeadlineItem headlineItem = null;
        try {
             doc = Jsoup.connect(strings[0]).get();
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
        if(doc==null)
            return articles;
        Elements rss = doc.select("rss");
        Elements atom = doc.select("feed");
        String rssVersion = null;
        if(atom.size()  > 0){
            Elements atomOutil = atom.select("generator");
            String generator = atomOutil.html();
            String version = atomOutil.attr("version");
            Elements entries = atom.select("entry");
            for(Element entry : entries) {
                headlineItem = new HeadlineItem();
                headlineItem.setTitle(entry.select("title").html());
                headlineItem.setLink(entry.select("link").attr("href"));
                headlineItem.setPubDate(entry.select("updated").html());
                headlineItem.setContent(entry.select("content").html());
                headlineItem.setText(entry.select("summary").html());
                Elements author =entry.select("author");
                headlineItem.setAuthor(author.select("name").html());
                Elements source =entry.select("source");
                headlineItem.setAuthor(source.select("title").html());

                articles.add(headlineItem);
                int x = articles.size();
            }
        }
        if(rss.size() > 0){
            rssVersion = getValueFromElement(rss.get(0), "version"); // attribute
            Elements channel = rss.select("channel");
            String channelTitle = getValueFromElement(channel.get(0), "title");
            String channelCategory = getValueFromElement(channel.get(0), "category");
            Elements items = channel.select("item");
            // this matches with RSS é.à standard
            for(Element element : items) {
                headlineItem = new HeadlineItem();
                headlineItem.setTitle(getValueFromElement(element, "title"));
                headlineItem.setLink(getValueFromElement(element, "link"));
                headlineItem.setGuid(getValueFromElement(element, "guid"));
                headlineItem.setPubDate(getValueFromElement(element, "pubDate"));
                headlineItem.setText(getValueFromElement(element, "description"));
                headlineItem.setAuthor(getValueFromElement(element, "author"));
                headlineItem.setComment(getValueFromElement(element, "comments"));
                headlineItem.setSource(getValueFromElement(element, "source"));
                // super-tag for media
                Elements enclosures = element.select("enclosure");
                if (enclosures != null && !(enclosures.size() < 1) && !enclosures.isEmpty()) {
                    String type = enclosures.get(0).attr("type").toString();
                    String size = enclosures.get(0).attr("length");
                    if (type.startsWith("image")) {
                        String url = enclosures.get(0).attr("url").toString();
                        headlineItem.setImageUrl(url);
                    }
                    if (type.startsWith("audio")) {
                        String url = enclosures.get(0).attr("url").toString();
                        headlineItem.setAudioUrl(url);
                    }
                    if (type.startsWith("video")) {
                        String url = enclosures.get(0).attr("url").toString();
                        headlineItem.setVideoUrl(url);
                    }


                }
                articles.add(headlineItem);
            }
                int x = articles.size();
            }
        rssDownloadListener.rssDownloadedSuccessfully(articles);
        return articles;
    }

    private String getValueFromElement(Element element, String name){

        Elements result = element.select(name);
        String value = null;
        if (result != null &&  result.size() > 0 && !result.isEmpty()){
            value = result.get(0).text();
        }
        return value;
    }

}
