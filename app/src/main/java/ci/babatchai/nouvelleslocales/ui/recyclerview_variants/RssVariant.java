package ci.babatchai.nouvelleslocales.ui.recyclerview_variants;

import static ci.babatchai.nouvelleslocales.outils.NetworkingTools.getDomainFromUrl;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;
import ci.babatchai.nouvelleslocales.data.UrlDataItem;
import ci.babatchai.nouvelleslocales.data.UrlLists;
import ci.babatchai.nouvelleslocales.outils.NetworkingTools;
import ci.babatchai.nouvelleslocales.rsshandling.DldRssAndPrepare;
import ci.babatchai.nouvelleslocales.ui.home.NoNetworkFragment;

public class RssVariant {
    private Context mContext;

    public RssVariant(Context mContext) {
        this.mContext = mContext;
    }

    public void rv_rss_variant(LinearLayout overlay, RecyclerView recyclerView, String url, Fragment caller){
        if(!NetworkingTools.isDeviceConnToInternet(mContext)){
            caller.getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_navigation,
                            new NoNetworkFragment())
                    .commitNow();

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Thread callRss = new Thread(new Runnable() {
            @Override
            public void run() {
                UrlDataItem urlItem = UrlLists.getItemForDomain(getDomainFromUrl(url));
                try {
                    DldRssAndPrepare dldRssAndPrepare = new DldRssAndPrepare(caller);
                    dldRssAndPrepare.setRssDownloadListener(new DldRssAndPrepare.RssDownloadListener() {
                        @Override
                        public void rssDownloadedSuccessfully(ArrayList<HeadlineItem> articles) {
                            doRssLaunch(overlay, recyclerView, articles);
                        }

                        @Override
                        public void rssDownloadFailed() {
                            try {
                                throw new Exception("RSS download failed");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dldRssAndPrepare.execute(urlItem.getFeedUrl());
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        callRss.start();
    }

    private void doRssLaunch(LinearLayout overlay, RecyclerView recyclerView, ArrayList<HeadlineItem> articles) {
        MainActivity.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new RssVariantRecyclerViewAdapter(mContext, articles));
                overlay.setVisibility(View.GONE);
            }
        });
    }
}
