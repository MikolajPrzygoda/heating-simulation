package simulation;

import simulation.cell.Cell;
import simulation.cell.HeaterCell;
import util.int3d;

import java.util.ArrayList;
import java.util.List;

public class Simulation{

    private Cell[][][] room;
    private int depth;
    private int height;
    private int width;
    private int timeStep = 5;
    private int elapsedTime = 0;
    private List<HeaterCell> heaterCells = new ArrayList<>();

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

                    if(room[z][y][x] instanceof HeaterCell){
                        heaterCells.add((HeaterCell) room[z][y][x]);
                    }
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

    public int getTimeStep(){
        return timeStep;
    }

    public void setTimeStep(int timeStep){
        this.timeStep = timeStep;
    }

    public int getElapsedTime(){
        return elapsedTime;
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
                    room[z][y][x].updateEnergyFlow(room[z + 1][y][x], 1, timeStep);
                    room[z][y][x].updateEnergyFlow(room[z][y + 1][x], 1, timeStep);
                    room[z][y][x].updateEnergyFlow(room[z][y][x + 1], 1, timeStep);

                    room[z][y][x].updateEnergyFlow(room[z + 1][y + 1][x], 1.414, timeStep);
                    room[z][y][x].updateEnergyFlow(room[z + 1][y][x + 1], 1.414, timeStep);
                    room[z][y][x].updateEnergyFlow(room[z][y + 1][x + 1], 1.414, timeStep);

                    room[z][y][x].updateEnergyFlow(room[z + 1][y + 1][x + 1], 1.732, timeStep);
                }
            }
        }

        //Apply changes
        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    room[z][y][x].applyEnergyChange(timeStep);
                }
            }
        }

        //Increase time elapsed since the start of simulation
        elapsedTime += timeStep;
    }

    public double getMinValue(){
//        double result = Double.POSITIVE_INFINITY;
//
//        for(int z = 0; z < depth; z++){
//            for(int y = 0; y < height; y++){
//                for(int x = 0; x < width; x++){
//                    if(room[z][y][x].getTemperature() < result)
//                        result = room[z][y][x].getTemperature();
//                }
//            }
//        }
//
//        if(result < 0 || result > 1000){
//            System.out.println("WUT " + result);
//        }
//
//        return result;

        return 0;
    }

    public double getMaxValue(){
//        double result = Double.NEGATIVE_INFINITY;
//
//        for(int z = 0; z < depth; z++){
//            for(int y = 0; y < height; y++){
//                for(int x = 0; x < width; x++){
//                    if(room[z][y][x].getTemperature() > result)
//                        result = room[z][y][x].getTemperature();
//                }
//            }
//        }
//
//        if(result < 0 || result > 1000){
//            System.out.println("WUT " + result);
//        }
//
//        return result;

        return 30;
    }

    public void changeHeaterPower(double heaterPower){
        double cellsPower = heaterPower / heaterCells.size();
        heaterCells.forEach(cell -> cell.setPowerOutput(cellsPower));
    }
}
