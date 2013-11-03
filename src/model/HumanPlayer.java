package model;

public class HumanPlayer extends Player {

	public HumanPlayer(Board board) {
		super(board);
	}

	@Override
	public void makeMove() {
		
	}

	public void makeMove(int row, int col) {
		Game.lock.lock();
		board.makeMove(row, col, GameState.HUMAN);
		Game.computerMove.signal();
		Game.state = GameState.COMPUTER;
		Game.lock.unlock();
	}
}