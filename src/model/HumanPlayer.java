package model;

public class HumanPlayer extends Player {

	public HumanPlayer(Board board) {
		super(board);
	}

	@Override
	public void makeMove() {

	}

	public boolean makeMove(int row, int col) {
		if (board.isLegalMove(row, col, GameState.HUMAN, false)) {
			Game.lock.lock();
			board.makeMove(row, col, GameState.HUMAN);
			Game.computerMove.signal();
			Game.state = GameState.COMPUTER;
			Game.lock.unlock();
			return true;
		}
		return false;
	}
}
