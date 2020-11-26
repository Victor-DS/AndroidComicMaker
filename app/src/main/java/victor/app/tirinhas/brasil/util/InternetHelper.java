package victor.app.tirinhas.brasil.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by victor on 15/08/15.
 */
public class InternetHelper {

    public static final String DEFAULT_CHARACTERS = "https://api.imgur.com/3/album/6OaKj";
    public static final String EXTRAS = "";
    private static final String SECRET = "e89ed1595d783ecbc708bb56f8392ef5eccdf155";

    public static String getJSON(String URL) throws IOException {
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpClient httpclient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new HttpGet(URL);
        httpGet.addHeader("Authorization", "Client-ID 5350c8a04fb819a");
        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
}
