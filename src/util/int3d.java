package util;

public class int3d {

    public int x = 0;
    public int y = 0;
    public int z = 0;

    public int3d() {
    }

    public int3d(int z, int y, int x) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(int3d point){
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }

    public boolean isBetween(int3d start, int3d end) {
        return start.x <= x && x <= end.x &&
                start.y <= y && y <= end.y &&
                start.z <= z && z <= end.z;
    }

    public static boolean isBetween(int3d point, int3d start, int3d end) {
        return start.x <= point.x && point.x <= end.x &&
                start.y <= point.y && point.y <= end.y &&
                start.z <= point.z && point.z <= end.z;
    }

}
