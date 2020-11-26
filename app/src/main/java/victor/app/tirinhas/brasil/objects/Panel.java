package victor.app.tirinhas.brasil.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import victor.app.tirinhas.brasil.R;

/**
 * Created by victor on 11/08/15.
 */
public class Panel {

    private Meme meme;
    private String text;
    private int textColor;
    private float textSize;
    private boolean isLeft;
    private Bitmap panelView;

    public Panel(Meme meme, String text, int textColor, float textSize, boolean left) {
        this.meme = meme;
        this.text = text;
        this.textColor = textColor;
        this.textSize = textSize;
        isLeft = left;
    }

    public Bitmap getPanelView() {
        return panelView;
    }

    public void setPanelView(Bitmap panelView) {
        this.panelView = panelView;
    }

    public Meme getMeme() {
        return meme;
    }

    public void setMeme(Meme meme) {
        this.meme = meme;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }
}
