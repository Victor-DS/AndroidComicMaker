package victor.app.tirinhas.brasil.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import victor.app.tirinhas.brasil.objects.Meme;

/**
 * Created by victor on 15/08/15.
 */
public class JSONParser {

    public static ArrayList<Meme> getCharacters(String json) throws JSONException {
        Log.i("JSONParser", "Starting to parse JSON: " + json);
        ArrayList<Meme> characters = new ArrayList<Meme>();

        JSONArray jArray = new JSONObject(json).getJSONObject("data").getJSONArray("images");

        for(int i = 0; i < jArray.length(); i++) {
            String name = jArray.getJSONObject(i).getString("title").equals("null") ? "Default" :
                    jArray.getJSONObject(i).getString("title");
            String id = jArray.getJSONObject(i).getString("id");
            int type = jArray.getJSONObject(i).getString("description").equals("null") ? 0 :
                    Integer.parseInt(jArray.getJSONObject(i).getString("description"));
            Log.i("JSONParser", "Characters added: " + name + ", " + id + ", type: " + type);
            characters.add(new Meme(name,id, type));
        }

        Log.i("JSONParser", "JSONParsed. Result: " + characters.size() + " items.");
        return characters;
    }
}
