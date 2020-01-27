package application.task;

import application.model.PreviewWindow;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ToRealSizeCanvas extends Task<Integer>{
	
	private PreviewWindow previewWindow;
	private WritableImage image;
	
	public ToRealSizeCanvas(PreviewWindow previewWindow, WritableImage image) {
		this.previewWindow = previewWindow;
		this.image = image;
	}
	
	@Override
	protected Integer call() throws Exception {
		
		Integer imageWidth = (int) Math.floor(previewWindow.getImageWidth());
		Integer imageHeight = (int) Math.floor(previewWindow.getImageHeight());
		int pixelNumber = imageWidth * imageHeight;
		
		Canvas canvas = new Canvas(imageWidth, imageHeight);
		PixelWriter imageWriter = canvas.getGraphicsContext2D().getPixelWriter();
		
		for (int i = 0; i < imageWidth; i++) {
			for (int j = 0; j < imageHeight; j++) {
				if (!previewWindow.getColorByIndexWithImage(i, j, image).equals(Color.TRANSPARENT)) {
					imageWriter.setColor(i, j, previewWindow.getColorByIndexWithImage(i, j, image));
				}
				updateProgress(i * imageHeight + j + 1, pixelNumber);
			}
		}
		
		previewWindow.setRealCanvas(canvas);
		
		return null;
	}

}
