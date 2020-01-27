package application.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;

import javax.imageio.ImageIO;

import application.Main;
import application.model.AlertWithCss;
import application.model.Pixel;
import application.model.Position;
import application.model.PreviewWindow;
import application.model.ToolType;
import application.task.ImageFillByPixel;
import application.task.PaintCanvas;
import application.task.ToRealSizeCanvas;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;

public class MainControl {
	
	@FXML MenuItem saveFileMenu;
	@FXML Canvas canvas;
	@FXML ColorPicker colorPicker;
	@FXML Label indexXLabel;
	@FXML Label indexYLabel;
	@FXML Label positionXLabel;
	@FXML Label positionYLabel;
	@FXML ProgressBar progressBar;
	@FXML Canvas canvasBack;
	@FXML MenuItem exitMenuItem;
	@FXML Button brushButton;
	@FXML Button eraserButton;
	@FXML Button bucketButton;
	
	private Main main;
	private PreviewWindow previewWindow;
	private Stage stage;
	private ToolType tooltype;

	/**
	 * Getter for previewWindow
	 * @return
	 */
	public PreviewWindow getPreviewWindow() {
	    return previewWindow;
	}
	
	/**
	 * Setter for previewWindow
	 * @param previewWindow value to set
	 */
	
	public void setPreviewWindow(PreviewWindow previewWindow) {
	    this.previewWindow = previewWindow;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void initialize() {
		previewWindow = new PreviewWindow(canvas, canvasBack, 64.0, 64.0);
		previewWindow.canvasClean();
		colorPicker.setFocusTraversable(false);
		brushButton.setFocusTraversable(false);
		brushButton.getStyleClass().add("info");
		eraserButton.setFocusTraversable(false);
		eraserButton.getStyleClass().add("danger");
		bucketButton.setFocusTraversable(false);
		bucketButton.getStyleClass().add("warning");
		tooltype = ToolType.BRUSH;
	}
	
	@FXML public void onSaveClicked() {
		
		DirectoryChooser chooser = new DirectoryChooser();
		File defaultDirectory = new File(main.getClass().getClassLoader().getResource("").getPath());
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(stage);
		
		if (selectedDirectory == null) {
			return;
		}
		
		WritableImage imageCanvas = previewWindow.canvasToImage();
		
		ToRealSizeCanvas trsc = new ToRealSizeCanvas(previewWindow, imageCanvas);
		Thread trscThread = new Thread(trsc);
		trscThread.start();
		progressBar.progressProperty().bind(trsc.progressProperty());
		
		try {
			trscThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
		
		WritableImage imageRealCanvas = previewWindow.realCanvasToImage();
		File file = new File(selectedDirectory + "/image_" + new Date().getTime() + ".png");
		
		try {
	        ImageIO.write(SwingFXUtils.fromFXImage(imageRealCanvas, null), "png", file);
	    } catch (IOException e) {
	        System.out.println(e);
	    }
		
		AlertWithCss alert = new AlertWithCss(AlertType.INFORMATION, main);
		alert.setTitle("Saved");
		alert.setContentText("Your image has been saved in the selected folder: " + selectedDirectory);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			progressBar.progressProperty().unbind();
			progressBar.setProgress(0);
		}
	}
	
	@FXML public void onImportClicked() {
		if (!main.showImportImageWindow()) {
			AlertWithCss alert = new AlertWithCss(AlertType.ERROR, main);
			alert.setTitle("Error");
			alert.setContentText("Can not open window.");
			alert.show();
		}
		//this.stage.getIcons().add(new Image(getClass().getClassLoader().getResource("resources/icon/pixel.png").toString()));
	}
	
	@FXML public void onExitClicked() {
		exitMenuItem.setOnAction(null);
		AlertWithCss alert = new AlertWithCss(AlertType.CONFIRMATION, main);
		alert.setTitle("Exit Confirmation");
		alert.setContentText("Are you sure to exit the Sprite Editor?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			this.stage.close();
			System.exit(0);
		} else {
			alert.close();
		}
	}
	
	@FXML public void onCleanCanvasClicked() {
		previewWindow.canvasClean();
	}
	
	@FXML public void onChangeSizeClicked() {
		if (!main.showChangeSizeWindow()) {
			AlertWithCss alert = new AlertWithCss(AlertType.ERROR, main);
			alert.setTitle("Error");
			alert.setContentText("Can not open window.");
			alert.show();
		}
	}
	
	@FXML public void onGitlabClicked() {
		try {
			Desktop.getDesktop().browse(new URI("https://projects.cs.nott.ac.uk/psyby2/CW2-SpaceInvaders"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@FXML public void onMouseClickedCanvas(MouseEvent me) {
		Pixel pixel = previewWindow.getPixelByPosition(new Position(me.getX(), me.getY()));
		printIndexToLabel(pixel);
		doTool(pixel, MouseEvent.MOUSE_CLICKED);
	}

	@FXML public void onMouseDraggedCanvas(MouseEvent me) {
		if (0 < me.getX() && me.getX() < canvas.getWidth() && 0 < me.getY() &&  me.getY() < canvas.getHeight()) {
			Pixel pixel = previewWindow.getPixelByPosition(new Position(me.getX(), me.getY()));
			printIndexToLabel(pixel);
			printPositionToLabel(me);
			doTool(pixel, MouseEvent.MOUSE_DRAGGED);
		}
	}
	
	@FXML public void onMouseMovedCanvas(MouseEvent me) {
		printPositionToLabel(me);
	}
	
	@FXML public void onColorSelected() {
		
	}
	
	@FXML public void onUseBrush() {
		tooltype = ToolType.BRUSH;
	}
	
	@FXML public void onUseEraser() {
		tooltype = ToolType.ERASER;
	}
	
	@FXML public void onUseBucket() {
		tooltype = ToolType.BUCKET;
	}
	
	public void doTool(Pixel pixel, EventType<MouseEvent> mouseEventType) {
		if (tooltype.equals(ToolType.BRUSH)) {
			setColorPickerValueToPixel(pixel, colorPicker.getValue());
		}
		else if (tooltype.equals(ToolType.ERASER)) {
			setEmptyColorToPixel(pixel);
		}
		else if (tooltype.equals(ToolType.BUCKET)) {
			if (mouseEventType.equals(MouseEvent.MOUSE_CLICKED)) {
				doPaint(pixel, colorPicker.getValue());
			}
		}
	}

	public void setColorPickerValueToPixel(Pixel pixel, Color color) {
		setColorToPixel(pixel, color);
	}
	
	public void setEmptyColorToPixel(Pixel pixel) {
		setColorToPixel(pixel, Color.TRANSPARENT);
	}
	
	public void doPaint(Pixel pixel, Color color) {
		WritableImage imageCanvas = previewWindow.canvasToImage();
		PaintCanvas pc = new PaintCanvas(pixel, color, previewWindow, imageCanvas);
		new Thread(pc).start();
	}
	
	public void setColorToPixel(Pixel pixel, Color color) {
		if (!pixel.getColor().equals(color)) {
			ImageFillByPixel ifbp = new ImageFillByPixel(pixel, color, previewWindow);
			new Thread(ifbp).start();
		}
	}
	
	public void printPositionToLabel(MouseEvent me) {
		positionXLabel.setText(String.format("%.2f", (new Double(me.getX()) / previewWindow.getCanvas().getWidth() * 100)) + "%");
		positionYLabel.setText(String.format("%.2f", (new Double(me.getY()) / previewWindow.getCanvas().getHeight() * 100)) + "%");
	}
	
	public void printIndexToLabel(Pixel pixel) {
		indexXLabel.setText(pixel.getIndexX().toString());
		indexYLabel.setText(pixel.getIndexY().toString());
	}

}
