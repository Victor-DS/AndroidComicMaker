package victor.app.tirinhas.brasil.util;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import victor.app.tirinhas.brasil.R;
import victor.app.tirinhas.brasil.objects.Meme;

/**
 * Created by victor on 09/08/15.
 */
public class Util {

    private static final String SHARED_PREFERENCES_NAME = "Comic Maker Preferences";

    public static void aboutDialog(Context c) {
        generateDialog(c, R.string.about_title, R.string.about_message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
    }

    public static void buyPremiumDialog(Context c) {
        generateDialog(c, R.string.buy_premium_title, R.string.buy_premium_message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
    }

    public static void goToOtherApps(Context c) throws ActivityNotFoundException {
        launchGooglePlay(c, "market://search?q=pub:Willy+Wonka");
    }

    public static void rateThisApp(Context c) throws ActivityNotFoundException {
        launchGooglePlay(c, "market://details?id=" + c.getPackageName());
    }

    private static void launchGooglePlay(Context c, String link)
            throws ActivityNotFoundException {
        c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    private static void generateDialog(Context c, int title, int message,
                                       DialogInterface.OnClickListener okButton) {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(c);
        adBuilder.setMessage(message);
        adBuilder.setTitle(title);
        adBuilder.setCancelable(true);
        adBuilder.setPositiveButton(android.R.string.ok, okButton);
        AlertDialog adFinal = adBuilder.create();
        adFinal.show();
    }

    public static boolean hasInternetConnection(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /** SHARED PREFERENCES **/

    public static void saveJSON(Context c, String JSON) {
        c.getSharedPreferences(SHARED_PREFERENCES_NAME, c.MODE_PRIVATE).edit()
                .putString("LAST_SAVED_JSON", JSON).commit();
    }

    public static String getJSON(Context c) throws NullPointerException {
        String json = c.getSharedPreferences(SHARED_PREFERENCES_NAME, c.MODE_PRIVATE)
                .getString("LAST_SAVED_JSON", null);

        if(json == null)
            throw new NullPointerException("No JSON on SharedPreferences!");

        return json;
    }

    public static boolean hasPremium(Context c) {
        return c.getSharedPreferences(SHARED_PREFERENCES_NAME, c.MODE_PRIVATE)
                .getBoolean("premium", false);
    }

    public static void setHasPremium(Context c, boolean premium) {
        c.getSharedPreferences(SHARED_PREFERENCES_NAME, c.MODE_PRIVATE).edit()
                .putBoolean("premium", premium).commit();

    }

    public static boolean shouldDownloadJSONAgain(Context c) {
        Calendar timeToUpdate = Calendar.getInstance();

        long lastUpdatedLong = c.getSharedPreferences(SHARED_PREFERENCES_NAME, c.MODE_PRIVATE)
                .getLong("lastTimeUpdated", 0);

        timeToUpdate.setTimeInMillis(lastUpdatedLong);
        timeToUpdate.add(Calendar.DAY_OF_YEAR, 7);

        if(Calendar.getInstance().before(timeToUpdate)) return false;

        return true;
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    public static Bitmap getBitmapFromView(View v) {
        v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        v.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }
}
