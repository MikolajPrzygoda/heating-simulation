package simulation.cell;

import simulation.Simulation;

public abstract class Cell{

    /**
     * Length of cell's edge in [m].
     */
    protected static final double CELL_SIZE = 0.1;
    protected static final double CELL_AREA = CELL_SIZE * CELL_SIZE;
    protected static final double CELL_VOLUME = CELL_SIZE * CELL_SIZE * CELL_SIZE;

    public static final double INITIAL_TEMPERATURE = 12;

    /**
     * Current cell's temperature measured in [°C].
     */
    protected double temperature = INITIAL_TEMPERATURE; //[deg C]

    /**
     * Mass of the material of the cube, calculated based on cube's volume and density, in [kg].
     */
    protected double mass; //[kg]

    /**
     * Ratio of the heat that is added to (or removed from) an object to the resulting temperature change.
     * <br /> Equals: specific heat * mass.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Heat_capacity#Table_of_specific_heat_capacities">
     * Table of heat capacity</a>
     */
    protected double heatCapacity; //[J/K]

    /**
     * The amount of heat energy per second [W] that's gonna flow through 1 meter of material with 1 degree C/K of
     * temperature difference between two sides of said material.
     *
     * @see <a href="https://en.wikipedia.org/wiki/List_of_thermal_conductivities">Table of heat conductivities</a>
     */
    protected double heatConductivity; //[W/(m*K)]

    /**
     * Change of amount of energy in this cell between consecutive simulation 'frames'. This field is being updated
     * by {@link #updateEnergyFlow(Cell, double, int)} method, and then, after the flow between all cells in the
     * simulation has been accounted for, energyChange is being applied to cell's temperature with
     * {@link #applyEnergyChange(double)}.
     */
    protected double energyChange;

    /**
     * Calculations used to simulate air convection are done on temperature rather than energy (in [°C] instead of [J]),
     * so there's different field for tracking that change.
     */
    protected double temperatureChange;

    public double getTemperature(){
        return temperature;
    }

    public void setTemperature(double temperature){
        this.temperature = temperature;
    }

    public double getTemperatureChange(){
        return temperatureChange;
    }

    public abstract int getTypeColor();

    /**
     * Formula for energy change: <br/>
     * dQ / dt = A * dT / (L/2 / k_this + L/2 / k_other) <br/><br/>
     *
     * Where: <br/>
     * dQ - Change of heat [J], <br/>
     * dt - Change of time - const in this model = 0.1[s] <br/>
     * A  - Area of heat transfer - const in this model = CELLS_SIZE * CELL_SIZE [m^2] <br/>
     * dT - Difference of temperature [°C {or} K] <br/>
     * L  - Distance of heat transfer [m] <br/>
     * k  - Heat conductivity of specific material [W / (m * K)] <br/><br/>
     *
     * The above formula is already modified for when we want to consider heat flow through two different materials,
     * so in the case of room heating simulation, we model heat exchange between two cells as flow through half the
     * cell in the first cell's material and through half of the other cell (with different material -> heat conductivity).
     */
    public void updateEnergyFlow(Cell other, double dist, int timeStep){

        double dT = temperature - other.temperature;

        // Positive dQ means that heat flows: this->other, because of the way dT is calculated.
        double dQ = timeStep * CELL_AREA * dT / (dist / 2 / this.heatConductivity + dist / 2 / other.heatConductivity);

        // If it's a transfer between heater and air, boost it :')
        if(this instanceof HeaterCell && other instanceof AirCell || this instanceof AirCell && other instanceof HeaterCell){
            dQ *= 50;
        }

        this.energyChange -= dQ;
        other.energyChange += dQ;
    }

    /**
     * Function used to calculate the amount of heat given to other cells in convective heat transfer. <br />
     * More information here: {@link Simulation#update()} <i>(point 2)</i>
     *
     * @param other    Cell to which try to give up heat to
     * @param ratio    Ratio of the temperature difference to send to the other cell
     * @param dist     Distance between cells
     * @param timeStep Amount of seconds between two simulation states.
     */
    public void updateConvectiveEnergyFlow(Cell other, double ratio, double dist, int timeStep){
        if(this instanceof AirCell && other instanceof AirCell){
            double temperatureDiff = this.temperature - other.temperature;

            // Allow only giving heat to the other cell.
            if(temperatureDiff > 0){
                // 20 - maximum timeStep (NOTE: should reconsider this).
                double dT = (temperatureDiff * ratio * (timeStep / 20f)) / dist;

                this.temperatureChange -= dT;
                other.temperatureChange += dT;
            }
        }
    }

    public void applyEnergyChange(double timeStep){
        this.temperature += energyChange / heatCapacity;
        this.energyChange = 0;
    }

    public void applyTemperatureChange(){
        this.temperature += temperatureChange;
        this.temperatureChange = 0;
    }


    protected static class CellParameters{

        //At 1atm, 20degC
        protected static final double AIR_SPECIFIC_HEAT_CAPACITY = 1005;    // [J/(K*kg)]
        protected static final double AIR_DENSITY = 1.2041;                 // [kg/m^3]
        protected static final double AIR_THERMAL_CONDUCTIVITY = 0.0258;    // [W/(m*K)]

        //Dry soil
        protected static final double DIRT_SPECIFIC_HEAT_CAPACITY = 800;
        protected static final double DIRT_DENSITY = 1220;
        protected static final double DIRT_THERMAL_CONDUCTIVITY = 0.364;

        //Parameters for cast iron
        protected static final double HEATER_SPECIFIC_HEAT_CAPACITY = 460;
        protected static final double HEATER_DENSITY = 7200;
        protected static final double HEATER_THERMAL_CONDUCTIVITY = 52;

        //http://www.certyfikat-energetyczny.powiat.pl/CE_P/wspolczynniki_przewodzenia.html
        protected static final double WALL_SPECIFIC_HEAT_CAPACITY = 840;
        protected static final double WALL_DENSITY = 700;
        protected static final double WALL_THERMAL_CONDUCTIVITY = 0.35;

        //Glass - crown
        protected static final double WINDOW_SPECIFIC_HEAT_CAPACITY = 670;
        protected static final double WINDOW_DENSITY = 2.5;
        protected static final double WINDOW_THERMAL_CONDUCTIVITY = 0.96;
    }
}
