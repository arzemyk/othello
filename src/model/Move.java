package model;

public class Move {

	private int row;
	private int col;
	
	public Move(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Move)) {
			return false;
		}
		
		Move otherMove = (Move) obj;
		
		return (getRow() == otherMove.getRow() && getCol() == otherMove.getCol());
	}
	
	@Override
	public String toString() {
		return String.format("%d %d", row, col);
	}
}
