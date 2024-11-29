
package presentation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Font;
/**
 *
 * @author Danny
 */
public class Presentation extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hospital Intake Patient Registration System");

        //start with either login or creating new account
        Label IDLabel = new Label("ID:");
        TextField IDField = new TextField();

        Label lblAllFields = new Label("All Field Are Required Unless Marked As Optional");

        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();

        Button submitButton = new Button("Submit");
        Button forgotButton = new Button("Forgot user ID or password?");
        Button registerButton = new Button("Register for an account");

        String kaiserPic = "file:C:/Users/Danny/Downloads/Kaiser-Permanente-Logo.jpg";
        Image image = new Image(kaiserPic);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.add(imageView, 0, 0);

        gridPane.add(lblAllFields, 0, 1);

        gridPane.add(IDLabel, 0, 2);
        gridPane.add(IDField, 1, 2);

        gridPane.add(passwordLabel, 0, 3);
        gridPane.add(passwordField, 1, 3);

        gridPane.add(submitButton, 1, 4);

        gridPane.add(forgotButton, 0, 5);
        gridPane.add(registerButton, 1, 5);
        
        forgotButton.setOnAction(e ->
        {
            primaryStage.hide();
            forgotWindow();
        }
        );
        
        submitButton.setOnAction(e ->
        {
            int id = Integer.parseInt(IDField.getText());
            String password = passwordField.getText();
            
            
            
           boolean flag = checkCredentials(id, password);
           
           if(flag)
           {
               primaryStage.hide();
               JOptionPane.showMessageDialog(null, "USER: " + id + " HAS BEEN FOUND");
               
               
                try {
                    String name = getName(id, password);
                    
                    foundUsers(name);
                } catch (SQLException ex) {
                    Logger.getLogger(Presentation.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
           else
           {
                JOptionPane.showMessageDialog(null, "CANNOT FIND USER: " + id);
           }
           
           
        }
        );

        registerButton.setOnAction(e
                -> {
            primaryStage.hide();
            openRegisterWindow();
        }
        );

        Scene scene = new Scene(gridPane, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    
    
    
    public void forgotWindow()
    {
        Stage forgotStage = new Stage();
        
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        
        Label birthLabel = new Label("Birth:");
        TextField birthField = new TextField();
        
        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();
        
        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();
        
        
        String picture = "file:C:/Users/Danny/Downloads/Kaiser-Permanente-fb.png";
        String picture2 = "file:C:/Users/Danny/Downloads/Question-mark.jpg";
        
        Image image = new Image(picture);
        Image image2 = new Image(picture2);
        
        ImageView imageView = new ImageView(image);
        ImageView imageView2 = new ImageView(image2);
        
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        imageView2.setFitWidth(200);
        imageView2.setFitHeight(100);
        imageView2.setPreserveRatio(true);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        
        gridPane.add(imageView, 0, 0);
        gridPane.add(imageView2, 1, 0);
        
        gridPane.add(nameLabel, 0, 3);
        gridPane.add(nameField, 1, 3);
        
        gridPane.add(birthLabel, 0, 4);
        gridPane.add(birthField, 1, 4);
        
        gridPane.add(phoneLabel, 0, 5);
        gridPane.add(phoneField, 1, 5);
        
        gridPane.add(addressLabel, 0, 6);
        gridPane.add(addressField, 1, 6);
        
        Button submit = new Button("Submit");
        
        gridPane.add(submit, 1, 7);
        
        submit.setOnAction(e ->
        {
            boolean flag = forgotCredentials(nameField.getText(), birthField.getText(), phoneField.getText(), addressField.getText());
            
            if(flag)
            {
                forgotStage.hide();
                JOptionPane.showMessageDialog(null, "Successfully Found User");
                try {
                    foundUsers(nameField.getText());
                } catch (SQLException ex) {
                    Logger.getLogger(Presentation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Couldn't Find User");
            }
        }
        );
        
        forgotStage.setTitle("Forgot Credentials");
        
        Scene scene = new Scene(gridPane);
        forgotStage.setScene(scene);
        forgotStage.show();
    
    }
    
    public Label createLabel(String text, String font, int fontSize)
    {
        Label label = new Label(text);
        label.setFont(new Font(font, fontSize));
        return label;
    }
    
    public String getName(int id, String password) throws SQLException
    {
        String name = null;
    
        String url = "jdbc:sqlite:C:/Users/Danny/OneDrive - Rancho Santiago Community College District/Documents/NetBeansProjects/Presentation/kaiser.db";

        String sql = "SELECT id, password, name FROM kaiser WHERE id = ? AND password = ?";
        
        try(Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, id);
            pstmt.setString(2, password);
            
            ResultSet result = pstmt.executeQuery();
            
            if(result.next())
            {
                name = result.getString("name");
            }
        } catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        return name;
    }
           
    
    public void foundUsers(String name) throws SQLException {
    String url = "jdbc:sqlite:C:/Users/Danny/OneDrive - Rancho Santiago Community College District/Documents/NetBeansProjects/Presentation/kaiser.db";

    String sql = "SELECT id, name, gender, emergencyContact, password, appoinment, appointmentReason, birth, address, phone, payment FROM kaiser WHERE name = ?";

    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, name);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            String nameRecovered = rs.getString("name");
            String gender = rs.getString("gender");
            String emergencyContact = rs.getString("emergencyContact");
            String password = rs.getString("password");
            String appoinment = rs.getString("appoinment");  // Updated here
            String appointmentReason = rs.getString("appointmentReason");
            String birth = rs.getString("birth");
            String address = rs.getString("address");
            String phone = rs.getString("phone");
            String payment = rs.getString("payment");

            // Display retrieved data
            System.out.println("ID: " + id);
            System.out.println("Name: " + nameRecovered);
            System.out.println("Gender: " + gender);
            System.out.println("Emergency Contact: " + emergencyContact);
            System.out.println("Appointment: " + appoinment);
            System.out.println("Appointment Reason: " + appointmentReason);
            System.out.println("Password: " + password);
            System.out.println("Birth: " + birth);
            System.out.println("Address: " + address);
            System.out.println("Phone: " + phone);
            System.out.println("Payment: " + payment);
            

            // Stage code here
            Stage stage = new Stage();
            stage.setTitle("USER INFORMATION");
            
            int fontSize = 20;
            String font = "Arial";
            
            String stringID = String.valueOf(id);
            
            Label idLabel = createLabel("ID:", font, fontSize);
            Label showID = createLabel(stringID, font, fontSize);
            
            Label nameLabel = createLabel("Name:", font, fontSize);
            Label showName = createLabel(name, font, fontSize);
            
            Label passwordLabel = createLabel("Password:", font, fontSize);
            Label showPassword = createLabel(password, font, fontSize);
                   
            Label appointmentLabel = createLabel("Appointment:", font, fontSize);
            Label showAppointment = createLabel(address, font, fontSize);
            
            Label birthLabel = createLabel("Birth:", font, fontSize);
            Label showBirth = createLabel(birth, font, fontSize);
            
            Label genderLabel = createLabel("Gender:", font, fontSize);
            Label showGender = createLabel(gender, font, fontSize);
            
            Label addressLabel = createLabel("Address:", font, fontSize);
            Label showAddress = createLabel(address, font, fontSize);
            
            Label phoneLabel = createLabel("Phone:", font, fontSize);
            Label showPhone = createLabel(phone, font, fontSize);
            
            Label emergencyContactLabel = createLabel("Emergency Contact:", font, fontSize);
            Label showEmergencyContact = createLabel(emergencyContact, font, fontSize);
            
            Label paymentLabel = createLabel("Payment:", font, fontSize);
            Label showPayment = createLabel(payment, font, fontSize);
            
            Label appointmentReasonLabel = createLabel("Appointment Reason:", font, fontSize);
            Label showAppointmentReason = createLabel(appointmentReason, font, fontSize);
            
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(20, 20, 20, 20));
            gridPane.setVgap(10);
            gridPane.setHgap(10);
            
            gridPane.add(idLabel, 0, 0);
            gridPane.add(showID, 2, 0);
            
            gridPane.add(nameLabel, 0, 1);
            gridPane.add(showName, 2, 1);
            
            gridPane.add(passwordLabel, 0, 2);
            gridPane.add(showPassword, 2, 2);
            
            gridPane.add(appointmentLabel, 0, 3);
            gridPane.add(showAppointment, 2, 3);
            
            gridPane.add(birthLabel, 0, 4);
            gridPane.add(showBirth, 2, 4);
            
            gridPane.add(genderLabel, 0, 5);
            gridPane.add(showGender, 2, 5);
            
            gridPane.add(addressLabel, 0, 6);
            gridPane.add(showAddress, 2, 6);
            
            gridPane.add(phoneLabel, 0, 7);
            gridPane.add(showPhone, 2, 7);
            
            gridPane.add(emergencyContactLabel, 0, 8);
            gridPane.add(showEmergencyContact, 2, 8);
            
            gridPane.add(paymentLabel, 0, 9);
            gridPane.add(showPayment, 2, 9);
            
            gridPane.add(appointmentReasonLabel, 0, 10);
            gridPane.add(showAppointmentReason, 2, 10);
            
            
            Scene scene = new Scene(gridPane, 500, 400);
            stage.setScene(scene);
            stage.show();
            

        } else {
            System.out.println("No user found with the name: " + name);
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}


    
    public boolean forgotCredentials(String name, String birth, String phone, String address) {
        String url = "jdbc:sqlite:C:/Users/Danny/OneDrive - Rancho Santiago Community College District/Documents/NetBeansProjects/Presentation/kaiser.db";

        boolean isValidUser = false;

        String sql = "SELECT * FROM kaiser WHERE name = ? AND birth = ? AND phone = ? AND address = ?";

        try (Connection conn = DriverManager.getConnection(url); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            
            pstmt.setString(1, name);
            pstmt.setString(2, birth);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isValidUser = true; 
            }

        } catch (SQLException e) {
            System.out.println("Database connection or query error: " + e.getMessage());
        }

        return isValidUser;
    }

    public void openRegisterWindow() {
        Stage secondStage = new Stage();

        secondStage.setTitle("Registering For Account");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();

        Label appoinmentLabel = new Label("Appoinment Date:");
        TextField appointmentField = new TextField();

        Label birthLabel = new Label("Birth Date:");
        TextField birthField = new TextField();

        //make gender a two box
        Label genderLabel = new Label("Gender:");
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleButton = new RadioButton("Male");
        RadioButton femaleButton = new RadioButton("Female");

        maleButton.setToggleGroup(genderGroup);
        femaleButton.setToggleGroup(genderGroup);

        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();

        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();
        
        Label appointmentReasonLabel = new Label("Appointment Reason:");
        TextField appointmentReasonField = new TextField();
        
        Label emergencyContactLabel = new Label("Emergency Contact Number:");
        TextField emergencyContactField = new TextField();

        Label paymentLabel = new Label("Payment:");
        ComboBox<String> paymentBox = new ComboBox<>();
        paymentBox.getItems().addAll("Credit Card", "Debit Card");

        String newImage = "file:C:/Users/Danny/Downloads/kaiser_logo_900x600.png";
        Image image = new Image(newImage);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        String newImage2 = "file:C:/Users/Danny/Downloads/6681204.png";
        Image image2 = new Image(newImage2);

        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(150);
        imageView2.setFitHeight(150);
        imageView2.setPreserveRatio(true);

        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.add(imageView, 0, 0);
        gridPane.add(imageView2, 2, 0);

        gridPane.add(nameLabel, 0, 3);
        gridPane.add(nameField, 1, 3);

        gridPane.add(passwordLabel, 0, 4);
        gridPane.add(passwordField, 1, 4);

        gridPane.add(appoinmentLabel, 0, 5);
        gridPane.add(appointmentField, 1, 5);

        gridPane.add(birthLabel, 0, 6);
        gridPane.add(birthField, 1, 6);

        gridPane.add(genderLabel, 0, 7);
        gridPane.add(maleButton, 1, 7);
        gridPane.add(femaleButton, 2, 7);
        
        gridPane.add(addressLabel, 0, 8);
        gridPane.add(addressField, 1, 8);
        
        gridPane.add(phoneLabel, 0, 9);
        gridPane.add(phoneField, 1, 9);
        
        gridPane.add(emergencyContactLabel, 0, 10);
        gridPane.add(emergencyContactField, 1, 10);
        
        gridPane.add(paymentLabel, 0, 11);
        gridPane.add(paymentBox, 1, 11);
        
        gridPane.add(appointmentReasonLabel, 0, 12);
        gridPane.add(appointmentReasonField, 1, 12);
        
        Button submit = new Button("Submit");
        
        gridPane.add(submit, 1, 13);

        submit.setOnAction(e ->
        {
            Random rand = new Random();
            
            // Generate a random 7-digit number between 1000000 and 9999999
            int id = 1000000 + rand.nextInt(9000000);
            
            JOptionPane.showMessageDialog(null, "ID NUMBER IS NOW: " + id);
            
            String name = nameField.getText();
            String password = passwordField.getText();
            String appointment = appointmentField.getText();
            String birth = birthField.getText();
            String gender = maleButton.isSelected() ? "Male" : "Female";
            String address = addressField.getText();
            String phone = phoneField.getText();
            String contact = emergencyContactField.getText();
            String payment = paymentBox.getValue();
            String appointmentReason = appointmentReasonField.getText();
            
            try
            {
                insertData(id, name, password, appointment, birth, gender, address, phone, contact, payment, appointmentReason); 
            } catch(SQLException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        );

        Scene scene = new Scene(gridPane, 500, 600);
        secondStage.setScene(scene);
        secondStage.show();
    }
    
     public boolean checkCredentials(int id, String password) {
        String url = "jdbc:sqlite:C:/Users/Danny/OneDrive - Rancho Santiago Community College District/Documents/NetBeansProjects/Presentation/kaiser.db";

        boolean isValidUser = false;

        String sql = "SELECT * FROM kaiser WHERE id = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            
            pstmt.setInt(1, id);         
            pstmt.setString(2, password);   

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isValidUser = true; 
            }

        } catch (SQLException e) {
            System.out.println("Database connection or query error: " + e.getMessage());
        }

        return isValidUser;
    }

    private void insertData(int id, String name, String password, String appoinment, String birth, String gender, String address, String phone, String contact, String payment, String appointmentReason) throws SQLException {
        String url = "INSERT INTO kaiser (id, name, password, appoinment, birth, gender, address, phone, emergencyContact, payment, appointmentReason) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(url)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setString(4, appoinment);
            pstmt.setString(5, birth);
            pstmt.setString(6, gender);
            pstmt.setString(7, address);
            pstmt.setString(8, phone);
            pstmt.setString(9, contact);
            pstmt.setString(10, payment);
            pstmt.setString(11, appointmentReason);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "DATA HAS BEEN INSERTED PATIENT: " + id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    
//    private void showDetails(int userID, String userName)
//    {
//     String url = "INSERT INTO kaiser (id, name) VALUES (?, ?)";
//     
//            try(Connection conn = this.connect();
//                    PreparedStatement pstmt = conn.prepareStatement(url, Statement.RETURN_GENERATED_KEYS))
//             {
//                 pstmt.setInt(1, userID);
//                 pstmt.setString(2, userName);
//                 
//                 int affectedRows = pstmt.executeUpdate();
//                 
//                 if(affectedRows > 0)
//                 {
//                    try(ResultSet result = pstmt.getGeneratedKeys())
//                    {
//                     if(result.next())
//                     {
//                        int generatedID = result.getInt(1);
//                        
//                         System.out.println("INFO: INSERTED WITH ID: " + generatedID);
//                     }
//                    }
//                 }
//                 
//             } catch(SQLException e)
//             {
//                 System.out.println("ERROR HERE");
//                 System.out.println(e.getMessage());
//             }
//    }

    public Connection connect() {
        String url = "jdbc:sqlite:C:/Users/Danny/OneDrive - Rancho Santiago Community College District/Documents/NetBeansProjects/Presentation/kaiser.db";

        Connection conn = null;

        try {

            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return conn;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
