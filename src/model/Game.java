package model;

public class Game {

	private Player blackPlayer;
	private Player whitePlayer;

	private final Board board;

	public Game() {
		board = new Board();
		blackPlayer = new RandomPlayer(board, PlayerColor.BLACK);
		whitePlayer = new RandomPlayer(board, PlayerColor.WHITE);
	}

	public void run() {
		while (board.getGameState() == GameState.RUNNING) {

			Player player;
			if (board.getCurrentPlayerColor() == PlayerColor.BLACK) {
				player = blackPlayer;
			} else {
				player = whitePlayer;
			}

			board.makeMove(player.getNextMove());
		}
	}

	public Board getBoard() {
		return board;
	}
}
