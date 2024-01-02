package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains methods for manipulating and viewing lists of inventory.
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int partId = 0; // used for storing value of last part ID generated
    private static int productId = 999; // used for storing vale of last product ID generated


    /**
     * Adds part to inventory.
     * @param part the part to be added to inventory
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     * Adds product to inventory.
     * @param product the product to be added to inventory
     */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /**
     * Searches for part by part ID.
     * @param partId the part ID to search for
     * @return if part ID matches part in inventory, returns matching part
     */
    public static Part lookupPart(int partId) {
        for(Part part : getAllParts()) {
            if(partId == part.getId()) {
                return part;
            }
        }
        return null;
    }

    /**
     * Searches for product by product ID.
     * @param productId the product ID to search for
     * @return if product ID matches product in inventory, returns matching product
     */
    public static Product lookupProduct(int productId) {
        for(Product product : getAllProducts()) {
            if(productId == product.getId()) {
                return product;
            }
        }
        return null;
    }

    /**
     * Searches for part by part name.
     * @param query part name to search for
     * @return if query fully or partially matches part name in inventory, returns list of matching part(s)
     */
    public static ObservableList<Part> lookupPart(String query) {
        ObservableList<Part> lookupList = FXCollections.observableArrayList();
        ObservableList<Part> allParts = getAllParts();

        for(Part part : allParts) {
            if(part.getName().toLowerCase().contains(query)) {
                lookupList.add(part);
            }
        }
        return lookupList;
    }

    /**
     * Searches for product by product name.
     * @param query product name to search for
     * @return if query fully or partially matches product name in inventory, returns list of matching product(s)
     */
    public static ObservableList<Product> lookupProduct(String query) {
        ObservableList<Product> lookupList = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = getAllProducts();

        for(Product product : allProducts) {
            if(product.getName().toLowerCase().contains(query)) {
                lookupList.add(product);
            }
        }
        return lookupList;
    }

    /**
     * Updates existing part with new information.
     * @param index index of part being updated
     * @param newPart updated part
     */
    public static void updatePart(int index, Part newPart) {
        allParts.set(index, newPart);
    }

    /**
     * Updates existing product with new information.
     * @param index index of product being updated
     * @param newProduct updated product
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes part from inventory.
     * @param selectedPart part to be deleted
     * @return true if part is successfully deleted; false otherwise
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes product from inventory.
     * @param selectedProduct part to be deleted
     * @return true if part is successfully deleted; false otherwise
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return list of all parts in inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return list of all products in inventory
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Generates unique part ID when new part is being created.
     * @return the new part ID
     */
    public static int generatePartId() {
        ++partId;
        return partId;
    }

    /**
     * Generates unique product ID when new product is being created.
     * @return the new product ID
     */
    public static int generateProductId() {
        ++productId;
        return productId;
    }
}