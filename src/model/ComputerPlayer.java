package model;

public class ComputerPlayer extends Player{

	public ComputerPlayer(Board board) {
		super(board);
	}

	private int i = 0;
	@Override
	public void makeMove() {
		board.makeMove(i, 0, Game.state);
		board.getUI().update(i, 0, Game.state);
		i++;
	}

}
