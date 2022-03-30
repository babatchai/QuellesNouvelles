package ci.babatchai.nouvelleslocales.tts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.webkit.WebView;
import android.widget.LinearLayout;

import java.io.Console;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import javax.xml.transform.Result;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.WebViewManager;
import ci.babatchai.nouvelleslocales.outils.ResourceTools;

public class TextToSpeechActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech mTts;
    private ArrayList<String> mParagraphs;
    private int mParagraphId;
    private boolean mWaitForSpeechConditions;
    private boolean mDoFlush;

    public TextToSpeechActivity(Context context) {
        // Initialize text-to-speech. This is an asynchronous operation.
        // The OnInitListener (second argument) is called after initialization completes.
        mTts = new TextToSpeech(MainActivity.getContext(),
                this  // TextToSpeech.OnInitListener
        );

        mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                int id = Integer.valueOf(utteranceId);
                WebViewManager.textSelect(mParagraphs.get(id), ResourceTools.getActiveWebView());
            }

            @Override
            public void onDone(String utteranceId) {
            }

            @Override
            public void onError(String utteranceId) {
            }
        });
    }

    public void stopSpeetch(){
        mDoFlush = true;
        speetchFlush("",null);
    }

    // Implements TextToSpeech.OnInitListener.
    @Override
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.FRENCH);
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Lanuage data is missing or the language is not supported.
                System.out.println("Langue pas disponible");
            } else if (result == TextToSpeech.SUCCESS) {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.
                System.out.println("TTS initialisé avec succés");
            }
        } else {
            // Initialization failed.
            System.out.println("Could not initialize TextToSpeech.");
        }
    }

    public int stop(){
        return mTts.stop();
    }

    private void speetchAdd(String textToSpeak, String utteranceId) {
        mTts.speak(textToSpeak,
                TextToSpeech.QUEUE_ADD,  // Append to entries in the queue.
                null, utteranceId);
    }

    private void speetchFlush(String textToSpeak, String utteranceId) {
        mTts.speak(textToSpeak,
                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                null, utteranceId);
    }

    public void haveSpeechBySentences(ArrayList<String> paragraphs) {

        ArrayList<String> allSentences = new ArrayList<>();
        for(String paragraph : paragraphs) {
            paragraph = paragraph.replace("Mme. ", "Mme| ").replace("Mlle. ", "Mlle| ").replace("Me. ", "Me| ").replace("M. ", "M| ");
            String[] sentences = paragraph.split(Pattern.quote(". "));
            for (String sentence : sentences) {
                sentence = sentence.replace("Mme| ","Mme. ").replace("Mlle| ","Mlle. ").replace("Me| ","Me. ").replace("M| ","M. ");
                allSentences.add(sentence);
            }
        }
        haveSpeech(allSentences);
    }

    public void haveSpeech(ArrayList<String> paragraphs) {
        mParagraphId = 0;
        mParagraphs = paragraphs;
        mDoFlush = false;
        mWaitForSpeechConditions = true;
        Thread waiter = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (mWaitForSpeechConditions) {
                        if (mTts.isSpeaking()) {
                            Thread.sleep(500);
                        } else {
                            if (mParagraphId < paragraphs.size())
                                mWaitForSpeechConditions = true;
                            else
                                mWaitForSpeechConditions = false;

                            String speechPart = paragraphs.get(mParagraphId);
                            speetchAdd(speechPart, String.valueOf(mParagraphId));
                            mParagraphId++;
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        waiter.start();
    }



}
