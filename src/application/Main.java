package application;
	


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;



public class Main extends Application {
	
	private TableView<Student> tableView;
    private ObservableList<Student> studentList;
    private Connection connection;
    TextField nom =  new TextField();
	TextField prenom =  new TextField();
	TextField numero =  new TextField();
	TextField email =  new TextField();
    
	@Override
	public void start(Stage primaryStage) {
			connectToDatabase() ;
			
			tableView = new TableView<>();
			studentList = FXCollections.observableArrayList();
			tableView.setItems(studentList);
			
			//layout1
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,850,400);
			//layout2
			BorderPane root_profile = new BorderPane();
			Scene scene_profile = new Scene(root_profile,550,350);
			
			primaryStage.setTitle("ENSAO");
			
			//button download 
			Button download = new Button("Imprimer la fiche");
			
			
			//layout1 component
			Label titre = new Label("La liste des étudiants inscrits à l'ENSAO :");
			titre.setFont(Font.font(20));
			titre.setTextFill(Color.DARKBLUE);
			titre.setPadding(new Insets(10,0,10,120));
			//layout2 component
			Label titre_profile = new Label("Profile  :");
			
			titre_profile.setFont(Font.font(20));
			titre_profile.setTextFill(Color.DARKBLUE);
			titre_profile.setPadding(new Insets(20));
			
			GridPane grid = new GridPane();
			TextArea nom_profile =  new TextArea();
			TextArea prenom_profile =  new TextArea();
			TextArea CNE_profile =  new TextArea();
			TextArea Email_profile =  new TextArea();
			
			nom_profile.setMaxSize(300, 10);
			prenom_profile.setMaxSize(300, 10);
			CNE_profile.setMaxSize(300, 10);
			Email_profile.setMaxSize(300, 10);
			
			Label nom_porofile = new Label("Prenom :");
			Label prenom_porofile = new Label("Nom :");
			Label cne_porofile = new Label("CNE :");
			Label email_porofile = new Label("Email :");
			
			grid.add(nom_profile, 2, 0);
			grid.add(prenom_profile, 2, 1);
			grid.add(CNE_profile, 2, 2);
			grid.add(Email_profile, 2, 3);
			grid.add(nom_porofile, 0, 0);
			grid.add(prenom_porofile, 0, 1);
			grid.add(cne_porofile, 0, 2);
			grid.add(email_porofile, 0, 3);
	
			grid.setVgap(10);
			grid.setHgap(10);
			grid.setPadding(new Insets(20));
			
			Button back = new Button ("go back");
			
			
			HBox hbox2 = new HBox();
			hbox2.getChildren().addAll(back);
			hbox2.setPadding(new Insets(10));
			hbox2.setSpacing(10);
			root_profile.setBottom(hbox2);
			root_profile.setCenter(grid);
			root_profile.setTop(titre_profile);
			
			HBox hbox1 = new HBox();
			HBox hbox3 = new HBox();
			VBox vbox = new VBox();
			
			Button insert = new Button("insert");
			Button delete = new Button("delete");
			Button modify = new Button("modify");
			Button profile = new Button("voire profile");
			
			hbox1.getChildren().addAll(nom,prenom,numero,email);
			hbox1.setPadding(new Insets(10));
			hbox1.setSpacing(10);
			hbox3.getChildren().addAll(insert,delete,modify,profile,download);
			hbox3.setPadding(new Insets(10));
			hbox3.setSpacing(10);
			vbox.getChildren().addAll(hbox1,hbox3);
			
			
			
			//ajouter des columns 
			TableColumn<Student, String> FirstNameColumn = new TableColumn<Student,String>("First Name");
			TableColumn<Student, String> LastNameColumn = new TableColumn<Student,String>("Last Name");
			TableColumn<Student, Integer> ResultColmn = new TableColumn<Student,Integer>("CNE");
			TableColumn<Student, String> NoteColumn = new TableColumn<Student,String>("Email");
			
			FirstNameColumn.setCellValueFactory(new PropertyValueFactory<Student,String>("FirstName"));
			LastNameColumn.setCellValueFactory(new PropertyValueFactory<Student,String>("LastName"));
			ResultColmn.setCellValueFactory(new PropertyValueFactory<Student,Integer>("CNE"));
			NoteColumn.setCellValueFactory(new PropertyValueFactory<Student,String>("Email"));
			
			//tableView.getColumns().addAll(FirstNameColumn,LastNameColumn,ResultColmn,NoteColumn);
			
			tableView.getColumns().add(FirstNameColumn);
			tableView.getColumns().add(LastNameColumn);
			tableView.getColumns().add(ResultColmn);
			tableView.getColumns().add(NoteColumn);
			
			//pour normaliser les colonnes
			tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			//insert button
			insert.setOnAction(e->AddStudent());
			//delete button 
			delete.setOnAction(e->deleteStudent());
			//button modify
			modify.setOnAction(e->updateStudent());
			//profile
			profile.setOnAction(e->{
				primaryStage.setScene(scene_profile);
		        Student selectedStudent= tableView.getSelectionModel().getSelectedItem();
		        nom_profile.appendText(selectedStudent.getFirstName());
		        prenom_profile.appendText(selectedStudent.getLastName());
		        String cne =String.format("%d",selectedStudent.getCNE());
		        CNE_profile.appendText(cne);
		        Email_profile.appendText(selectedStudent.getEmail());	
			});
			
			//button download
			download.setOnAction(event ->printButtonClicked());
			//button go back
			back.setOnAction(e->{
				primaryStage.setScene(scene);
			});
			
			root.setBottom(vbox);
			root.setTop(titre);
			root.setCenter(tableView);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			getStudents();
	}
	
	
	private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/ensa";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Error connecting to database.");
            e.printStackTrace();
        }
    }
	
	private void getStudents() {
        try {
        	PreparedStatement statement = connection.prepareStatement("SELECT * FROM Student");
            ResultSet result = statement.executeQuery();

            studentList.clear();
            while (result.next()) {
            	Student etudiant = new Student();
				etudiant.setFirstName(result.getString("FirstName"));
				etudiant.setLastName(result.getString("LastName"));
				etudiant.setCNE(result.getInt("CNE"));
				etudiant.setEmail(result.getString("Email"));
				studentList.add(etudiant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	private void AddStudent() {
        String firstName = nom.getText();
        String lastName = prenom.getText();
        String cnee = numero.getText();
        Integer cne = Integer.parseInt(cnee);
        String mail = email.getText();
        

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Student (FirstName,LastName,CNE,Email) VALUES (?, ?, ?,?)");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, cne);
            statement.setString(4, mail);
            statement.executeUpdate();
            statement.close();
            nom.clear();
            prenom.clear();
            numero.clear();
            email.clear();
            getStudents();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	private void updateStudent() {
        Student selectedStudent= tableView.getSelectionModel().getSelectedItem();
        
	        
        
        	int id = selectedStudent.getCNE();
        	System.out.println("je suis "+ id);
        	String firstName = nom.getText();
        	System.out.println(firstName);
            String lastName = prenom.getText();
            String mail = email.getText();

            try {
                PreparedStatement statement = connection.prepareStatement("update Student set FirstName=? , LastName=? , Email=? where CNE=?");
                statement.setString(1, firstName);
                System.out.println(firstName);
                statement.setString(2, lastName);
                
                statement.setString(3, mail);
                statement.setInt(4, id);
                System.out.println(id);
                statement.executeUpdate();
                statement.close();
                nom.clear();
                prenom.clear();
                numero.clear();
                email.clear();
                getStudents();
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
        
    }
	
	private void deleteStudent() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
            int id = selectedStudent.getCNE();
            System.out.println(id);

            try {
                PreparedStatement statement = connection.prepareStatement("delete from Student where CNE = ?");
                statement.setInt(1, id);
                System.out.println(id);
                statement.executeUpdate();
                statement.close();
                getStudents();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
    }
	 private void printButtonClicked() {
	        Student selectedPerson = tableView.getSelectionModel().getSelectedItem();
	        if (selectedPerson != null) {
	            String firstName = selectedPerson.getFirstName();
	            String lastName = selectedPerson.getLastName();
	            String email = selectedPerson.getEmail();

	            try {
	                PDDocument document = new PDDocument();
	                PDPage page = new PDPage();
	                document.addPage(page);

	                PDPageContentStream contentStream = new PDPageContentStream(document, page);

	                contentStream.beginText();
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
	                contentStream.newLineAtOffset(100, 700);
	                contentStream.showText("First Name: " + firstName);
	                contentStream.newLineAtOffset(0, -20);
	                contentStream.showText("Last Name: " + lastName);
	                contentStream.newLineAtOffset(0, -20);
	                contentStream.showText("Email: " + email);
	                contentStream.endText();

	                contentStream.close();

	                FileChooser fileChooser = new FileChooser();
	                fileChooser.setTitle("Save PDF File");
	                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
	                File selectedFile = fileChooser.showSaveDialog(tableView.getScene().getWindow());

	                if (selectedFile != null) {
	                    document.save(selectedFile);
	                    System.out.println("PDF file saved successfully.");
	                } else {
	                    System.out.println("PDF file not saved.");
	                }

	                document.close();
	            } catch (IOException e) {
	                System.out.println("Error generating or saving PDF file.");
	                e.printStackTrace();
	            }
	        }
	    }

	 
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
