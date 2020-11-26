package victor.app.tirinhas.brasil.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import victor.app.tirinhas.brasil.R;
import victor.app.tirinhas.brasil.objects.Panel;

/**
 * Created by victor on 17/08/15.
 */
public class GeneratorHelper {

    private final String DRAFT_FOLDER = "COMICS_DRAFT";

    private String PREFIX, FOLDER, lastImageStringPath;
    private Context mContext;
    private ArrayList<String> drafts;
    private Uri lastImageUri;

    public GeneratorHelper(Context context) {
        mContext = context;
        FOLDER = mContext.getString(R.string.folder);
        PREFIX = mContext.getString(R.string.prefix);
        drafts = new ArrayList<String>();
    }

    public String getLastImage() {
        return lastImageStringPath;
    }

    private String generateFileName() {
        Random generator = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
        String currentDateAndTime = sdf.format(new Date());
        return PREFIX + currentDateAndTime + "_" + generator.nextInt(1000000000) +".jpg";
    }

    public void saveImage(ArrayList<Panel> list) {
        new AsynchronousSaving(mContext, list).execute();
    }

    /**
     *  This method generates a single panel image and returns its complete path
     *
     * @param layout = Layout do painel (cada item da lista)
     * @param name = Nome da imagem para salvar. (Dispensável já que é rascunho?)
     * @return String do "path" da imagem. Preciso dela depois para gerar a imagem final
     * @throws IOException
     * @throws FileNotFoundException
     */
    private String savePanel(Bitmap layout, final String name)
            throws IOException, FileNotFoundException {
        File arquivo = new File(Environment
                .getExternalStorageDirectory().getPath()+"/"+ DRAFT_FOLDER +"/");

        if(!arquivo.exists())
            arquivo.mkdirs();

        File file = new File (arquivo, name);

//        layout.setDrawingCacheEnabled(true);
        Bitmap screenshot;
//        screenshot = Bitmap.createBitmap(layout.getDrawingCache());
        screenshot = layout;
        Canvas canvas = new Canvas(screenshot);
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        canvas.drawBitmap(screenshot, 0, 0, null);
        screenshot.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();
//        layout.setDrawingCacheEnabled(false);

        return Environment.getExternalStorageDirectory().getPath() +
                "/" + DRAFT_FOLDER + "/" + name;
    }

    private Uri saveFinalImage(Bitmap finalBitmap, final String filename)
            throws FileNotFoundException, IOException {
        File arquivo = new File(Environment
                .getExternalStorageDirectory().getPath()+"/"+ FOLDER +"/");

        if(!arquivo.exists())
            arquivo.mkdir();

        File file = new File(arquivo, filename);
        FileOutputStream outputStream = new FileOutputStream(file);
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();

        lastImageUri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() +
                "/" + FOLDER + "/" + filename);
        lastImageStringPath = Environment.getExternalStorageDirectory().getPath() +
                "/" + FOLDER + "/" + filename;

        return lastImageUri;
    }

    private Uri save(ArrayList<Panel> list)
            throws FileNotFoundException, IOException {
        for(int i = 0; i < list.size(); i++)
            if(list.get(i).getPanelView() != null)
                drafts.add(savePanel(list.get(i).getPanelView(), "temp_0" + i + ".jpg"));

        int finalImageHeight = 0;
        int finalImageWidth = BitmapFactory.decodeFile(drafts.get(0)).getWidth();

        for(String s : drafts)
            finalImageHeight += BitmapFactory.decodeFile(s).getHeight();

        Bitmap finalBitmap = Bitmap.createBitmap(finalImageWidth,
                finalImageHeight, Bitmap.Config.ARGB_8888);

        Canvas finalImage = new Canvas(finalBitmap);

        int currentDrawingHeightPoint = 0;

        while(!drafts.isEmpty()) {
            String currentDraftPath = drafts.remove(0);

            Bitmap currentBitmap = BitmapFactory.decodeFile(currentDraftPath);
            finalImage.drawBitmap(currentBitmap, 0.0f,
                    currentDrawingHeightPoint, null);

            currentDrawingHeightPoint += currentBitmap.getHeight();

            deleteDraftPanel(currentDraftPath);
        }

        deleteDraftFolder();

        return saveFinalImage(finalBitmap, generateFileName());
    }

    private void deleteDraftPanel(String filePath) {
        File draft = new File(filePath);

        if(draft.exists())
            draft.delete();
    }

    private void deleteDraftFolder() {
        File folder = new File(Environment
                .getExternalStorageDirectory().getPath()+"/"+ DRAFT_FOLDER +"/");

        if(folder.exists())
            folder.delete();
    }

    public class AsynchronousSaving extends AsyncTask<Void, Void, Uri> {

        private ArrayList<Panel> list;
        private Context mContext;
        private GeneratorHelper generator;

        public AsynchronousSaving(Context c, ArrayList<Panel> listPanel) {
            list = listPanel;
            mContext = c;
            generator = new GeneratorHelper(c);
        }

        @Override
        protected Uri doInBackground(Void... params) {
            try {
                return save(list);
            } catch(FileNotFoundException e) {
                e.printStackTrace();

//                if(!drafts.isEmpty())
//                    while(!drafts.isEmpty())
//                        deleteDraftPanel(drafts.remove(0));
//                deleteDraftFolder();

                return null;
            } catch(IOException e) {
                e.printStackTrace();

//                if(!drafts.isEmpty())
//                    while(!drafts.isEmpty())
//                        deleteDraftPanel(drafts.remove(0));
//                deleteDraftFolder();

                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(mContext, R.string.saving, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Uri imageUri) {
            super.onPostExecute(imageUri);
            if(imageUri != null) {
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        lastImageUri));
                Toast.makeText(mContext, R.string.saved_on_gallery, Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(mContext, R.string.failed_to_save, Toast.LENGTH_LONG).show();
        }
    }
}
