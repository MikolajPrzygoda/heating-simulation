package util;

public class Color {
    public static int fromRGB(int r, int g, int b) {
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public static int fromARGB(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }
}
