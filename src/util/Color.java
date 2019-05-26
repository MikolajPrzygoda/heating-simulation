package util;

import processing.core.PConstants;
import processing.core.PGraphics;

public class Color {
    public static int fromRGB(int r, int g, int b) {
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public static int fromRGBA(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    //Helper to ease remembering PGraphics method name/parameters
    public static int lerp(int startColor, int endColor, double fraction) {
        return PGraphics.lerpColor(startColor, endColor, (float) fraction, PConstants.RGB);
    }
}
