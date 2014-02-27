package ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.swt.graphics.Color;

public class BoardSquare extends RectangleFigure {
	private final int column;
	private final int row;
	private Ellipse piece = null;
	
	public BoardSquare(int row, int column) {
		setSize(50, 50);
		this.row = row;
		this.column = column;
		setBackgroundColor(ColorConstants.darkGreen);
	}
	
	public void placePiece(Color color) {
		piece = new Ellipse();
		piece.setSize(50, 50);
		piece.setBackgroundColor(color);
		piece.setForegroundColor(ColorConstants.black);
		piece.setLocation(getLocation());
		add(piece);
	}
	
	public void toggleColor() {
		Color currentColor = getPieceColor();
		piece.setBackgroundColor((currentColor == ColorConstants.black) ? ColorConstants.white : ColorConstants.black);
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getRow() {
		return row;
	}
	
	public boolean isPiecePlaced() {
		return piece != null;
	}
	
	public Color getPieceColor() {
		return piece.getBackgroundColor();
	}
}
