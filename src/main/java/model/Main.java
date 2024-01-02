package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main method used to launch application. Can be used to preload data into application.
 */
public class Main extends Application {
    /**
     * Creates stage and launches application GUI. Idea for future extended functionality - the program could be modified so that it reads from and saves data to a network database. This would allow for multiple users to update inventory and would provide protection against data loss.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Inventory Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Data to be loaded upon application launch can be added here.
     * @param args
     */
    public static void main(String[] args) {
        /*  Sample data - uncomment code to load

        Outsourced samplePart1 = new Outsourced(997, "Brakes", 15.00, 10, 5, 15, "Brakes-R-Us");
        Inventory.addPart(samplePart1);
        InHouse samplePart2 = new InHouse(998, "Wheel", 11.00, 16, 10, 20, 777);
        Inventory.addPart(samplePart2);
        Outsourced samplePart3 = new Outsourced(999, "Seat", 15.00, 10, 5, 15, "Seats and Things");
        Inventory.addPart(samplePart3);

        ObservableList<Part> sampleProduct1Parts = FXCollections.observableArrayList(Inventory.getAllParts());
        ObservableList<Part> sampleProduct2Parts = FXCollections.observableArrayList(Inventory.getAllParts());
        Product sampleProduct1 = new Product(sampleProduct1Parts, 2000,  "Giant Bike", 299.99, 5, 1, 10);
        Inventory.addProduct(sampleProduct1);
        Product sampleProduct2 = new Product(sampleProduct2Parts, 2001, "Tricycle", 99.99, 5, 1, 10);
        Inventory.addProduct(sampleProduct2);
        sampleProduct2.addAssociatedPart(sampleProduct2.getAllAssociatedParts().get(1));
        */

        launch();
    }
}