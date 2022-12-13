package com.example.image_editor;

import android.content.Context;

public class SliderItem {
    private Context context;
    private int images;

    public SliderItem(Context context, int images) {
        this.context = context;
        this.images = images;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }
}
