package simulation.cell;

public abstract class Cell {

    protected static final double CELL_SIZE = 0.1;
    protected static final double CELL_AREA = CELL_SIZE * CELL_SIZE;
    protected static final double CELL_VOLUME = CELL_SIZE * CELL_SIZE * CELL_SIZE;

    protected double temperature; //[deg C]

    protected double mass; //[kg]

    /**
     * Ratio of the heat that is added to (or removed from) an object to the resulting temperature change.
     * <br /> Equals: specific heat * mass.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Heat_capacity#Table_of_specific_heat_capacities">
     * Table of heat capacity</a>
     */
    protected double heatCapacity; //[J/K]
    protected double heatConductivity; //[W/(m*K)]

    /**
     * Change of amount of energy in this cell between consecutive simulation 'frames'. This field is being updated
     * by {@link #updateEnergyFlow(Cell, double)} method, and then, after the flow between all cells in the simulation has been
     * accounted for, energyChange is being applied to cell's temperature with {@link #applyEnergyChange()}.
     */
    protected double energyChange;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public abstract int getTypeColor();

    public void updateEnergyFlow(Cell other, double distance) {
        /*
        Formula for energy change:
            dQ / dt = A * dT / (L/2 / k_this + L/2 / k_other)
        Where:
            dQ - Change of heat [J],
            dt - Change of time - const in this model = 0.1[s]
            A  - Area of heat transfer - const in this model = CELLS_SIZE * CELL_SIZE [m^2]
            dT - Difference of temperature [°C / K]
            L  - Distance of heat transfer [m]
            k  - heat transfer coefficient of specific material [W / m^2 / K]
        */
        double dT = temperature - other.temperature;
        double dt = 0.1;

        // Positive dQ means that heat flows: this->other, because of the way dT is calculated.
        double dQ = dt * CELL_AREA * dT / (distance / 2 / this.heatConductivity + distance / 2 / other.heatConductivity);

        this.energyChange -= dQ;
        other.energyChange += dQ;
    }

    public void applyEnergyChange() {
        this.temperature += energyChange / heatCapacity;
        this.energyChange = 0;
    }

    protected static class CellParameters {

        //At 1atm, 20degC
        protected static final double AIR_SPECIFIC_HEAT_CAPACITY = 1005; // [J/(K*kg)]
        protected static final double AIR_DENSITY = 1.2041; // [kg/m^3]
        protected static final double AIR_THERMAL_CONDUCTIVITY = 0.0258; // [W/(m*K)]

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
