package simulation;

import simulation.cell.Cell;
import util.int3d;

public class Simulation{

    public static final double TIME_STEP = 0.1;

    private Cell[][][] room;
    private int depth;
    private int height;
    private int width;


    public Simulation(RoomPlan roomPlan){

        this.depth = roomPlan.getRoomDepth();
        this.height = roomPlan.getRoomHeight();
        this.width = roomPlan.getRoomWidth();

        // Create room arrays
        room = new Cell[depth][height][width];
        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    room[z][y][x] = roomPlan.getCellAt(new int3d(z, y, x));
                }
            }
        }
    }

    public Cell getCell(int z, int y, int x){
        return room[z][y][x];
    }

    public int getDepth(){
        return depth;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public void update(){
        /*
        Calculate heat flow between cells.

        The method for that is called only once between two cells, and 'away' from simulation's origin.
        The last page/column/row isn't being considered as it is not needed, the outside air cell has constant (or at
        least not depending on the heat coming out of the room cells) temperature so the amount of heat flow
        is irrelevant.

        Second parameter is the distance between two cells, so for cells:
         - sharing a side -> sqrt(1)
         - sharing an edge -> sqrt(2)
         - sharing a point -> sqrt(3)
        */
        for(int z = 0; z < depth - 1; z++){
            for(int y = 0; y < height - 1; y++){
                for(int x = 0; x < width - 1; x++){
                    room[z][y][x].updateEnergyFlow(room[z + 1][y][x], 1);
                    room[z][y][x].updateEnergyFlow(room[z][y + 1][x], 1);
                    room[z][y][x].updateEnergyFlow(room[z][y][x + 1], 1);

                    room[z][y][x].updateEnergyFlow(room[z + 1][y + 1][x], 1.414);
                    room[z][y][x].updateEnergyFlow(room[z + 1][y][x + 1], 1.414);
                    room[z][y][x].updateEnergyFlow(room[z][y + 1][x + 1], 1.414);

                    room[z][y][x].updateEnergyFlow(room[z + 1][y + 1][x + 1], 1.732);
                }
            }
        }

        // Apply changes
        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    room[z][y][x].applyEnergyChange();
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
