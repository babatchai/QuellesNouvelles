package ci.babatchai.nouvelleslocales.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.WebViewManager;
import ci.babatchai.nouvelleslocales.data.UrlLists;
import ci.babatchai.nouvelleslocales.databinding.FragmentHomeBinding;
import ci.babatchai.nouvelleslocales.outils.ResourceTools;
import ci.babatchai.nouvelleslocales.ui.international.InternationalFragment;
import ci.babatchai.nouvelleslocales.ui.recyclerview_variants.RssVariantItemFragment;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.home_nav_host, new RssVariantItemFragment(getContext(), UrlLists.getItemForHome().getFeedUrl()))
                        .commitNow();
            }
        }, 500);
    }

    @Override
    public boolean getAllowEnterTransitionOverlap() {
        return super.getAllowEnterTransitionOverlap();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}