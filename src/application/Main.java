package application;
	
import java.io.IOException;

import application.control.ChangeSizeControl;
import application.control.ImportImageControl;
import application.control.MainControl;
import application.model.PreviewWindow;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@FXML Canvas canvas;
	
	private Stage primaryStage;
	private PreviewWindow previewWindow;
	 
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
		this.primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("resources/icon/pixel.png").toString()));
		this.primaryStage.setTitle("Sprite Editor");
		
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root);
			
			scene.getStylesheets().add(getClass().getResource("bootstrap3.css").toExternalForm());
			
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("application/view/MainGUI.fxml"));
			
            Parent content = loader.load();
			root.setCenter(content);
			primaryStage.setScene(scene);
			
			MainControl mainControl = loader.getController();
			mainControl.setMain(this);
			mainControl.setStage(primaryStage);
			this.previewWindow = mainControl.getPreviewWindow();
            
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public boolean showImportImageWindow() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("application/view/ImportImageWindow.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage stage = new Stage();
			stage.setTitle("Import Image");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(this.primaryStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource("bootstrap3.css").toExternalForm());
			stage.setScene(scene);
			
			ImportImageControl importImageControl = loader.getController();
			importImageControl.setStage(stage);
			importImageControl.setPreviewWindow(previewWindow);
			importImageControl.setMain(this);
			
			stage.showAndWait();
	        
			return true;
		} catch (IOException e) {
			e.printStackTrace();
	    		return false;
		}
	}
	
	public boolean showChangeSizeWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("application/view/ChangeSizeWindow.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage stage = new Stage();
			stage.setTitle("Change Image Size");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(this.primaryStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource("bootstrap3.css").toExternalForm());
			stage.setScene(scene);
			
			ChangeSizeControl changeSizeControl = loader.getController();
			changeSizeControl.setStage(stage);
			changeSizeControl.setPreviewWindow(previewWindow);
			changeSizeControl.setMain(this);
			
			stage.showAndWait();
	        
			return true;
		} catch (IOException e) {
			e.printStackTrace();
	    		return false;
		}
	}
}
