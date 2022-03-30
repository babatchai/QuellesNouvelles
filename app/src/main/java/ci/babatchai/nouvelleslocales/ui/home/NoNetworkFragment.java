package ci.babatchai.nouvelleslocales.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ci.babatchai.nouvelleslocales.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoNetworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoNetworkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameter
    String callbackFragmentName;

    public NoNetworkFragment() {
        callbackFragmentName = "";
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param caller Parameter 1.
     * @return A new instance of fragment NoNetworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoNetworkFragment newInstance() {
        NoNetworkFragment fragment = new NoNetworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, "");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            callbackFragmentName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_network, container, false);
    }
}