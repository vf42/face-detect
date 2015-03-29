package lv.rtu.dadi.facedetect.bitmaps;

import java.awt.image.BufferedImage;

public interface Bitmap {

    Bitmap copy();
    BufferedImage toBufferedImage();

    int getWidth();
    int getHeight();
}
