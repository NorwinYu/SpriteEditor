package application.model;

public class Position {
	
	private Double pX;
	private Double pY;

	public Position() {
		
	}
	
	public Position(Double pX, Double pY) {
		this.pX = pX;
		this.pY = pY;
	}
	
	/**
	 * Getter for pX
	 * @return
	 */
	public Double getPX() {
	    return pX;
	}
	
	/**
	 * Setter for pX
	 * @param pX value to set
	 */
	
	public void setPX(Double pX) {
	    this.pX = pX;
	}
	
	/**
	 * Getter for pY
	 * @return
	 */
	public Double getPY() {
	    return pY;
	}
	
	/**
	 * Setter for pY
	 * @param pY value to set
	 */
	
	public void setPY(Double pY) {
	    this.pY = pY;
	}

}
