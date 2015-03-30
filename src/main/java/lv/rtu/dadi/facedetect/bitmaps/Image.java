package lv.rtu.dadi.facedetect.bitmaps;

import java.awt.image.BufferedImage;

public interface Image {

    Image copy();
    BufferedImage toBufferedImage();

    int getWidth();
    int getHeight();
}
