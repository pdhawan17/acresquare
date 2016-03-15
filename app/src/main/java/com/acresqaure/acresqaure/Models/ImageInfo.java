package com.acresqaure.acresqaure.Models;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by parasdhawan on 05/03/16.
 */
public class ImageInfo {
    private Bitmap bitmap;
    private String filePath="";

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Bitmap getBitmap() {

        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
