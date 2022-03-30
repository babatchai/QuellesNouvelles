package ci.babatchai.nouvelleslocales.ui.recyclerview_variants;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.apihandling.DldApiAndPrepare;
import ci.babatchai.nouvelleslocales.data.ApiLangages;
import ci.babatchai.nouvelleslocales.data.ApiLists;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;

public class ApiVariant {

    public void rv_api_variant(RecyclerView recyclerView){

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.getContext()));

        Thread breakingNewsThread = new Thread(new Runnable() {
            @Override
            public void run() {

                String apiUrl = ApiLists.getApiRequest("ivory coast",
                        5, "ci,za,ma", new ApiLangages[]{ApiLangages.FRENCH},new ApiLangages[]{ApiLangages.ARABIC});
                // String apiUrl ="";
                DldApiAndPrepare dldApiAndPrepare = new DldApiAndPrepare();
                dldApiAndPrepare.execute(apiUrl);
                dldApiAndPrepare.setApiDownloadListener(new DldApiAndPrepare.ApiDownloadListener() {
                    @Override
                    public void apiDownloadedSuccessfully(ArrayList<HeadlineItem> articles) {
                        doApiLaunch(recyclerView, articles);
                    }
                    @Override
                    public void apiDownloadFailed() {

                    }
                });
            }
        });
        breakingNewsThread.start();
    }

    private void doApiLaunch(RecyclerView recyclerView, ArrayList<HeadlineItem> articles) {
        MainActivity.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new ApiVariantRecyclerViewAdapter(articles));
            }
        });
    }
}
