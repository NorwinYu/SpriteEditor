package application.control;

import application.Main;
import application.model.AlertWithCss;
import application.model.PreviewWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ChangeSizeControl {
	
	@FXML Button cancelButton;
	@FXML Button changeButton;
	@FXML ProgressBar progressBarChange;
	@FXML ChoiceBox<String> choiceBox;
	
	private Stage stage;
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
		ObservableList<String> options = FXCollections.observableArrayList(
				"16 x 16",
				"32 x 32",
				"64 x 64"
				);
		choiceBox.setItems(options);
	}
	
	@FXML public void onPressCancel() {
		this.stage.close();
	}
	
	@FXML public void onPressChange() {
		if (choiceBox.getValue() == null) {
			AlertWithCss alert = new AlertWithCss(AlertType.INFORMATION, main);
			alert.setTitle("Message");
			alert.setHeaderText("Can not change size");
			alert.setContentText("You have not chosen the size");
			alert.show();
		}
		else {
			progressBarChange.setProgress(0);
			this.previewWindow.canvasClean();
			progressBarChange.setProgress(0.5);
			if (choiceBox.getValue().equals("16 x 16")) {
				this.previewWindow.setImageHeight(16.0);
				this.previewWindow.setImageWidth(16.0);
			}
			else if (choiceBox.getValue().equals("32 x 32")) {
				this.previewWindow.setImageHeight(32.0);
				this.previewWindow.setImageWidth(32.0);
			}
			else if (choiceBox.getValue().equals("64 x 64")) {
				this.previewWindow.setImageHeight(64.0);
				this.previewWindow.setImageWidth(64.0);
			}
			
			progressBarChange.setProgress(1);
			
			this.changeButton.setText("Done");
			this.changeButton.getStyleClass().add("success");
			this.changeButton.setOnAction((event) -> {
				  this.stage.close();
			});
		}
	}
}
