package victor.app.tirinhas.brasil.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import victor.app.tirinhas.brasil.R;
import victor.app.tirinhas.brasil.objects.Meme;

/**
 * Created by victor on 09/08/15.
 */
public class MemeSelectionAdapter extends BaseAdapter {

    private ArrayList<Meme> memes;
    private Context mContext;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

    public MemeSelectionAdapter(Context c, ArrayList<Meme> memes) {
        this.memes = memes;
        mContext = c;

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
        return memes.size();
    }

    @Override
    public Object getItem(int position) {
        return memes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }

        String thumbnailSize = (mContext.getResources().getDisplayMetrics().density > 2.0) ?
                Meme.BIG_SQUARE : Meme.SMALL_SQUARE;

        imageLoader.displayImage(memes.get(position).getImageURL(thumbnailSize), imageView,
                displayImageOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        imageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        imageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        imageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                        imageView.setVisibility(View.GONE);
                    }
                });

        return imageView;
    }
}
