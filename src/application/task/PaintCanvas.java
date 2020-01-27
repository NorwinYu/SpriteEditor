package application.task;

import java.util.Stack;

import application.model.Pixel;
import application.model.PreviewWindow;
import javafx.concurrent.Task;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PaintCanvas extends Task<Integer> {
	
	private Pixel pixel;
	private Color color;
	private Color startColor;
	private PreviewWindow previewWindow;
	private WritableImage imageCanvas;
	private Stack<Pixel> pixelStack;
	private boolean[][] isPainteds; 
	private Integer imageWith;
	private Integer imageHeight;
	
	public PaintCanvas(Pixel pixel, Color color, PreviewWindow previewWindow, WritableImage imageCanvas) {
		this.pixel = pixel;
		this.color = color;
		this.previewWindow = previewWindow;
		this.imageCanvas = imageCanvas;
	}
	
	@Override
	protected Integer call() throws Exception {
		this.imageWith = (int) Math.floor(previewWindow.getImageWidth());
		this.imageHeight = (int) Math.floor(previewWindow.getImageHeight());
		setIsPainteds(new boolean[imageWith][imageHeight]);
		
		pixelStack = new Stack<Pixel>();
		if (pixel.getColor().equals(color)) {
			return null;
		}
		startColor = pixel.getColor();
		pixelStack.push(pixel);
		
		while (!pixelStack.isEmpty()) {
			Pixel pixelPop = pixelStack.pop();
			if (isPainted(pixelPop) || !pixelPop.getColor().equals(startColor)) {
				continue;
			}
			setColorToPixel(pixelPop, color);
			pushOtherPixel(pixelPop);
		}
		
		return null;
	}
	
	public void pushOtherPixel(Pixel pixel) {
		if ((pixel.getIndexY() - 1) >= 0) {
			Pixel pixelDown = previewWindow.getPixelByIndexWithImage(pixel.getIndexX(), pixel.getIndexY() - 1, imageCanvas);
			pixelStack.push(pixelDown);
		}
		if ((pixel.getIndexY() + 1) < previewWindow.getImageHeight()) {
			Pixel pixelUp = previewWindow.getPixelByIndexWithImage(pixel.getIndexX(), pixel.getIndexY() + 1, imageCanvas);
			pixelStack.push(pixelUp);
		}
		if ((pixel.getIndexX() - 1) >= 0) {
			Pixel pixelLeft = previewWindow.getPixelByIndexWithImage(pixel.getIndexX() - 1, pixel.getIndexY(), imageCanvas);
			pixelStack.push(pixelLeft);
		}
		if ((pixel.getIndexX() + 1) < previewWindow.getImageWidth()) {
			Pixel pixelRight = previewWindow.getPixelByIndexWithImage(pixel.getIndexX() + 1, pixel.getIndexY(), imageCanvas);
			pixelStack.push(pixelRight);
		}
	}
	
	public void setColorToPixel(Pixel pixel, Color color) {
		if (!pixel.getColor().equals(color)) {
			new ImageFillByPixel(pixel, color, previewWindow).fill();
		}
		setIsPaintedTrue(pixel);
	}
	
	public boolean isPainted(Pixel pixel) {
		boolean[][] isPainteds = getIsPainteds();
		return isPainteds[pixel.getIndexX()][pixel.getIndexY()];
	}
	
	public void setIsPaintedTrue(Pixel pixel) {
		boolean[][] isPainteds = getIsPainteds();
		isPainteds[pixel.getIndexX()][pixel.getIndexY()] = true;
	}

	public boolean[][] getIsPainteds() {
		return isPainteds;
	}

	public void setIsPainteds(boolean[][] isPainteds) {
		this.isPainteds = isPainteds;
	}
}
