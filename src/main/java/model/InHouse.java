package model;

/**
 * Used to create, set, and view attributes of in house parts.
 */
public class InHouse extends Part {
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }


    /**
     * @param machineId machine ID to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * @return machine ID of specified part
     */
    public int getMachineId() {
        return machineId;
    }
}
