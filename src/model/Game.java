package model;


public class Game {

	private Player blackPlayer;
	private Player whitePlayer;

	private final Board board;

	public Game() {
		board = new Board();
		
		if (board.getUI() != null) {
			board.getUI().start();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public GameState run(Player blackPlayer, Player whitePlayer) {
		while (board.getGameState() == GameState.RUNNING) {

			Player player;
			if (board.getCurrentPlayerColor() == PlayerColor.BLACK) {
				player = blackPlayer;
			} else {
				player = whitePlayer;
			}

			board.makeMove(player.getNextMove());
		}

		return board.getGameState();		
	}

	public Board getBoard() {
		return board;
	}
}
