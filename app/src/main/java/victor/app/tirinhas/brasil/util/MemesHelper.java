package victor.app.tirinhas.brasil.util;

import java.util.ArrayList;

import victor.app.tirinhas.brasil.R;
import victor.app.tirinhas.brasil.objects.Meme;

/**
 * Created by victor on 09/08/15.
 */
public class MemesHelper {

    public static ArrayList<Meme> getCharactersByType(ArrayList<Meme> characters, int type) {
        ArrayList<Meme> memes = new ArrayList<Meme>();

        for(int i = 0; i < characters.size(); i++)
            if(characters.get(i).getType() == type)
                memes.add(characters.get(i));

        return memes;
    }
}
