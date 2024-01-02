package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains methods for viewing and modifying products.
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(ObservableList<Part> associatedParts, int id, String name, double price, int stock, int min, int max) {
        this.associatedParts = associatedParts;
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    /**
     * @param id product id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name product name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param price product price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param stock amount of stock/inventory to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @param min minimum amount of stock/inventory to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param max amount of stock/inventory to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the product id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the amount of product stock/inventory
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return the minimum amount of product stock/inventory
     */
    public int getMin() {
        return min;
    }

    /**
     * @return the maximum amount of product stock/inventory
     */
    public int getMax() {
        return max;
    }

    /**
     * @param part the associated part to be added
     */
    public void addAssociatedPart(Part part){
        associatedParts = getAllAssociatedParts();
        associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart the associated part to be deleted
     * @return true if associated product was successfully deleted; false otherwise
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts = getAllAssociatedParts();
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return list of parts associated with product
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
