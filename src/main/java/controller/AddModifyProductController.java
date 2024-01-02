package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for add/modify products menu. Contains logic for all elements of add/modify products menu.
 */
public class AddModifyProductController implements Initializable {
    @FXML
    private TableColumn<Part, Integer> bottomPartInventoryLevelCol;
    @FXML
    private TableColumn<Part, Integer> bottomPartIdCol;
    @FXML
    private TableColumn<Part, String> bottomPartNameCol;
    @FXML
    private TableView<Part> bottomPartsTableView;
    @FXML
    private TableColumn<Part, Double> bottomPartPriceCol;
    @FXML
    private TextField idField;
    @FXML
    private TextField invField;
    @FXML
    private TextField maxField;
    @FXML
    private TextField minField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Part, Integer> topPartInventoryLevelCol;
    @FXML
    private TableColumn<Part, Integer> topPartIdCol;
    @FXML
    private TableColumn<Part, String> topPartNameCol;
    @FXML
    private TableView<Part> topPartsTableView;
    @FXML
    private TableColumn<Part, Double> topPartPriceCol;
    @FXML
    private Label addModifyProductLabel;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    /**
     * Sets up table views.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topPartsTableView.setItems(Inventory.getAllParts());
        topPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        topPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        topPartInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        topPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        bottomPartsTableView.setItems(associatedParts);
        bottomPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bottomPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        bottomPartInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        bottomPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Saves data entered into form. Checks for missing/mismatched fields and checks logic for min, max, and inv fields. Uses different logic depending on whether product is being created for the first time or is being modified.
     * @param actionEvent save button clicked
     * @throws IOException
     */
    @FXML
    void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            // get data from text fields and check for logic errors
            int inv = -1;
            int max = -1;
            int min = -1;
            double price = -1;
            try {
                inv = Integer.parseInt(invField.getText());
            } catch (NumberFormatException e) {
                generateErrorMessage(2);
            }
            try {
                max = Integer.parseInt(maxField.getText());
                min = Integer.parseInt(minField.getText());
            } catch (NumberFormatException e) {
                generateErrorMessage(4);
            }
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException e) {
                generateErrorMessage(3);
            }
            String name = nameField.getText();
            if (name.isBlank()) {
                generateErrorMessage(1);
            }
            if ((max < min) || max < 0 || min < 0) {
                generateErrorMessage(4);
            }
            if (inv < min || inv > max) {
                generateErrorMessage(6);
                return;
            }
            //determine if add or modify button was clicked and call addProduct or updateProduct accordingly
            if (idField.getText().isBlank()) { // Add product button was clicked
                int id = Inventory.generateProductId();
                Inventory.addProduct(new Product(associatedParts, id, name, price, inv, min, max));
            } else { // Modify product button was clicked
                int id = Integer.parseInt(idField.getText());
                ObservableList<Product> productsList = Inventory.getAllProducts();
                for (int i = 0; i < productsList.size(); i++) {
                    if (productsList.get(i).getId() == id) {
                        Product updatedProduct = new Product(associatedParts, id, name, price, inv, min, max);
                        Inventory.updateProduct(i, updatedProduct);
                    }
                }
            }
            Stage stage = MainMenuController.menuNavigator("MainMenu", actionEvent);
            stage.setTitle("Inventory Application");
        } catch (NumberFormatException e) {
            generateErrorMessage(7);
        }
    }

    /**
     * Returns to main menu without saving data.
     * @param actionEvent cancel button clicked
     * @throws IOException
     */
    @FXML
    void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = MainMenuController.menuNavigator("MainMenu", actionEvent);
        stage.setTitle("Inventory Application");
    }

    /**
     * Adds associated part to bottom table view.
     * @param actionEvent add button clicked
     * @throws IOException
     */
    @FXML
    void onAddButtonClick(ActionEvent actionEvent) throws IOException {
        if (topPartsTableView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        associatedParts.add(topPartsTableView.getSelectionModel().getSelectedItem());
    }

    /**
     * Removes associated part from bottom table view.
     * @param actionEvent remove associated part button clicked
     * @throws IOException
     */
    @FXML
    void onRemoveAssociatedPartButtonClick(ActionEvent actionEvent) throws IOException {
        if (bottomPartsTableView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to remove this associated part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            associatedParts.remove(bottomPartsTableView.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Pre-loads data into form for product being modified. I experienced a bug with this method when first implementing it. Initially, the associated parts list that was being used to modify the product was passed by reference when calling the getAllAssociatedParts() method. This meant that any changes made to the list were saved to the product even if the cancel button was clicked. I resolved this by creating a copy of the list returned from getAllAssociatedProducts() and only passing that copy of the list to the Product constructor after clicking the save button.
     * @param id id of product being modified
     */
    public void modifyProduct(int id) {
        for (Product product : Inventory.getAllProducts()) {
            if (product.getId() == id) { // load data from existing product being modified into text fields and set table views
                idField.setText(Integer.toString(id));
                nameField.setText(product.getName());
                invField.setText(Integer.toString(product.getStock()));
                priceField.setText(Double.toString(product.getPrice()));
                maxField.setText(Integer.toString(product.getMax()));
                minField.setText(Integer.toString(product.getMin()));
                associatedParts = FXCollections.observableArrayList(product.getAllAssociatedParts()); // make copy of list so that values are not modified until save is clicked

                bottomPartsTableView.setItems(associatedParts);
                bottomPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                bottomPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                bottomPartInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
                bottomPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

                addModifyProductLabel.setText("Modify Product");
                break;
            }
        }
    }

    /**
     * Implements part search logic when text is typed into part search field and "Enter" key is pressed.
     * @param actionEvent "Enter" key pressed
     */
    @FXML
    void onPartSearchTextTyped(ActionEvent actionEvent) {
        String query = searchField.getText().toLowerCase(); // convert to lower case so search is not case-sensitive
        ObservableList<Part> searchResults = Inventory.lookupPart(query);
        if (searchResults.size() > 0) { // Match was found. Set TableView to show result.
            topPartsTableView.setItems(searchResults);
        }
        else { // If no matching string was found, text entered may be an integer, so parseInt() is called to attempt to convert text from search field to integer.
            try {
                Part searchPart = Inventory.lookupPart(Integer.parseInt(searchField.getText()));
                if(searchPart == null) { // Searched with integer but no matching partID found.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("No part with the specified Part ID found.");
                    alert.show();
                }
                else  {
                    topPartsTableView.getSelectionModel().select(searchPart); // select row of found part in table view
                }
            }
            catch (NumberFormatException exception) { // Could not find matching string and text entered in search was not an integer.
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No part with the specified Part Name found.");
                alert.show();
            }
        }
    }

    /**
     * Generates error message if check fails on saving product.
     * @param alertNumber corresponds to alert message to be displayed
     */
    private void generateErrorMessage(int alertNumber) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertNumber) {
            case 1:
                alert.setContentText("Name cannot be blank.");
                alert.showAndWait();
                break;
            case 2:
                alert.setContentText("Inv must be an integer.");
                alert.showAndWait();
                break;
            case 3:
                alert.setContentText("Price/Cost must be a number.");
                alert.showAndWait();
                break;
            case 4:
                alert.setContentText("Min and max must be non-negative integers, and min must be less than or equal to max.");
                alert.showAndWait();
                break;
            case 5:
                alert.setContentText("Machine ID must be an integer.");
                alert.showAndWait();
                break;
            case 6:
                alert.setContentText("Inv must be between min and max.");
                alert.showAndWait();
                break;
            case 7:
                alert.setContentText("Please ensure that valid values are entered in all fields.");
                alert.showAndWait();
                break;
        }
    }
}