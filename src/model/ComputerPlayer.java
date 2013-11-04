package model;

public class ComputerPlayer extends Player{

	public ComputerPlayer(Board board) {
		super(board);
	}

	@Override
	public void makeMove() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.isLegalMove(i, j, GameState.COMPUTER, false)) {
					board.makeMove(i, j, Game.state);
					board.getUI().update();
					return;
				}
			}
		}
	}

}
