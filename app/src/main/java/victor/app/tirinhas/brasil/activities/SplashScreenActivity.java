package victor.app.tirinhas.brasil.activities;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import victor.app.tirinhas.brasil.GoogleAnalytics.AnalyticsApplication;
import victor.app.tirinhas.brasil.R;
import victor.app.tirinhas.brasil.SplashScreenFragment;


public class SplashScreenActivity extends ActionBarActivity {

    private Tracker t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initAnalytics();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SplashScreenFragment())
                    .commit();
        }
    }

    public void initAnalytics() {
        t = ((AnalyticsApplication) getApplication())
                .getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Tela Principal");
        t.setLanguage(this.getResources().getConfiguration().locale.getCountry());
        t.send(new HitBuilders.AppViewBuilder().build());
    }

}
