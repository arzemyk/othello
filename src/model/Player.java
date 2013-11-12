package model;

public abstract class Player {
	protected final Board board;
	protected final PlayerColor playerColor;
	
	public Player(Board board, PlayerColor playerColor) {
		this.board = board;
		this.playerColor = playerColor;
	}
	
	public PlayerColor getPlayerColor() {
		return playerColor;
	}

	public abstract Move getNextMove();
}
