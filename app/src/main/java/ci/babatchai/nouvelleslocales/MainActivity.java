package ci.babatchai.nouvelleslocales;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import ci.babatchai.nouvelleslocales.data.UrlDataItem;
import ci.babatchai.nouvelleslocales.outils.ResourceTools;
import ci.babatchai.nouvelleslocales.tts.SpeakService;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ci.babatchai.nouvelleslocales.databinding.ActivityNavigationBinding binding;
    private static ArrayList<UrlDataItem> urlItems;
    public static SpeakService speakService;
    private FrameLayout muteOverlay;
    private static MainActivity self;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        binding = ci.babatchai.nouvelleslocales.databinding.ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarNavigation.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_international, R.id.nav_national)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        speakService = SpeakService.getIstance();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Button closeBtn = ResourceTools.getActiveBackButton();
            if(closeBtn != null){
                closeBtn.performClick();
            }else{
                if (doubleBackToExitPressedOnce) {
                    this.finishAffinity();
                }else {

                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Pressez le bouton encore une fois pour quitter l'application!", Toast.LENGTH_SHORT).show();
                }
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static Activity getActivity(){
        return self;
    }

    public static Context getContext(){
        return self.getApplicationContext();
    }
}