package ci.babatchai.nouvelleslocales.data;

public class ApiLists {
    private static String apiKey = "4c3008be73ea30a30965c6139169221d";
    private static String apiUrl = "http://api.mediastack.com/v1/";

    public static String getApiRequest(ApiCategories category, int limit, ApiLangages[] languagesPlus, ApiLangages[] languagesMinus){
        return apiUrl + "news?access_key=" + apiKey + "&categories=" + category.getText() + "&limit=" + limit + agglomerateLang(languagesPlus, languagesMinus);
    }

    public static String getApiRequest(ApiCategories category, int limit, String country, ApiLangages[] languagesPlus, ApiLangages[] languagesMinus){
        return apiUrl + "news?access_key=" + apiKey + "&categories=" + category.getText() + "&country=" + country + "&limit=" + limit +  agglomerateLang(languagesPlus, languagesMinus);
    }

    public static String getApiRequest(String keywords, int limit, String country, ApiLangages[] languagesPlus, ApiLangages[] languagesMinus){
        return apiUrl + "news?access_key=" + apiKey + "&keywords=" + keywords + "&country=" + country + "&limit=" + limit +  agglomerateLang(languagesPlus, languagesMinus);
    }

    private static String agglomerateLang(ApiLangages[] langPlus, ApiLangages[] langMinus){
        String retVal = "&languages=";
        String comma = "";

        for(ApiLangages langue : langPlus) {
            retVal += comma + langue.getText();
            comma = ",";
        }

        for(ApiLangages langue : langMinus) {
            retVal += comma + "-" + langue.getText();
            comma = ",";
        }

        return retVal;
    }
    /* optional parameters:

    & sources = cnn,bbc
    & categories = business,sports
    & countries = us,au
    & languages = en,-de
    & keywords = virus,-corona
    & sort = published_desc
    & offset = 0
    & limit = 100
     */
}
