package ci.babatchai.nouvelleslocales.ui.international;

import static ci.babatchai.nouvelleslocales.outils.ResourceTools.getResId;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.UUID;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.R;


import ci.babatchai.nouvelleslocales.data.UrlDataItem;
import ci.babatchai.nouvelleslocales.data.UrlLists;

import ci.babatchai.nouvelleslocales.databinding.FragmentInternationalBinding;
import ci.babatchai.nouvelleslocales.outils.ResourceTools;
import ci.babatchai.nouvelleslocales.ui.recyclerview_variants.RssVariantItemFragment;

public class InternationalFragment extends Fragment implements View.OnClickListener{

    private FragmentInternationalBinding binding;
    ExtendedFloatingActionButton fabMain;
    LinearLayout buttonRoot;
    LinearLayout overlay;

    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    boolean isMenuOpen = false;

    public InternationalFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInternationalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        buttonRoot = root.findViewById(R.id.buttonHost);

        fabMain = new ExtendedFloatingActionButton(getContext());
        fabMain.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        fabMain.setTag(null);
        fabMain.setBackgroundColor(getContext().getResources().getColor(R.color.menu_00, null));
        fabMain.setOnClickListener(this);
        ((LinearLayout)buttonRoot).addView(fabMain);

        ArrayList<UrlDataItem> dataItems = UrlLists.getUrlData();
        int itemCount=1;
        Toolbar toolbar = MainActivity.getActivity().findViewById(R.id.toolbar);
        String section = toolbar.getTitle().toString();
        String filter = "";
        if(section.equals("International")){
            filter = "inter";
            fabMain.setText("International");
        }else if(section.equals("National")){
            filter = "natio";
            fabMain.setText("National");
        }
        for(UrlDataItem item : dataItems){
            if(item.getFeedCategorie().equalsIgnoreCase(filter)) {
                ExtendedFloatingActionButton efab = new ExtendedFloatingActionButton(getContext());
                efab.setIcon(getContext().getResources().getDrawable(R.drawable.ic_menu_slideshow, null));
                efab.setText(item.getFeedName());
                efab.setTag(item);
                efab.setAlpha(0f);
                efab.setBackgroundColor(
                        getContext().getResources().getColor(
                                getResId("menu_0" + itemCount, R.color.class), null));
                efab.setOnClickListener(this);
                ((LinearLayout) buttonRoot).addView(efab);
                itemCount++;
            }
        }

        openMenu();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        for(int i = 1;i < buttonRoot.getChildCount();i++) {
            ((ExtendedFloatingActionButton) buttonRoot.getChildAt(i)).setVisibility(View.VISIBLE);
        }
        fabMain.animate().setInterpolator(interpolator).setDuration(300).start();
        for(int i = 1;i < buttonRoot.getChildCount();i++) {
            ((ExtendedFloatingActionButton) buttonRoot.getChildAt(i)).animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        }
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;


        for(int i = 1;i < buttonRoot.getChildCount();i++) {
            ((ExtendedFloatingActionButton) buttonRoot.getChildAt(i)).animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        }
        fabMain.animate().setInterpolator(interpolator).setDuration(300).start();
        for(int i = 1;i < buttonRoot.getChildCount();i++) {
            ((ExtendedFloatingActionButton) buttonRoot.getChildAt(i)).setVisibility(View.GONE);
        }
    }

    private void handleFabOne() {

    }

    @Override
    public void onClick(View view) {
        if(view.getTag() != null){
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.inter_nav_host,
                            new RssVariantItemFragment(getContext(), ((UrlDataItem)view.getTag()).getFeedUrl()))
                    .commitNow();
        }
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}