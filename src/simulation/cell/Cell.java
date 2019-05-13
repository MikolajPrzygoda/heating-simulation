package simulation.cell;

public abstract class Cell{

    /**
     * Length of cell's edge in [m].
     */
    protected static final double CELL_SIZE = 0.1;
    protected static final double CELL_AREA = CELL_SIZE * CELL_SIZE;
    protected static final double CELL_VOLUME = CELL_SIZE * CELL_SIZE * CELL_SIZE;

    /**
     * Current cell's temperature measured in [°C].
     */
    protected double temperature; //[deg C]

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
     * {@link #applyEnergyChange()}.
     */
    protected double energyChange;

    public double getTemperature(){
        return temperature;
    }

    public void setTemperature(double temperature){
        this.temperature = temperature;
    }

    public abstract int getTypeColor();

    /**
     * Formula for energy change: <br/>
     * dQ / dt = A * dT / (L/2 / k_this + L/2 / k_other) <br/><br/>
     * <p>
     * Where: <br/>
     * dQ - Change of heat [J], <br/>
     * dt - Change of time - const in this model = 0.1[s] <br/>
     * A  - Area of heat transfer - const in this model = CELLS_SIZE * CELL_SIZE [m^2] <br/>
     * dT - Difference of temperature [°C {or} K] <br/>
     * L  - Distance of heat transfer [m] <br/>
     * k  - Heat conductivity of specific material [W / (m * K)] <br/><br/>
     * <p>
     * The above formula is already modified for when we want to consider heat flow through two different materials,
     * so in the case of room heating simulation, we model heat exchange between two cells as flow through half the
     * cell in the first cell's material and through half of the other cell (with different material -> heat conductivity).
     */
    public void updateEnergyFlow(Cell other, double dist, int timeStep){

        double dT = temperature - other.temperature;

        // Positive dQ means that heat flows: this->other, because of the way dT is calculated.
        double dQ = timeStep * CELL_AREA * dT / (dist / 2 / this.heatConductivity + dist / 2 / other.heatConductivity);

        this.energyChange -= dQ;
        other.energyChange += dQ;
    }

    public void applyEnergyChange(){
        this.temperature += energyChange / heatCapacity;
        this.energyChange = 0;
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
