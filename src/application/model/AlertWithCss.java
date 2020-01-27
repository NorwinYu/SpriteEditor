package application.model;

import java.util.Iterator;

import application.Main;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class AlertWithCss extends Alert {
	
	public AlertWithCss(AlertType alertType, Main main) {
		super(alertType);
		setStyle(main);
	}

	public AlertWithCss(AlertType alertType, String contentText, ButtonType[] buttons, Main main) {
		super(alertType, contentText, buttons);
		setStyle(main);
	}
	
	public void setStyle(Main main) {
		this.getDialogPane().getStylesheets().add(main.getClass().getResource("bootstrap3.css").toExternalForm());
		ButtonBar buttonBar = (ButtonBar)this.getDialogPane().lookup(".button-bar");
		for (Iterator<Node> i =  buttonBar.getButtons().iterator(); i.hasNext();) {
			Button button = (Button)i.next();
			if (button.getText().equals("OK")) {
				button.getStyleClass().add("primary");
			}
			else if (button.getText().equals("Cancel")) {
				button.getStyleClass().add("warning");
			}
		}
	}

}
