package model;

public class HumanPlayer extends Player {	
	public HumanPlayer(Board board, PlayerColor playerColor) {
		super(board, playerColor);
	}

	@Override
	public Move getNextMove() {
		return board.getUI().getMove(playerColor);
	}


}
