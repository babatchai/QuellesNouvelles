package ci.babatchai.nouvelleslocales.rsshandling;

import java.util.ArrayList;

import ci.babatchai.nouvelleslocales.data.HeadlineItem;

public interface AsyncResponse {

        void processFinish(ArrayList<HeadlineItem> output);

}
