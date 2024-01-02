package controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for main menu. Contains logic for all elements of application's main menu.
 */
public class MainMenuController implements Initializable {
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, Integer> partInventoryLevelCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    private TextField partsSearchField;
    @FXML
    private TableView<Part> partsTableView;
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    @FXML
    private TableColumn<Product, Integer> productInventoryLevelCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Double> productPriceCol;
    @FXML
    private TextField productSearchField;
    @FXML
    private TableView<Product> productsTableView;


    /**
     * Sets up part and product table views.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { // set inventory table views
        partsTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTableView.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Associates menu name with resource path of FXML document pertaining to menu.
     * @param menuName menu name to be located
     * @return resource path of specified menu's FXML document
     */
    public static String resourceLocator(String menuName) {
        //Create hash map to associate menu name parameter with resource path.
        Map<String, String> resourcePath = new HashMap<String, String>();
        resourcePath.put("MainMenu", "/view/MainMenu.fxml");
        resourcePath.put("AddModifyPart", "/view/AddModifyPart.fxml");
        resourcePath.put("AddModifyProduct", "/view/AddModifyProduct.fxml");
        return resourcePath.get(menuName);
    }

    /**
     * Gets stage and sets scene for specified menu to be navigated to.
     * @param menuName name of menu to be loaded
     * @param actionEvent button pressed
     * @return stage so that it can be used by the calling method to set stage title to appropriate value
     * @throws IOException
     */
    public static Stage menuNavigator(String menuName, ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(MainMenuController.class.getResource(resourceLocator(menuName)));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        return stage;
    }

    /**
     * Overloaded method used when data needs to be passed from one scene to another.
     * @param menuName name of menu to be loaded
     * @param actionEvent button pressed
     * @param id id of part or product to be modified
     * @return stage so that it can be used by the calling method to set stage title to appropriate value
     * @throws IOException
     */
    public static Stage menuNavigator(String menuName, ActionEvent actionEvent, int id) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainMenuController.class.getResource(resourceLocator(menuName)));
        Parent root = loader.load();
        // determine whether to pass data to modifyPart() or modifyProduct()
        if(menuName.equals("AddModifyPart")) {
            AddModifyPartController controller = loader.getController();
            controller.modifyPart(id);
        }
        else if(menuName.equals("AddModifyProduct")) {
            AddModifyProductController controller = loader.getController();
            controller.modifyProduct(id);
        }
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        return stage;
    }

    /**
     * Called when add button under parts table view is clicked.
     * @param actionEvent parts add button clicked
     * @throws IOException
     */
    @FXML
    void onPartsAddButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = menuNavigator("AddModifyPart", actionEvent);
        stage.setTitle("Add Part");
    }

    /**
     * Called when modify button under parts table view is clicked. Gets ID of part that was selected when button was clicked.
     * @param actionEvent parts modify button clicked
     * @throws IOException
     */
    @FXML
    void onPartsModifyButtonClick(ActionEvent actionEvent) throws IOException {
        if(partsTableView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        int id = partsTableView.getSelectionModel().getSelectedItem().getId();
        Stage stage = menuNavigator("AddModifyPart", actionEvent, id);
        stage.setTitle("Modify Part");
    }

    /**
     * Called when delete button under parts table view is clicked. Gets ID of part that was selected when button was clicked.
     * @param actionEvent parts delete button clicked
     */
    @FXML
    void onPartsDeleteButtonClick(ActionEvent actionEvent) {
        if (partsTableView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Part part = partsTableView.getSelectionModel().getSelectedItem();
            Inventory.deletePart(part);
        }
    }

    /**
     * Called when add button under products table view is clicked. Gets ID of product that was selected when button was clicked.
     * @param actionEvent products add button clicked
     * @throws IOException
     */
    @FXML
    void onProductsAddButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = menuNavigator("AddModifyProduct", actionEvent);
        stage.setTitle("Add Product");
    }

    /**
     * Called when modify button under products table view is clicked. Gets ID of product that was selected when button was clicked.
     * @param actionEvent products modify button clicked
     * @throws IOException
     */
    @FXML
    void onProductsModifyButtonClick(ActionEvent actionEvent) throws IOException {
        if(productsTableView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        int id = productsTableView.getSelectionModel().getSelectedItem().getId();
        Stage stage = menuNavigator("AddModifyProduct", actionEvent, id);
        stage.setTitle("Modify Product");
        }

    /**
     * Called when delete button under parts table view is clicked. Gets ID of product that was selected when button was clicked.
     * @param actionEvent products delete button was clicked
     */
    @FXML
    void onProductsDeleteButtonClick(ActionEvent actionEvent) {
        if (productsTableView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        if(!productsTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()) { // determine if product has parts associated with it
            Alert alert = new Alert(Alert.AlertType.ERROR, "All associated parts must be removed before product can be deleted.");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this product?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Product product = productsTableView.getSelectionModel().getSelectedItem();
            Inventory.deleteProduct(product);
        }
    }

    /**
     * Exits program.
     * @param actionEvent exit button clicked
     */
    @FXML
    void onExitButtonClick(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Implements part search logic when text is typed into part search field and "Enter" key is pressed.
     * @param actionEvent "Enter" key pressed
     */
    @FXML
    void onPartSearchTextTyped(ActionEvent actionEvent) {
        String query = partsSearchField.getText().toLowerCase(); // convert to lower case so search is not case-sensitive
        ObservableList<Part> searchResults = Inventory.lookupPart(query);
        if (searchResults.size() > 0) { // Match was found. Set TableView to show result.
            partsTableView.setItems(searchResults);
            }
        else { // If no matching string was found, text entered may be an integer, so parseInt() is called to attempt to convert text from search field to integer.
            try {
                Part searchPart = Inventory.lookupPart(Integer.parseInt(partsSearchField.getText()));
                if(searchPart == null) { // Searched with integer but no matching partID found.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No part with the specified Part ID found.");
                    alert.showAndWait();
                }
                else  {
                    partsTableView.getSelectionModel().select(searchPart); // select part row in table view
                }
            }
            catch (NumberFormatException exception) { // Could not find matching string and text entered in search was not an integer.
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No part with the specified Part Name found.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Implements product search logic when text is typed into product search field and "Enter" key is pressed
     * @param actionEvent "Enter" key pressed
     */
    @FXML
    void onProductSearchTextTyped(ActionEvent actionEvent) {
        String query = productSearchField.getText().toLowerCase(); // convert to lower case so search is not case-sensitive
        ObservableList<Product> searchResults = Inventory.lookupProduct(query);
        if (searchResults.size() > 0) { // Match was found. Set TableView to show result.
            productsTableView.setItems(searchResults);
        }
        else { // If no matching string was found, text entered may be an integer, so parseInt() is called to attempt to convert text from search field to integer.
            try {
                Product searchProduct = Inventory.lookupProduct(Integer.parseInt(productSearchField.getText()));
                if (searchProduct == null) { // Searched with integer but no matching partID found.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No product with the specified Product ID found.");
                    alert.showAndWait();
                }
                else { // Match was found. Set TableView to show result.
                    productsTableView.getSelectionModel().select(searchProduct);
                }
            } catch (NumberFormatException e) { // Could not find matching string and text entered in search was not an integer.
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No product with the specified Product Name found.");
                alert.showAndWait();
            }
        }
    }
}