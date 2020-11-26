package victor.app.tirinhas.brasil.objects;

/**
 * Created by victor on 09/08/15.
 */
public class Meme {

    private String name, id;
    private int type;

    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final int EXTRAS = 2;

    public static final String SMALL_SQUARE = "s"; //90x90
    public static final String BIG_SQUARE = "b"; //160x160
    public static final String SMALL_THUMBNAIL = "t"; //160x160
    public static final String MEDIUM_THUMBNAIL = "m"; //320x320
    public static final String LARGE_THUMBNAIL = "l"; //640x640
    public static final String HUGE_THUMBNAIL = "h"; //1024x1024
    public static final String ORIGINAL_SIZE = "";

    public Meme(String name, String id, int type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL(String size) {
        return "http://i.imgur.com/"+id+size+".png";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
