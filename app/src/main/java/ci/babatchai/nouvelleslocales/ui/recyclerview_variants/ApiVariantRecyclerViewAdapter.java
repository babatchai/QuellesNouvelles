package ci.babatchai.nouvelleslocales.ui.recyclerview_variants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ci.babatchai.nouvelleslocales.MainActivity;
import ci.babatchai.nouvelleslocales.R;
import ci.babatchai.nouvelleslocales.WebViewManager;
import ci.babatchai.nouvelleslocales.data.HeadlineItem;
import ci.babatchai.nouvelleslocales.databinding.FragmentApiItemBinding;
import ci.babatchai.nouvelleslocales.distiller.PageContentDistiller;
import ci.babatchai.nouvelleslocales.outils.TextCutter;
import ci.babatchai.nouvelleslocales.tts.OverlayType;
import ci.babatchai.nouvelleslocales.tts.SpeakService;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HeadlineItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ApiVariantRecyclerViewAdapter extends RecyclerView.Adapter<ApiVariantRecyclerViewAdapter.ViewHolder> {

    private static ArrayList<HeadlineItem> mValues = new ArrayList<>();
    private LinearLayout webViewWrapper;
    private LinearLayout overlay;
    private String webViewUrl;
    private String urlGuid;
    private SpeakService tts;
    private PageContentDistiller pageContentDistiller;
    private int paragraphId;
    private boolean waitForSpeechConditions;

    public ApiVariantRecyclerViewAdapter(ArrayList<HeadlineItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentApiItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(holder.getBindingAdapterPosition());

        String titVal = TextCutter.CutTextLength(mValues.get(holder.getBindingAdapterPosition()).getTitle(), TextCutter.MAX_TITLE_LEGTH);
        holder.mItemTitle.setText(titVal);

        String txtVal = TextCutter.CutTextLength(mValues.get(holder.getBindingAdapterPosition()).getText(), TextCutter.MAX_TEXT_LENGTH);
        holder.mItemContent.setText(txtVal);
        if(mValues.get(holder.getBindingAdapterPosition()).getAuthor() != null &&
                !mValues.get(holder.getBindingAdapterPosition()).getAuthor().equals("null")) {
            holder.mItemAuthor.setVisibility(View.VISIBLE);
            holder.mItemAuthor.setText(mValues.get(holder.getBindingAdapterPosition()).getAuthor());
        }else{
            holder.mItemAuthor.setVisibility(View.GONE);
        }
        holder.mItemDate.setText(mValues.get(holder.getBindingAdapterPosition()).getPubDate());
        if(mValues.get(holder.getBindingAdapterPosition()).getImageUrl() != null &&
                !mValues.get(holder.getBindingAdapterPosition()).getImageUrl().isEmpty()) {
            holder.mItemImage.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(mValues.get(holder.getBindingAdapterPosition()).getImageUrl())
                    .resize(300, 200)
                    .into(holder.mItemImage);
//            new DldAndSetImage().setImage(mValues.get(holder.getBindingAdapterPosition()).getImageUrl(),
//                    holder.mItemImage);
        }else{
            holder.mItemImage.setVisibility(View.GONE);
        }
        if (holder.getBindingAdapterPosition() % 2 == 0) {
            holder.itemView.setBackground(MainActivity.getContext().getResources().getDrawable(R.drawable.list_gradient_even, null));
            holder.mItemTitle.setTextColor(MainActivity.getContext().getResources().getColor(R.color.black, null));
            holder.mItemContent.setTextColor(MainActivity.getContext().getResources().getColor(R.color.black, null));
            holder.mItemAuthor.setTextColor(MainActivity.getContext().getResources().getColor(R.color.black, null));
            holder.mItemDate.setTextColor(MainActivity.getContext().getResources().getColor(R.color.black, null));

        } else {
            holder.mItemTitle.setTextColor(MainActivity.getContext().getResources().getColor(R.color.black, null));
            holder.mItemContent.setTextColor(MainActivity.getContext().getResources().getColor(R.color.black, null));
            holder.mItemAuthor.setTextColor(MainActivity.getContext().getResources().getColor(R.color.black, null));
            holder.itemView.setBackground(MainActivity.getContext().getResources().getDrawable(R.drawable.list_gradient_odd, null));
            holder.mItemDate.setTextColor(MainActivity.getContext().getResources().getColor(R.color.black, null));
        }

        holder.mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title =TextCutter.CutTextLength(mValues.get(holder.getBindingAdapterPosition()).getTitle(), TextCutter.MAX_TITLE_LEGTH);
                String descr= TextCutter.CutTextLength(mValues.get(holder.getBindingAdapterPosition()).getText(), TextCutter.MAX_TEXT_LENGTH);
                ArrayList<String> sentences = new ArrayList<>();
                sentences.add(title);
                sentences.add(descr);
                SpeakService.getIstance().speetch(sentences);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewUrl = mValues.get(holder.getBindingAdapterPosition()).getLink();
                urlGuid = mValues.get(holder.getBindingAdapterPosition()).getGuid();
                WebViewManager webViewManager = new WebViewManager();
                webViewWrapper = webViewManager.displayWebView(webViewUrl);
            }
        });

        ImageButton read_web_btn = MainActivity.getActivity().findViewById(R.id.api_read_web_button);
        read_web_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts = SpeakService.getIstance();
                ProgressBar loadProgress  = MainActivity.getActivity().findViewById(R.id.api_read_Web_progress);

                        loadProgress.setVisibility(View.VISIBLE);
                        loadProgress.bringToFront();

                        pageContentDistiller = new PageContentDistiller();
                        pageContentDistiller.execute(webViewUrl, urlGuid);
                        pageContentDistiller.setPageContentDistillerListener(new PageContentDistiller.PageContentDistillerListener() {
                            @Override
                            public void pageContentDistillerSuccessfully(ArrayList<String> paragraphs) {
                                    tts.speetch(paragraphs);
                                    loadProgress.setVisibility(View.GONE);
                                    read_web_btn.setVisibility(View.VISIBLE);

                            };

                            @Override
                            public void pageContentDistillerFailed() {
                                loadProgress.setVisibility(View.GONE);
                                read_web_btn.setVisibility(View.VISIBLE);
                            }
                        });
                    }

        });
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

        public ViewHolder(@NonNull FragmentApiItemBinding binding) {
            super(binding.getRoot());
            mItemTitle = binding.apiItemTitle;
            mItemContent = binding.apiContent;
            mItemAuthor = binding.apiItemAuthor;
            mItemImage = binding.apiItemImg;
            mItemDate = binding.apiItemDate;
            mImgButton = binding.apiReadWebButton;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItemTitle.getText() + "'";
        }
    }
}