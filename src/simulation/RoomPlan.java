package simulation;

import simulation.cell.*;
import util.int3d;

public class RoomPlan {

    private RoomPlan() {
    }

    private int3d roomDimms = new int3d(40, 20, 40);

    private int wallThickness = 3;
    private int3d windowStart = new int3d(36, 10, 15);
    private int3d windowEnd = new int3d(38, 14, 25);
    private int3d heaterStart = new int3d(5, 5, 5);
    private int3d heaterEnd = new int3d(7, 7, 7);

    public int getRoomDepth() {
        return roomDimms.z;
    }

    public int getRoomHeight() {
        return roomDimms.y;
    }

    public int getRoomWidth() {
        return roomDimms.x;
    }

    public Cell getCellAt(int3d point) {
        if (point.y == roomDimms.y - 1) { //Bottom row of a simulation
            return new DirtCell();
        }
        if (point.isBetween(windowStart, windowEnd)) {
            return new WindowCell();
        } else if (point.isBetween(heaterStart, heaterEnd))
            return new HeaterCell();
        else {
            // If this cell isn't a dirt, window nor heater cell, then it only depends on the distance to the closest
            // simulation wall, so -> calculate the distance -> if it's:
            //    == 0 -> OUTSIDE
            //    [1;wallThickness] -> WALL
            //    > wallThickness -> AIR
            int min = Integer.MAX_VALUE;
            min = Integer.min(min, point.x);
            min = Integer.min(min, (roomDimms.x - 1) - point.x);
            min = Integer.min(min, point.y);
            min = Integer.min(min, (roomDimms.y - 1) - point.y);
            min = Integer.min(min, point.z);
            min = Integer.min(min, (roomDimms.z - 1) - point.z);
            if (min == 0)
                return new OutsideCell();
            else if (min > wallThickness)
                return new AirCell();
            else
                return new WallCell();

        }
    }


    public static class Factory {
        private RoomPlan instance;

        public Factory() {
            instance = new RoomPlan();
        }

        public Factory roomDimms(int3d dimms) {
            instance.roomDimms.set(dimms);
            return this;
        }

        public Factory wallThickness(int val) {
            instance.wallThickness = val;
            return this;
        }

        public Factory windowStart(int3d point) {
            instance.windowStart.set(point);
            return this;
        }

        public Factory windowEnd(int3d end) {
            instance.windowEnd.set(end);
            return this;
        }

        public Factory heaterStart(int3d point) {
            instance.heaterStart.set(point);
            return this;
        }

        public Factory heaterEnd(int3d end) {
            instance.heaterEnd.set(end);
            return this;
        }

        public RoomPlan build() {
            return instance;
        }

    }
}
