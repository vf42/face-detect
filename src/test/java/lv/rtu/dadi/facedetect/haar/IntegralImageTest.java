package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class IntegralImageTest {

    private static double[][] pixels = new double[][] {
        { 1,  2,  3,  4},
        { 5,  6,  7,  8},
        { 9, 10, 11, 12},
        {13, 14, 15, 16}};
    private static GrayscaleImage img = new GrayscaleImage(pixels);

    private static double[][] iiValues = new double[][] {
        { 1,  3,  6, 10},
        { 6, 14, 24, 36},
        {15, 33, 54, 78},
        {28, 60, 96,136}};
    private static double[][] ii2Values = new double[][] {
        { 1,  5, 14, 30},
        {26, 66,124,204},
       {107,247,426,650},
       {276,612,1016,1496}};


    @Test
    public void testII() {
        final IntegralImage ii = new IntegralImage(img);
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                Assert.assertTrue(ii.values[i][j] == iiValues[i][j] * 255);
            }
        }
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                Assert.assertTrue(ii.sqValues[i][j] == ii2Values[i][j] * 255 * 255);
            }
        }
    }

    @Test
    public void testJVJII() {
        final int width = pixels.length;
        final int height = pixels[0].length;
        final int[][] grayImage=new int[width][height];
        final int[][] img = new int[width][height];
        final int[][] squares=new int[width][height];
        // Copy-pasted code from jViolaJones.
        for(int i=0;i<width;i++)
        {
                int col=0;
                int col2=0;
                for(int j=0;j<height;j++)
                {
                        final int value=(int)pixels[i][j]*255;
                        img[i][j]=value;
                        grayImage[i][j]=(i>0?grayImage[i-1][j]:0)+col+value;
                        squares[i][j]=(i>0?squares[i-1][j]:0)+col2+value*value;
                        col+=value;
                        col2+=value*value;
                }
        }

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                Assert.assertTrue(grayImage[i][j] == (int) (iiValues[i][j] * 255));
            }
        }
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                Assert.assertTrue(squares[i][j] == (int) (ii2Values[i][j] * 255 * 255));
            }
        }
    }
}
