package application.task;

import application.model.Pixel;
import application.model.PreviewWindow;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class ImageFillFull extends Task<Integer> {
	
	private PreviewWindow previewWindow;
	private Image image;
	
	public ImageFillFull(PreviewWindow previewWindow, Image image) {
		this.previewWindow = previewWindow;
		this.image = image;
	}
	
	@Override
	protected Integer call() throws Exception {
		PixelReader pixelReader= image.getPixelReader();
		int pixelNumber = (int) (previewWindow.getImageWidth() * previewWindow.getImageHeight());
		
		boolean flag = false;
		for (int i = 0; i < previewWindow.getImageWidth() && !flag; i++) {
			for (int j = 0; j < previewWindow.getImageHeight(); j++) {
				ImageFillByPixel ifbp = new ImageFillByPixel(
						new Pixel(i, j), pixelReader.getColor(i, j), previewWindow);
				ifbp.fill();
				updateProgress(i * previewWindow.getImageHeight() + j + 1, pixelNumber);
				if (isCancelled()) {
					flag = true;
					break;
				}
			}
		}
		
		return null;
	}
}
