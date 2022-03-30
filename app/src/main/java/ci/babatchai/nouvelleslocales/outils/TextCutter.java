package ci.babatchai.nouvelleslocales.outils;

import org.jsoup.Jsoup;

public class TextCutter {
    public static final int MAX_TITLE_LEGTH = 80;
    public static final int MAX_TEXT_LENGTH = 160;

    public static String CutTextLength(String text, int maxLength){
        String txtCut = "";
        String txtFinal = "";
        String txtClean = "";
        if(text == null)
            return "";

        txtClean = Jsoup.parse(text).text();
        if (txtClean.length() > maxLength) {
            txtCut = "...";
            txtFinal = txtClean.substring(0, maxLength - 4);
        } else {
            txtFinal = Jsoup.parse(text).text();
        }
        return txtFinal + txtCut;
    }

}
