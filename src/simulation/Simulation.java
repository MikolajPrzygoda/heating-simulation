package simulation;

import simulation.cell.Cell;
import util.int3d;

public class Simulation {

    public static final double TIME_STEP = 0.1;

    private Cell[][][] room;
    private double[][][] nextRoomValues;
    private int depth;
    private int height;
    private int width;


    public Simulation(RoomPlan roomPlan) {

        this.depth = roomPlan.getRoomDepth();
        this.height = roomPlan.getRoomHeight();
        this.width = roomPlan.getRoomWidth();

        // Create room arrays
        room = new Cell[depth][height][width];
        nextRoomValues = new double[depth][height][width];
        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    room[z][y][x] = roomPlan.getCellAt(new int3d(z,y,x));
                }
            }
        }
    }

    public Cell getCell(int z, int y, int x){
        return room[z][y][x];
    }

    public int getDepth() {
        return depth;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public void update(){
        // Move simulation forward
        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    nextRoomValues[z][y][x] = room[z][y][x].getTemperature();
                }
            }
        }


        // Swap values
        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    room[z][y][x].setTemperature(nextRoomValues[z][y][x]);
                }
            }
        }
    }

    public float getMinValue(){
        return 0;
    }

    public float getMaxValue(){
        return 1;
    }
}
