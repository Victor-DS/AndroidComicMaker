package victor.app.tirinhas.brasil.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ValueBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;

import victor.app.tirinhas.brasil.GoogleAnalytics.AnalyticsApplication;
import victor.app.tirinhas.brasil.IAB.IabHelper;
import victor.app.tirinhas.brasil.IAB.IabResult;
import victor.app.tirinhas.brasil.IAB.Inventory;
import victor.app.tirinhas.brasil.IAB.Purchase;
import victor.app.tirinhas.brasil.MemesDrawerFragment;
import victor.app.tirinhas.brasil.NavigationDrawerFragment;
import victor.app.tirinhas.brasil.PanelsFragment;
import victor.app.tirinhas.brasil.R;
import victor.app.tirinhas.brasil.objects.Meme;
import victor.app.tirinhas.brasil.objects.Panel;
import victor.app.tirinhas.brasil.util.GeneratorHelper;
import victor.app.tirinhas.brasil.util.Util;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        MemesDrawerFragment.MemesDrawerCallbacks,
        PanelsFragment.OnPanelClickListener {

    //region Google Analytics Variable
    private Tracker t;
    //endregion

    //region Billing Variables
    private IabHelper mHelper;
    private boolean mIsPremium;
    private final String SKU_PREMIUM = "premium_extras";
    //endregion

    //region Helpers
    private GeneratorHelper generator;
    //endregion

    //region Fragments
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MemesDrawerFragment memesDrawerFragment;
    private PanelsFragment panelsFragment;
    //endregion

    //region Variables
    private int selectedPanel;
    private boolean isAnyPanelSelected, doubleBackToExitPressedOnce;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    //endregion

    //TODO Tap to reload image button
    //TODO Swipe left to delete
    //TODO Analytics
    //TODO Invite friends

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Analytics
        initAnalytics();

        //Billing
        initBilling();

        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                        //.showImageOnFail(R.drawable.warning)
                .build();

        generator = new GeneratorHelper(this);

        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        imageLoader.init(config);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        memesDrawerFragment = (MemesDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.memes_drawer);
        memesDrawerFragment.setUp(R.id.memes_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        panelsFragment = new PanelsFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, panelsFragment).commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            //TODO Google Analytics when the user clicks on each of the buttons
            case 0:
                Util.aboutDialog(this);
                break;
            case 1:
                Util.rateThisApp(this);
                break;
            case 2:
                Util.goToOtherApps(this);
                break;
            case 3:
                mHelper.launchPurchaseFlow(this, SKU_PREMIUM, 1001,
                        mPurchaseFinishedListener, "");
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    public String getJSON() throws NullPointerException {
        return getIntent().getExtras().getString("Main characters JSON", null) != null ?
                getIntent().getExtras().getString("Main characters JSON", null) :
                Util.getJSON(this);
    }

    public void initAnalytics() {
        t = ((AnalyticsApplication) getApplication())
                .getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Tela Principal");
        t.setLanguage(this.getResources().getConfiguration().locale.getCountry());
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public void onBackPressed() {
        if(memesDrawerFragment.isDrawerOpen()
                || mNavigationDrawerFragment.isDrawerOpen()) {
            memesDrawerFragment.closeDrawer();
            mNavigationDrawerFragment.closeDrawer();
            return;
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.double_press_to_exit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.add_panel) {
            panelsFragment.addPanel();
            return true;
        } else if(id == R.id.remove_panel) {
            if(panelsFragment.getPanelsSize() > 1)
                panelsFragment.removePanel();
            else
                Toast.makeText(this, R.string.you_need_one, Toast.LENGTH_LONG).show();
            return true;
        } else if(id == R.id.save_image) {
            generator.saveImage(panelsFragment.getAllPanels());
            return true;
        } else if(id == R.id.share_image) {
            if(generator.getLastImage() == null) {
                Toast.makeText(this, R.string.save_image_first, Toast.LENGTH_LONG).show();
                Toast.makeText(this, R.string.share_last_saved, Toast.LENGTH_LONG).show();
                return false;
            }

            ContentValues values = new ContentValues(2);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            values.put(MediaStore.Images.Media.DATA, generator.getLastImage());
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);
            Intent nick = new Intent();
            nick.setAction(Intent.ACTION_SEND);
            nick.setType("image/*");
            nick.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(nick);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMemeSelected(Meme meme) {
        final int selectedPanel = this.selectedPanel;
        View v = panelsFragment.getListView().getChildAt(selectedPanel -
                panelsFragment.getListView().getFirstVisiblePosition());

        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBarPanel);
        final ProgressBar progressBarRight = (ProgressBar)
                v.findViewById(R.id.progressBarPanelRight);

        final ImageView image = (ImageView) v.findViewById(R.id.imageMemePanel);
        final ImageView imageRight = (ImageView) v.findViewById(R.id.imageMemePanel);


        ImageLoadingListener loadingListener = new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
                if(panelsFragment.getPanelAt(selectedPanel).isLeft()) {
                    progressBar.setVisibility(View.VISIBLE);
                    image.setVisibility(View.GONE);
                } else {
                    progressBarRight.setVisibility(View.VISIBLE);
                    imageRight.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                if(panelsFragment.getPanelAt(selectedPanel).isLeft()) {
                    progressBar.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.sad_baby_error);
                } else {
                    progressBarRight.setVisibility(View.GONE);
                    imageRight.setVisibility(View.VISIBLE);
                    imageRight.setImageResource(R.drawable.sad_baby_error);
                }

                Toast.makeText(MainActivity.this, R.string.failed_to_load_image,
                        Toast.LENGTH_SHORT).show();

                Toast.makeText(MainActivity.this, R.string.tap_to_try_again,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                if(panelsFragment.getPanelAt(selectedPanel).isLeft()) {
                    progressBar.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                } else {
                    progressBarRight.setVisibility(View.GONE);
                    imageRight.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
                if(panelsFragment.getPanelAt(selectedPanel).isLeft()) {
                    progressBar.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.sad_baby_error);
                } else {
                    progressBarRight.setVisibility(View.GONE);
                    imageRight.setVisibility(View.VISIBLE);
                    imageRight.setImageResource(R.drawable.sad_baby_error);
                }

                Toast.makeText(MainActivity.this, R.string.image_cancelled,
                        Toast.LENGTH_SHORT).show();

                Toast.makeText(MainActivity.this, R.string.tap_to_try_again,
                        Toast.LENGTH_LONG).show();
            }
        };

        if(panelsFragment.getPanelAt(selectedPanel).isLeft()) {
            imageLoader.displayImage(meme.getImageURL(Meme.ORIGINAL_SIZE), image,
                    displayImageOptions, loadingListener);
        } else {
            imageLoader.displayImage(meme.getImageURL(Meme.ORIGINAL_SIZE), imageRight,
                    displayImageOptions, loadingListener);
        }

        panelsFragment.updateMeme(selectedPanel, meme);

        this.selectedPanel = 0;
        isAnyPanelSelected = false;

        panelsFragment.updateView(v, selectedPanel);
        memesDrawerFragment.closeDrawer();
    }

    @Override
    public void flipPanel(boolean left) {
        panelsFragment.setPanelIsLeft(selectedPanel, left);
        View v = panelsFragment.getListView().getChildAt(selectedPanel -
                panelsFragment.getListView().getFirstVisiblePosition());

        final ImageView memeLeft = (ImageView) v.findViewById(R.id.imageMemePanel);
        final ImageView memeRight = (ImageView) v.findViewById(R.id.imageMemePanelRight);

        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBarPanel);
        final ProgressBar progressBarRight = (ProgressBar)
                v.findViewById(R.id.progressBarPanelRight);

        memeLeft.setVisibility(left ? View.VISIBLE : View.GONE);
        memeRight.setVisibility(left ? View.GONE : View.VISIBLE);

        imageLoader.displayImage(panelsFragment.getPanelAt(selectedPanel).getMeme()
                        .getImageURL(Meme.ORIGINAL_SIZE), panelsFragment.getPanelAt(selectedPanel)
                        .isLeft() ? memeLeft : memeRight,
                displayImageOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        progressBar.setVisibility(panelsFragment.getPanelAt(selectedPanel).isLeft()
                                ? View.VISIBLE : View.GONE);
                        progressBarRight.setVisibility(panelsFragment.getPanelAt(selectedPanel)
                                .isLeft() ? View.GONE : View.VISIBLE);

                        memeLeft.setVisibility(View.GONE);
                        memeRight.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        boolean isLeft = panelsFragment.getPanelAt(selectedPanel).isLeft();

                        progressBar.setVisibility(View.GONE);
                        progressBarRight.setVisibility(View.GONE);

                        memeLeft.setVisibility(isLeft ? View.VISIBLE : View.GONE);
                        memeLeft.setImageResource(R.drawable.dolan);

                        memeRight.setVisibility(isLeft ? View.GONE : View.VISIBLE);
                        memeRight.setImageResource(R.drawable.dolan);

                        Toast.makeText(MainActivity.this, R.string.failed_to_load_image,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        boolean isLeft = panelsFragment.getPanelAt(selectedPanel).isLeft();

                        progressBar.setVisibility(View.GONE);
                        progressBarRight.setVisibility(View.GONE);

                        memeLeft.setVisibility(isLeft ? View.VISIBLE : View.GONE);
                        memeRight.setVisibility(isLeft ? View.GONE : View.VISIBLE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);

                        progressBar.setVisibility(View.GONE);
                        memeLeft.setVisibility(View.GONE);

                        progressBarRight.setVisibility(View.GONE);
                        memeRight.setVisibility(View.GONE);

                    }
                });

        panelsFragment.updateView(v, selectedPanel);
        memesDrawerFragment.closeDrawer();
    }

    @Override
    public void onTextClickListener(final int position, String originalText,
                                    int originalColor, float originalSize) {
        LayoutInflater lInflater = this.getLayoutInflater();
        View layout = lInflater.inflate(R.layout.dialog_text_change, null);
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setView(layout);

        final EditText eText = (EditText) layout.findViewById(R.id.editTextDialog);
        eText.setText(originalText);

        final TextView sampleText = (TextView) layout.findViewById(R.id.tvSample);
        sampleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Util.pixelsToSp(this, originalSize));
        sampleText.setTextColor(originalColor);

        final SeekBar textSizeBar = (SeekBar) layout.findViewById(R.id.seekBarTextSize);
        textSizeBar.setProgress((int) Util.pixelsToSp(this, originalSize));
        textSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sampleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final ColorPicker colorPicker = (ColorPicker) layout.findViewById(R.id.colorPicker);
        final ValueBar valueBar = (ValueBar) layout.findViewById(R.id.svBar);
        colorPicker.addValueBar(valueBar);
        colorPicker.setColor(originalColor);
        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                sampleText.setTextColor(i);
            }
        });

        adBuilder.setTitle(R.string.edit_speech);
        adBuilder.setCancelable(true);
        adBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                View v = panelsFragment.getListView().getChildAt(position -
                        panelsFragment.getListView().getFirstVisiblePosition());
                panelsFragment.updateText(position, eText.getText().toString(),
                        colorPicker.getColor(), textSizeBar.getProgress());

                try {
                    TextView text = (TextView) v.findViewById(R.id.textViewPanel);
                    text.setText(eText.getText().toString());
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeBar.getProgress());
                    text.setTextColor(colorPicker.getColor());

                    panelsFragment.updateView(v, position);
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        });
        adBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog adFinal = adBuilder.create();
        adFinal.show();
    }

    @Override
    public void onImageClickListener(int position) {
        if(!memesDrawerFragment.isDrawerOpen()) {
            selectedPanel = position;
            isAnyPanelSelected = true;
            memesDrawerFragment.setUpDrawerBeforeOpening(panelsFragment.getPanelAt(position));
            memesDrawerFragment.openDrawer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }

    //region Billing Methods
    private void initBilling() {
        mIsPremium = Util.hasPremium(this);

        mHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlTwCnodBfiCDVtfC2/0GbEFk5LfeOHU2AaxPzQs669/72sctWCKBbLmnT6ysJP+WsH0hWc3TdpWdU8R8KSeu2Ouz8Bhp3rjuZWNf8Vdu2Wd4C1fNqcdUgOjaTAP28icrNbAiSz2T18dD8DyrBUdtnLFypQKxNYhl4gO5J3ZQy2dCp+cJ17B2o1u6YzcP74bi8/HlNNTsjSyTnRlz3euHsrBKXOYeukh1NCDyxaCxLL0ikot0r6+J6AfhN5BisqBvnGUOprLo5UlqtgADL9TR4iIJGLVh6eiFpFoRBJgcoKsDBpHuMio5+fKuygIOzn7dsrh9vMZDZcUbpFXtfCTDwQIDAQAB");
        mHelper.enableDebugLogging(false);

        final IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                Log.d(this.getClass().getName(), "Query inventory finished.");

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Is it a failure?
                if (result.isFailure()) {
                    Log.i(this.getClass().getName(), "Failed to query inventory: " + result);
                    return;
                }

                Log.d(this.getClass().getName(), "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

                // Do we have the premium upgrade?
                Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
                mIsPremium = (premiumPurchase != null);
                Util.setHasPremium(MainActivity.this, mIsPremium);
                Log.d(this.getClass().getName(), "User is " + (mIsPremium ?
                        "PREMIUM" : "NOT PREMIUM"));

                Log.d(this.getClass().getName(), "Initial inventory query finished.");
            }
        };


        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if(!result.isSuccess()) {
                    Log.i(this.getClass().getName(), "Ops! Something wrong with the IAB stuff...");
                    return;
                }

                if(mHelper == null) return;

                Log.i(this.getClass().getName(), "IAB set up successful. " +
                        "Now querying for premium...");

                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener =
            new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if(mHelper == null) return;

            if(result.isFailure()) {
                Log.i(this.getClass().getName(), "Failed to purchase premium.");
                return;
            }

            //TODO Google Analytics

            Log.i(this.getClass().getName(), "Purchase successful.");

            mIsPremium = true;
            Util.setHasPremium(MainActivity.this, mIsPremium);

            Toast.makeText(MainActivity.this, R.string.purchase_successful,
                    Toast.LENGTH_LONG).show();
        }
    };
    //endregion
}
