package application.model;

import javafx.scene.paint.Color;

public class Pixel {
	
	private Integer indexX;
	private Integer indexY;
	private Color color;

	public Pixel() {
		
	}
	
	public Pixel(Integer indexX, Integer indexY) {
		this.indexX = indexX;
		this.indexY = indexY;
	}
	
	public Pixel(Integer indexX, Integer indexY, Color color) {
		this.indexX = indexX;
		this.indexY = indexY;
		this.color = color;
	}
	
	/**
	 * Getter for indexX
	 * @return
	 */
	public Integer getIndexX() {
	    return indexX;
	}
	
	/**
	 * Setter for indexX
	 * @param indexX value to set
	 */
	
	public void setIndexX(Integer indexX) {
	    this.indexX = indexX;
	}
	
	/**
	 * Getter for indexY
	 * @return
	 */
	public Integer getIndexY() {
	    return indexY;
	}
	
	/**
	 * Setter for indexY
	 * @param indexY value to set
	 */
	
	public void setIndexY(Integer indexY) {
	    this.indexY = indexY;
	}

	/**
	 * Getter for color
	 * @return
	 */
	public Color getColor() {
	    return color;
	}
	
	/**
	 * Setter for color
	 * @param color value to set
	 */
	
	public void setColor(Color color) {
	    this.color = color;
	}
	
	
}
