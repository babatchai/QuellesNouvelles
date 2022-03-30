package ci.babatchai.nouvelleslocales.ui.recyclerview_variants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;

/**
 * A fragment representing a list of Items.
 */
public class ApiVariantItemFragment extends Fragment{

    ArrayList<String> urls;
    private ArrayList<HeadlineItem> headlines;
    RecyclerView recyclerView;
    // TODO: Customize parameter argument names
    private static final String ARG_URL_ID = "url_id";
    // TODO: Customize parameters
    private int mUrl_Id = 1;
    private LinearLayout overlay;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ApiVariantItemFragment(int urlId) {
        mUrl_Id = urlId;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ApiVariantItemFragment newInstance(int urlId) {
        ApiVariantItemFragment fragment = new ApiVariantItemFragment(urlId);
        Bundle args = new Bundle();
        args.putInt(ARG_URL_ID, urlId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUrl_Id = getArguments().getInt(ARG_URL_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }


}