package ci.babatchai.nouvelleslocales.distiller;

import static ci.babatchai.nouvelleslocales.outils.NetworkingTools.getDomainFromUrl;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;
import ci.babatchai.nouvelleslocales.data.UrlDataItem;
import ci.babatchai.nouvelleslocales.data.UrlLists;
import ci.babatchai.nouvelleslocales.rsshandling.DldRssAndPrepare;
import ci.babatchai.nouvelleslocales.tts.SpeakService;

public class PageContentDistiller extends AsyncTask<String, Void, ArrayList<String>> {
    ArrayList<String> paragraphs;
    PageContentDistillerListener pageContentDistillerListener;
    public void setPageContentDistillerListener(PageContentDistillerListener pageContentDistillerListener) {
        this.pageContentDistillerListener = pageContentDistillerListener;
    }

    @Override
    protected void onPostExecute(ArrayList<String> paragraphs) {
        super.onPostExecute(paragraphs);
        if(paragraphs != null)
        {
            pageContentDistillerListener.pageContentDistillerSuccessfully(paragraphs);
        }
        else
            pageContentDistillerListener.pageContentDistillerFailed();
    }

    public static interface PageContentDistillerListener {
        void pageContentDistillerSuccessfully(ArrayList<String> paragraphs);
        void pageContentDistillerFailed();
    }

    protected ArrayList<String> doInBackground(String... strings) {
        paragraphs = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(strings[0]).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        UrlDataItem urlItem = UrlLists.getItemForDomain(getDomainFromUrl(strings[0]));
        String titleTagSuite = urlItem.getTitleTagSuite();
        String[] titleClasses = urlItem.getTitleTagClass().split(Pattern.quote("|"));
        String titleTag = "";

        String[] articleTagSuite = urlItem.getArticleTagSuite().split(Pattern.quote("|"));
        String[] articleClasses = urlItem.getArticleTagClass().split(Pattern.quote("|"));
        String[] articleTag = urlItem.getArticleTag().split(Pattern.quote("|"));

        Elements bodyHolder = null;
        Elements[] titleHolder = null;
        Elements[] articleHolder = null;
        Elements artParas = null;

        bodyHolder = doc.select("body");

        titleHolder = new Elements[1];
        titleHolder[0] = bodyHolder.select(titleTagSuite);

        for(Elements title : titleHolder) {
            titleTag = title.html();
            String titleValue = Jsoup.parse(titleTag).text();
            paragraphs.add(cleanText(titleValue));
        }

        articleHolder = new Elements[articleTagSuite.length];

        for(int i=0;i< articleTagSuite.length;i++ ){
            articleHolder[i] = bodyHolder.select(articleTagSuite[i]);

            for(Elements para : articleHolder) {
                Elements articleValue = null;
                Elements artParagraph = null;
                if (para != null && !para.isEmpty()) {
                    if(!articleTag[i].equals("-")) {
                        articleValue = para.select(articleTag[i]);
                        for (Element aps : articleValue) {
                            String articleString = Jsoup.parse(aps.html()).text();
                            paragraphs.add(cleanText(articleString));
                        }
                    }else{
                        String articleString =Jsoup.parse(para.html()).text();
                        paragraphs.add(cleanText(articleString));
                    }
                }
            }
        }

        return paragraphs;
    }

    private String cleanText(String articleString) {
        if(articleString.startsWith("\"")){
            articleString = articleString.substring(1,articleString.length() -1);
        }
        if(articleString.endsWith("\"")){
            articleString = articleString.substring(0,articleString.length() -2);
        }
        return articleString;
    }

    private String getValueFromElement(Element element){

        String result = element.text();
        String value = null;
        if (result != null &&  result.length() > 0 && !result.isEmpty()){
            value = Jsoup.parse(result).text();
        }
        return cleanText(value);
    }
}
