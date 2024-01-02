package model;

/**
 * Used to create, set, and view attributes of outsourced parts.
 */
public class Outsourced extends Part{
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }


    /**
     * @param companyName company name to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return company name of specified part
     */
    public String getCompanyName() {
        return companyName;
    }
}
