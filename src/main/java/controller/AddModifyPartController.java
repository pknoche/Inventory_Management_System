package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;

/**
 * Controller for add/modify parts menu. Contains logic for all elements of add/modify parts menu.
 */
public class AddModifyPartController {
    @FXML
    private ToggleGroup PartSource;
    @FXML
    private TextField idField;
    @FXML
    private RadioButton inHouseToggle;
    @FXML
    private TextField invField;
    @FXML
    private TextField machineIdCompanyNameField;
    @FXML
    private TextField maxField;
    @FXML
    private TextField minField;
    @FXML
    private TextField nameField;
    @FXML
    private RadioButton outsourcedToggle;
    @FXML
    private Label partSourceModifier;
    @FXML
    private TextField priceField;
    @FXML
    private Label addModifyPartLabel;


    /**
     * Saves data entered into form. Checks for missing/mismatched fields and checks logic for min, max, and inv fields. Uses different logic depending on whether part is being created for the first time or is being modified.
     * @param actionEvent save button clicked
     * @throws IOException
     */
    @FXML
    void onSaveButtonClick(ActionEvent actionEvent) throws IOException {

        try {
            // get values from text fields and check for logic errors
            int inventory = -1;
            int max = -1;
            int min = -1;
            double price = -1;
            try {
                inventory = Integer.parseInt(invField.getText());
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
            if (inventory < min || inventory > max) {
                generateErrorMessage(6);
                return;
            }
            // determine if part is being added or modified and generate new ID or get existing ID
            if (idField.getText().isBlank()) {  // add part button was clicked - determine which part type is being created and call appropriate constructor
                int id = Inventory.generatePartId();
                if (inHouseToggle.isSelected()) {
                    int machineId = Integer.parseInt(machineIdCompanyNameField.getText());
                    Inventory.addPart(new InHouse(id, name, price, inventory, min, max, machineId));
                } else { // Outsourced is selected
                    String companyName = machineIdCompanyNameField.getText();
                    Inventory.addPart(new Outsourced(id, name, price, inventory, min, max, companyName));
                }
            } else {  // modify part button was clicked - get data from part being modified, determine its index, and add data to text fields
                ObservableList<Part> partsList = Inventory.getAllParts();
                for (int i = 0; i < Inventory.getAllParts().size(); i++) { // determine index of part to be modified
                    if (partsList.get(i).getId() == Integer.parseInt(idField.getText())) { // create new part and replace part being modified with it
                        if (inHouseToggle.isSelected()) {
                            int machineId = Integer.parseInt(machineIdCompanyNameField.getText());
                            InHouse updatedPart = (new InHouse(Integer.parseInt(idField.getText()), name, price, inventory, min, max, machineId));
                            Inventory.updatePart(i, updatedPart);
                            break;
                        } else {
                            String companyName = machineIdCompanyNameField.getText();
                            Outsourced updatedPart = (new Outsourced(Integer.parseInt(idField.getText()), name, price, inventory, min, max, companyName));
                            Inventory.updatePart(i, updatedPart);
                            break;
                        }
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
     * Sets last field to "Machine ID", which is specific to in-house parts.
     * @param actionEvent In-House radio button clicked
     * @throws IOException
     */
    @FXML
    void onInHouseButtonClick(ActionEvent actionEvent) throws IOException {
        partSourceModifier.setText("Machine ID");
    }

    /**
     * Sets last field to "Company Name", which is specific to outsourced parts.
     * @param actionEvent Outsourced radio button clicked
     * @throws IOException
     */
    @FXML
    void onOutsourcedButtonClick(ActionEvent actionEvent) throws IOException {
        partSourceModifier.setText("Company Name");
    }

    /**
     * Pre-loads data into form for part being modified. Sets In-House or Outsourced radio button to selected depending on part type.
     * @param id id of part being modified.
     */
    public void modifyPart (int id) {
        for(Part part : Inventory.getAllParts()) {
            if(part.getId() == id) {
                idField.setText(Integer.toString(id));
                nameField.setText(part.getName());
                invField.setText(Integer.toString(part.getStock()));
                priceField.setText(Double.toString(part.getPrice()));
                maxField.setText(Integer.toString(part.getMax()));
                minField.setText(Integer.toString(part.getMin()));
                addModifyPartLabel.setText("Modify Part");
                if(part instanceof InHouse) {
                    inHouseToggle.setSelected(true);
                    machineIdCompanyNameField.setText(Integer.toString(((InHouse) part).getMachineId()));
                }
                else{
                    outsourcedToggle.setSelected(true);
                    partSourceModifier.setText("Company Name");
                    machineIdCompanyNameField.setText(((Outsourced) part).getCompanyName());
                }
            }
        }
    }

    /**
     * Generates error message if check fails on saving part.
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