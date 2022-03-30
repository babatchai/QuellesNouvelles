package ci.babatchai.nouvelleslocales.tts;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import java.nio.charset.MalformedInputException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.security.auth.DestroyFailedException;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.WebViewManager;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;


public class SpeakService{
    private TextToSpeechActivity tts;
    private ArrayList<String> mParagraphs;
    private static SpeakService instance;

    private SpeakService() {
        tts = new TextToSpeechActivity(MainActivity.getContext());
    }

    public static synchronized SpeakService getIstance() {
        if (instance == null) {
            instance = new SpeakService();
        }
        return instance;
    }

    public void speetch(ArrayList<String> paragraphs){
        tts.haveSpeechBySentences(paragraphs);
    }

    public void abortSpeetch(){
        tts.stopSpeetch();
    }
}