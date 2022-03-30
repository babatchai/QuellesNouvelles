package ci.babatchai.nouvelleslocales.ui.recyclerview_variants;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Pattern;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.WebViewManager;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;
import ci.babatchai.nouvelleslocales.databinding.FragmentRssItemBinding;
import ci.babatchai.nouvelleslocales.distiller.PageContentDistiller;
import ci.babatchai.nouvelleslocales.outils.ResourceTools;
import ci.babatchai.nouvelleslocales.outils.TextCutter;
import ci.babatchai.nouvelleslocales.tts.OverlayType;
import ci.babatchai.nouvelleslocales.tts.SpeakService;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HeadlineItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RssVariantRecyclerViewAdapter extends RecyclerView.Adapter<RssVariantRecyclerViewAdapter.ViewHolder> {

    private static ArrayList<HeadlineItem> mValues = new ArrayList<>();
    private LinearLayout webViewWrapper;
    private Context mContext;
    private LinearLayout overlay;
    private FrameLayout muteOverlay;
    private SpeakService speakService;
    private String webViewUrl;
    private String urlGuid;
    private WebViewManager webViewManager;
    private boolean titleSelected;
    private boolean textSelected;
    ImageButton read_web_btn = null;
    private ProgressBar loadProgress;
    private PageContentDistiller pageContentDistiller;
    private int paragraphId;
    private SpeakService tts;
    private boolean waitForSpeechConditions;

    public RssVariantRecyclerViewAdapter(Context context, ArrayList<HeadlineItem> items) {
        mValues = items;
        mContext = context;
        tts = SpeakService.getIstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder itemView = new ViewHolder(FragmentRssItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        speakService = SpeakService.getIstance();
        return itemView;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(holder.getBindingAdapterPosition());
        if(holder.mItem != null) {
            String[] location = holder.mItem.getTitle().split(Pattern.quote(":"));
            if (location.length > 0) {
                holder.mItem.setRegion(location[0]
                        .replace(" ", "")
                        .replace("'","")
                        .replace("-","")
                        .replace("é","e")
                        .toLowerCase());
            }
        }
        String titVal = TextCutter.CutTextLength(mValues.get(holder.getBindingAdapterPosition()).getTitle(), TextCutter.MAX_TITLE_LEGTH);
        holder.mItemTitle.setText(titVal);

        String txtVal = TextCutter.CutTextLength(mValues.get(holder.getBindingAdapterPosition()).getText(), TextCutter.MAX_TEXT_LENGTH);
        holder.mItemContent.setText(txtVal);

        holder.mItemAuthor.setText(mValues.get(holder.getBindingAdapterPosition()).getAuthor());
        holder.mItemDate.setText(mValues.get(holder.getBindingAdapterPosition()).getPubDate());
        if (mValues.get(holder.getBindingAdapterPosition()).getImageUrl() != null &&
                !mValues.get(holder.getBindingAdapterPosition()).getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(holder.mItem.getImageUrl())
                    .resize(300, 200)
                    .into(holder.mItemImage);
        } else {
            holder.mItemImage.setVisibility(View.GONE);
        }

        int background = getResourceIdByName(holder.mItem.getRegion());
        if(holder.mItem.getRegion() != null && !holder.mItem.getRegion().isEmpty() && background > 0){
            holder.itemView.setBackground(mContext.getResources().getDrawable(background, null));
            holder.mItemTitle.setTextColor(mContext.getResources().getColor(R.color.black, null));
            holder.mItemContent.setTextColor(mContext.getResources().getColor(R.color.black, null));
            holder.mItemAuthor.setTextColor(mContext.getResources().getColor(R.color.black, null));
            holder.mItemDate.setTextColor(mContext.getResources().getColor(R.color.black, null));
        }else {
            if (holder.getBindingAdapterPosition() % 2 == 0) {
                holder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.list_gradient_even, null));
                holder.mItemTitle.setTextColor(mContext.getResources().getColor(R.color.black, null));
                holder.mItemContent.setTextColor(mContext.getResources().getColor(R.color.black, null));
                holder.mItemAuthor.setTextColor(mContext.getResources().getColor(R.color.black, null));
                holder.mItemDate.setTextColor(mContext.getResources().getColor(R.color.black, null));

            } else {
                holder.mItemTitle.setTextColor(mContext.getResources().getColor(R.color.black, null));
                holder.mItemContent.setTextColor(mContext.getResources().getColor(R.color.black, null));
                holder.mItemAuthor.setTextColor(mContext.getResources().getColor(R.color.black, null));
                holder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.list_gradient_odd, null));
                holder.mItemDate.setTextColor(mContext.getResources().getColor(R.color.black, null));
            }
        }
        holder.mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = TextCutter.CutTextLength(mValues.get(holder.getBindingAdapterPosition()).getTitle(), TextCutter.MAX_TITLE_LEGTH);
                String descr = TextCutter.CutTextLength(mValues.get(holder.getBindingAdapterPosition()).getText(), TextCutter.MAX_TEXT_LENGTH);                ArrayList<String> sentences = new ArrayList<>();
                ArrayList<String> tesasers = new ArrayList<>();
                tesasers.add(title);
                tesasers.add(descr);
                SpeakService.getIstance().speetch(tesasers);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewUrl = mValues.get(holder.getBindingAdapterPosition()).getLink().replace("http://","https://");
                webViewManager = new WebViewManager();
                webViewWrapper = webViewManager.displayWebView(webViewUrl);
                webViewManager.setWebViewWatcherListener(new WebViewManager.WebViewWatcherListener() {
                    @Override
                    public void setFinished(boolean finished) {
                        tts.abortSpeetch();
                    }
                });
                hideHomeImage();
            }
        });

        read_web_btn = MainActivity.getActivity().findViewById(R.id.rss_read_Web_btn);
        if (read_web_btn == null) {
            read_web_btn = MainActivity.getActivity().findViewById(R.id.api_read_Web_btn);
        }

        read_web_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.abortSpeetch();
                    loadProgress = MainActivity.getActivity().findViewById(R.id.api_webreader_progress);
                    if(loadProgress == null){
                        loadProgress = MainActivity.getActivity().findViewById(R.id.rss_webreader_progress);
                    }

/***************************************************************/
                    loadProgress.setVisibility(View.VISIBLE);
                    loadProgress.bringToFront();

                    pageContentDistiller = new PageContentDistiller();
                    pageContentDistiller.execute(webViewUrl);
                    pageContentDistiller.setPageContentDistillerListener(new PageContentDistiller.PageContentDistillerListener() {
                        @Override
                        public void pageContentDistillerSuccessfully(ArrayList<String> paragraphs) {
                            tts.speetch(paragraphs);
                            loadProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void pageContentDistillerFailed() {
                            loadProgress.setVisibility(View.GONE);
                        }
                    });
                }

        });

    }


    private void hideHomeImage() {
        ImageView imageView = MainActivity.getActivity().findViewById(R.id.kioskwall);
        if(imageView != null && imageView.getVisibility() != View.GONE){
            imageView.animate()
                    .scaleY(-0f)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(1000);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageView.setVisibility(View.GONE);
                }
            }, 2000);

        }
    }

    private int getResourceIdByName(String region) {
        int drawableResourceId = -1;
        try {
            drawableResourceId = mContext.getResources().
                    getIdentifier(region, "drawable", mContext.getPackageName());
        }catch(Exception e){
            e.printStackTrace();
        }
        return drawableResourceId;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemTitle;
        public final TextView mItemContent;
        public final TextView mItemAuthor;
        public final ImageView mItemImage;
        public TextView mItemDate;
        public HeadlineItem mItem;
        public ImageButton mImgButton;

        public ViewHolder(@NonNull FragmentRssItemBinding binding) {
            super(binding.getRoot());
            mItemTitle = binding.interItemTitle;
            mItemContent = binding.interContent;
            mItemAuthor = binding.interItemAuthor;
            mItemImage = binding.interItemImg;
            mItemDate = binding.interItemDate;
            mImgButton = binding.interReadButton;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItemTitle.getText() + "'";
        }
    }
}