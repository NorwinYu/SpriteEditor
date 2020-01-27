package application.control;

import java.io.File;

import application.Main;
import application.model.AlertWithCss;
import application.model.PreviewWindow;
import application.task.ImageFillFull;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImportImageControl {
	@FXML TextArea directoryTextArea;
	@FXML Button browseButton;
	@FXML Button cancelButton;
	@FXML Button importButton;
	@FXML ImageView imageView;
	@FXML ProgressBar progressBarImport;
	
	private Stage stage;
	private Image image;
	private PreviewWindow previewWindow;
	private Main main;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setPreviewWindow(PreviewWindow previewWindow) {
		this.previewWindow = previewWindow;
	}

	public void setMain(Main main) {
		this.main = main;
	}
	
	public void initialize() {
		directoryTextArea.setFocusTraversable(false);
	}
	
	@FXML public void onPressBrowse() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		File selectedFile = fileChooser.showOpenDialog(stage);
		
		if(selectedFile == null) {
			directoryTextArea.setText("No File selected");
		}else{
			directoryTextArea.setText(selectedFile.getAbsolutePath());
			image = new Image(selectedFile.toURI().toString());
			imageView.setImage(image);
        }
	}

	
	@FXML public void onPressCancel() {
		this.stage.close();
	}
	
	@FXML public void onPressImport() {
		if (image == null) {
			AlertWithCss alert = new AlertWithCss(AlertType.INFORMATION, main);
			alert.setTitle("Message");
			alert.setHeaderText("Can not import image");
			alert.setContentText("You have not chosen the file");
			alert.show();
		}
		else {
			if (!checkSize()) {
				AlertWithCss alert = new AlertWithCss(AlertType.INFORMATION, main);
				alert.setTitle("Message");
				alert.setHeaderText("Illegal image size - " + Math.floor(image.getWidth()) + "x" + Math.floor(image.getHeight()));
				alert.setContentText("Please import file with size : 16x16 / 32x32 / 64x64");
				alert.show();
				return;
			}
			
			previewWindow.setImageWidth(image.getWidth());
			previewWindow.setImageHeight(image.getHeight());
			previewWindow.canvasClean();
			
			ImageFillFull iff = new ImageFillFull(this.previewWindow, this.image);
			Thread iffThread = new Thread(iff);
			iffThread.start();
			progressBarImport.progressProperty().bind(iff.progressProperty());
			try {
				iffThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			this.importButton.setText("Done");
			this.importButton.getStyleClass().add("success");
			this.importButton.setOnAction((event) -> {
				  this.stage.close();
			});
		}
	}
	
	public boolean checkSize() {
		if (Math.floor(image.getWidth()) != Math.floor(image.getHeight())) {
			return false;
		}
		if (Math.floor(image.getWidth()) != 16 && Math.floor(image.getWidth()) != 32 && Math.floor(image.getWidth()) != 64) {
			return false;
		}
		return true;
	}
}
