package model;

import ui.UI;

public class HumanPlayer extends Player {

	private UI ui;
	
	public HumanPlayer(Board board, PlayerColor playerColor) {
		super(board, playerColor);
	}

	@Override
	public Move getNextMove() {
		return board.getUI().getMove(playerColor);
	}


}
