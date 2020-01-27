package application.task;

import application.model.Pixel;
import application.model.PreviewWindow;
import javafx.concurrent.Task;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class ImageFillByPixel extends Task<Integer> {
	
	private Pixel pixel;
	private Color color;
	private PreviewWindow previewWindow;
	
	public ImageFillByPixel(Pixel pixel, Color color, PreviewWindow previewWindow) {
		this.pixel = pixel;
		this.color = color;
		this.previewWindow = previewWindow;
	}

	@Override
	protected Integer call() throws Exception {
		fill();
		return null;
	}
	
	public void fill() {
		PixelWriter imageWriter = previewWindow.getCanvas().getGraphicsContext2D().getPixelWriter();
		Integer rectangleWidth = (int) Math.floor(previewWindow.getCanvas().getWidth() / previewWindow.getImageWidth());
		Integer rectangleHeight = (int) Math.floor(previewWindow.getCanvas().getHeight() / previewWindow.getImageHeight());
		
		for (int i = 0; i < rectangleWidth; i++) {
			for (int j = 0; j < rectangleHeight; j++) {
				imageWriter.setColor(pixel.getIndexX() * rectangleWidth + i, pixel.getIndexY() * rectangleHeight + j, color);
			}
		}
	}

}
