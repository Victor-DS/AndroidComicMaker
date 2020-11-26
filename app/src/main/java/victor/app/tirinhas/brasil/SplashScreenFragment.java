package victor.app.tirinhas.brasil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;

import victor.app.tirinhas.brasil.activities.MainActivity;
import victor.app.tirinhas.brasil.util.InternetHelper;
import victor.app.tirinhas.brasil.util.Util;

/**
 * Created by victor on 15/08/15.
 */
public class SplashScreenFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Util.hasInternetConnection(getActivity())) {
            Toast.makeText(getActivity(),
                    R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        } else new DownloadJSON().execute(InternetHelper.DEFAULT_CHARACTERS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        return rootView;
    }

    private class DownloadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                if(Util.shouldDownloadJSONAgain(getActivity()))
                    return InternetHelper.getJSON(params[0]);

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return Util.getJSON(getActivity());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("SplashScreenFragment", "Error downloading JSON");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            if (json==null) {
                Toast.makeText(getActivity(), R.string.error_downloading, Toast.LENGTH_LONG).show();
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            }

            Util.saveJSON(getActivity(), json);
            Log.i("SplashScreen", json);
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class)
                    .putExtra("Main characters JSON", json));
        }
    }

}
