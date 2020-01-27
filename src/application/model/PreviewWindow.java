package application.model;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PreviewWindow {
	
	private Canvas canvas;
	private Canvas canvasBack;
	private Canvas realCanvas;
	private Double imageWidth;
	private Double imageHeight;

	public PreviewWindow() {
		
	}

	public PreviewWindow(Canvas canvas, Canvas canvasBack, Double imageWidth, Double imageHeight) {
		this.canvas = canvas;
		this.canvasBack = canvasBack;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}

	/**
	 * Getter for canvas
	 * @return
	 */
	public Canvas getCanvas() {
	    return canvas;
	}
	
	/**
	 * Setter for canvas
	 * @param canvas value to set
	 */
	
	public void setCanvas(Canvas canvas) {
	    this.canvas = canvas;
	}
	
	/**
	 * Getter for canvasBack
	 * @return
	 */
	public Canvas getCanvasBack() {
	    return canvasBack;
	}
	
	/**
	 * Setter for canvasBack
	 * @param canvasBack value to set
	 */
	
	public void setCanvasBack(Canvas canvasBack) {
	    this.canvasBack = canvasBack;
	}

	/**
	 * Getter for realCanvas
	 * @return
	 */
	public Canvas getRealCanvas() {
	    return realCanvas;
	}
	
	/**
	 * Setter for realCanvas
	 * @param realCanvas value to set
	 */
	
	public void setRealCanvas(Canvas realCanvas) {
	    this.realCanvas = realCanvas;
	}
	

	/**
	 * Getter for imageWidth
	 * @return
	 */
	public Double getImageWidth() {
	    return imageWidth;
	}
	
	/**
	 * Setter for imageWidth
	 * @param imageWidth value to set
	 */
	
	public void setImageWidth(Double imageWidth) {
	    this.imageWidth = imageWidth;
	}
	
	/**
	 * Getter for imageHeight
	 * @return
	 */
	public Double getImageHeight() {
	    return imageHeight;
	}
	
	/**
	 * Setter for imageHeight
	 * @param imageHeight value to set
	 */
	
	public void setImageHeight(Double imageHeight) {
	    this.imageHeight = imageHeight;
	}
	
	public Pixel getPixelByPosition(Position position) {
		Integer indexX = (int) Math.floor(position.getPX() / (canvas.getWidth() / this.getImageWidth()));
		Integer indexY = (int) Math.floor(position.getPY() / (canvas.getHeight() / this.getImageHeight()));
		Color color = getColorByIndex(indexX, indexY);
		return new Pixel(indexX, indexY, color);
	}
	
	public Pixel getPixelByIndex(Integer indexX, Integer indexY) {
		Color color = getColorByIndex(indexX, indexY);
		return new Pixel(indexX, indexY, color);
	}
	
	public Pixel getPixelByIndexWithImage(Integer indexX, Integer indexY, WritableImage image) {
		Color color = getColorByIndexWithImage(indexX, indexY, image);
		return new Pixel(indexX, indexY, color);
	}
	
	public Color getColorByIndex(Integer indexX, Integer indexY) {
		WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
		SnapshotParameters sp = new SnapshotParameters();
	    sp.setFill(Color.TRANSPARENT);
		PixelReader imageReader = canvas.snapshot(sp, image).getPixelReader();
		return imageReader.getColor((int) Math.floor((indexX + 0.5) * (getCanvas().getWidth() / getImageWidth())),
				(int) Math.floor((indexY + 0.5) * (getCanvas().getHeight() / getImageHeight())));
	}
	
	public void canvasClean() {
		canvasBackPrint();
		this.canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public void canvasBackPrint() {
		PixelWriter pixelWriter = this.canvasBack.getGraphicsContext2D().getPixelWriter();
		for (int i = 0; i < canvasBack.getWidth(); i++) {
			for (int j = 0; j < canvasBack.getHeight(); j++) {
				if ((i + j) % 2 == 0)
				{
					pixelWriter.setColor(i, j, Color.web("#ffffff"));
				}
				else
				{
					pixelWriter.setColor(i, j, Color.web("#cccccc"));
				}
			}
		}
	}
	
	public WritableImage realCanvasToImage() {
		SnapshotParameters sp = new SnapshotParameters();
	    sp.setFill(Color.TRANSPARENT);
		WritableImage image = realCanvas.snapshot(sp, null);
		return image;
	}
	
	public WritableImage canvasToImage() {
		WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
		SnapshotParameters sp = new SnapshotParameters();
	    sp.setFill(Color.TRANSPARENT);
	    return canvas.snapshot(sp, image);
	}
	
	public Color getColorByIndexWithImage(Integer indexX, Integer indexY, WritableImage image) {
		PixelReader imageReader = image.getPixelReader();
		return imageReader.getColor((int) Math.floor((indexX + 0.5) * (getCanvas().getWidth() / getImageWidth())),
				(int) Math.floor((indexY + 0.5) * (getCanvas().getHeight() / getImageHeight())));
	}
}
