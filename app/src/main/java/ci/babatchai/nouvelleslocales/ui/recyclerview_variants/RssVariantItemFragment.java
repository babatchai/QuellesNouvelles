package ci.babatchai.nouvelleslocales.ui.recyclerview_variants;

import static ci.babatchai.nouvelleslocales.outils.NetworkingTools.getDomainFromUrl;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.assertj.core.internal.Urls;

import java.util.ArrayList;
import java.util.UUID;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;
import ci.babatchai.nouvelleslocales.data.UrlDataItem;
import ci.babatchai.nouvelleslocales.data.UrlLists;
import ci.babatchai.nouvelleslocales.outils.ResourceTools;
import ci.babatchai.nouvelleslocales.rsshandling.DldRssAndPrepare;

/**
 * A fragment representing a list of Items.
 */
public class RssVariantItemFragment extends Fragment{
    Context mContext;
    ArrayList<UrlDataItem> urlDataList;
    private ArrayList<HeadlineItem> headlines;
    RecyclerView recyclerView;
    LinearLayout overlay;
    Fragment that;
    // TODO: Customize parameter argument names
    private static final String ARG_URL = "url";
    // TODO: Customize parameters
    private String mUrl;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RssVariantItemFragment(Context context, String url) {
        mContext = context;
        mUrl = url;
        that = this;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RssVariantItemFragment newInstance(String url) {
        RssVariantItemFragment fragment = new RssVariantItemFragment(MainActivity.getContext(), url);
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl= ARG_URL;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_item_list, container, false);
        recyclerView = view.findViewById(R.id.rss_news_list);
        // Set the adapter
        if (recyclerView instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            Thread callRss = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DldRssAndPrepare dldRssAndPrepare = new DldRssAndPrepare(that);
                        dldRssAndPrepare.setRssDownloadListener(new DldRssAndPrepare.RssDownloadListener() {
                            @Override
                            public void rssDownloadedSuccessfully(ArrayList<HeadlineItem> articles) {
                                doLaunch(articles);
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
                        dldRssAndPrepare.execute(mUrl);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            });
            callRss.start();
        }
        return view;
    }

    private void doLaunch(ArrayList<HeadlineItem> articles) {
        overlay = ResourceTools.getWebOverlay();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new RssVariantRecyclerViewAdapter(mContext, articles));
                overlay.setVisibility(View.GONE);
            }
        });
    }
}