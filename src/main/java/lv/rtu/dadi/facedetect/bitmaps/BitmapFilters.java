package lv.rtu.dadi.facedetect.bitmaps;

public class BitmapFilters {

    /**
     * Low frequency.
     */
    public static final short[][] LOWFREQ_3X3_1 =
        {{1, 1, 1},
         {1, 1, 1},
         {1, 1, 1}};

    public static final short[][] LOWFREQ_3X3_2 =
        {{1, 1, 1},
         {1, 2, 1},
         {1, 1, 1}};

    public static final short[][] LOWFREQ_3X3_3 =
        {{1, 2, 1},
         {2, 4, 2},
         {1, 2, 1}};

    /**
     * High frequency.
     */
    public static final short[][] HIFREQ_3X3_1 =
        {{ 0, -1,  0},
         {-1,  5, -1},
         { 0, -1,  0}};

    public static final short[][] HIFREQ_3X3_2 =
        {{-1, -1, -1},
         {-1,  9, -1},
         {-1, -1, -1}};

    public static final short[][] HIFREQ_3X3_3 =
        {{ 0, -2,  0},
         {-2,  5, -2},
         { 0, -2,  0}};

    /*
     * Prewitt and Sobel filters for edge detection.
     */
    public static final short[][] PREWITT_GX =
        {{-1,  0,  1},
         {-1,  0,  1},
         {-1,  0,  1}};
    public static final short[][] PREWITT_GY =
        {{-1, -1, -1},
         { 0,  0,  0},
         { 1,  1,  1}};

    public static final short[][] SOBEL_GX =
        {{-1,  0,  1},
         {-2,  0,  2},
         {-1,  0,  1}};
    public static final short[][] SOBEL_GY =
        {{-1, -2, -1},
         { 0,  0,  0},
         { 1,  2,  1}};
}
