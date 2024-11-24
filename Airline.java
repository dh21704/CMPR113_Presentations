/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentations;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Danny
 */
public class AirLine extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Automobile Mechanic Application");
        
        //formatting the grid
        
        
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        
        Label serviceLabel = new Label("Select Service:");
        
        ToggleGroup serviceGroup = new ToggleGroup();
        
        RadioButton oilButton = new RadioButton("Oil Change");
        RadioButton brakeButton = new RadioButton("Brake Repair");
        
        oilButton.setToggleGroup(serviceGroup);
        brakeButton.setToggleGroup(serviceGroup);
        
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        
        gridPane.add(serviceLabel, 0, 2);
        gridPane.add(oilButton, 1, 2);
        gridPane.add(brakeButton, 2, 2);
        
        Scene scene = new Scene(gridPane, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
