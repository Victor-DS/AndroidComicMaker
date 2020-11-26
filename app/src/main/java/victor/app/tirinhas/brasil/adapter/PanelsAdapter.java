package victor.app.tirinhas.brasil.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import victor.app.tirinhas.brasil.PanelsFragment;
import victor.app.tirinhas.brasil.R;
import victor.app.tirinhas.brasil.objects.Meme;
import victor.app.tirinhas.brasil.objects.Panel;

/**
 * Created by victor on 11/08/15.
 */
public class PanelsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Panel> panels;
    private PanelsFragment.OnPanelClickListener mListener;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

    public PanelsAdapter(Context c, ArrayList<Panel> panels) {
        mContext = c;
        this.panels = panels;
        mListener = (PanelsFragment.OnPanelClickListener) c;

        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                        //.showImageOnFail(R.drawable.warning)
                .build();

        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .build();
        imageLoader.init(config);
    }

    @Override
    public int getCount() {
        return panels.size();
    }

    @Override
    public Object getItem(int position) {
        return panels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.row_panel, parent, false);
        }

        final boolean isLeft = panels.get(position).isLeft();

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBarPanel);
        progressBar.setVisibility(View.GONE);

        final ProgressBar progressBarRight = (ProgressBar)
                view.findViewById(R.id.progressBarPanelRight);
        progressBarRight.setVisibility(View.GONE);

        final ImageView meme = (ImageView) view.findViewById(R.id.imageMemePanel);
        meme.setVisibility(View.VISIBLE);

        final ImageView memeRight = (ImageView) view.findViewById(R.id.imageMemePanelRight);
        memeRight.setVisibility(View.GONE);

        View.OnClickListener imageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onImageClickListener(position);
            }
        };

        memeRight.setOnClickListener(imageClickListener);
        meme.setOnClickListener(imageClickListener);

        ImageLoadingListener imgListener = new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);

                progressBar.setVisibility(isLeft ? View.VISIBLE : View.GONE);
                meme.setVisibility(isLeft ? View.GONE : View.VISIBLE);

                progressBarRight.setVisibility(isLeft ? View.GONE : View.VISIBLE);
                memeRight.setVisibility(isLeft ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);

                progressBar.setVisibility(View.GONE);
                progressBarRight.setVisibility(View.GONE);

                meme.setVisibility(isLeft ? View.VISIBLE : View.GONE);
                memeRight.setVisibility(isLeft ? View.GONE : View.VISIBLE);

                meme.setImageResource(R.drawable.dolan);
                memeRight.setImageResource(R.drawable.dolan);

                Toast.makeText(mContext, R.string.failed_to_load_image,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                progressBar.setVisibility(View.GONE);
                progressBarRight.setVisibility(View.GONE);

                meme.setVisibility(isLeft ? View.VISIBLE : View.GONE);
                memeRight.setVisibility(isLeft ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
                progressBar.setVisibility(View.GONE);
                meme.setVisibility(View.GONE);

                progressBarRight.setVisibility(View.GONE);
                memeRight.setVisibility(View.GONE);
            }
        };

        if(isLeft) {
            imageLoader.displayImage(panels.get(position).getMeme().getImageURL(Meme.ORIGINAL_SIZE),
                    meme, displayImageOptions, imgListener);
        } else {
            imageLoader.displayImage(panels.get(position).getMeme().getImageURL(Meme.ORIGINAL_SIZE),
                    memeRight, displayImageOptions, imgListener);
        }

        final TextView text = (TextView) view.findViewById(R.id.textViewPanel);
        text.setText(panels.get(position).getText());
        text.setTextColor(panels.get(position).getTextColor());
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTextClickListener(position, text.getText().toString(),
                        text.getCurrentTextColor(), text.getTextSize());
            }
        });
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, panels.get(position).getTextSize());

        return view;
    }

}
