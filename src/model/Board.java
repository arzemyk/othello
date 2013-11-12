package model;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import ui.UI;

public class Board {
	private final static Logger LOGGER = Logger
			.getLogger(Board.class.getName());

	private static final int BOARD_SIZE = 8;
	private PlayerColor[][] board;
	private UI ui;

	private int blackScore;
	private int whiteScore;

	private PlayerColor currentPlayerColor;
	private GameState gameState;

	private List<Move> legalMoves = new LinkedList<>();

	public Board() {
		initializeBoard();
	}

	public void makeMove(Move move) {
		if (!isMoveLegal(move.getRow(), move.getCol())) {
			throw new IllegalStateException(String.format(
					"This move is illegal: %s, %s ", currentPlayerColor,
					move.toString()));
		}

		LOGGER.info(String.format("Making move: %s, %s", currentPlayerColor,
				move));
		makeFlip(move.getRow(), move.getCol());

		if (ui != null) {
			ui.update();
		}

		refreshGameState();

	}

	public List<Move> getLegalMoves() {
		return legalMoves;
	}

	public PlayerColor getCurrentPlayerColor() {
		return currentPlayerColor;
	}

	public void setUI(UI ui) {
		this.ui = ui;
	}

	public UI getUI() {
		return ui;
	}

	public PlayerColor[][] getBoardTable() {
		return board;
	}

	public int getBlackScores() {
		return blackScore;
	}

	public int getWhiteScores() {
		return whiteScore;
	}

	public GameState getGameState() {
		return gameState;
	}

	private boolean isMoveLegal(int row, int col) {
		boolean legal = false;
		int steps;
		int i, j;

		if (board[row][col] != null || row < 0 || row >= BOARD_SIZE || col < 0
				|| col >= BOARD_SIZE) {
			return false;
		}

		for (int xdir = -1; xdir <= 1; xdir++) {
			for (int ydir = -1; ydir <= 1; ydir++) {
				steps = 0;
				do {
					steps++;
					i = row + steps * ydir;
					j = col + steps * xdir;

				} while ((i >= 0)
						&& (i < BOARD_SIZE)
						&& (j >= 0)
						&& (j < BOARD_SIZE)
						&& (board[i][j] == getOtherPlayerColor(currentPlayerColor)));

				if ((i >= 0) && (i < BOARD_SIZE) && (j >= 0)
						&& (j < BOARD_SIZE) && (steps > 1)
						&& board[i][j] == currentPlayerColor) {
					legal = true;
				}
			}
		}

		return legal;
	}

	private void refreshGameState() {
		refreshScores();

		if (blackScore + whiteScore == BOARD_SIZE * BOARD_SIZE) {
			if (blackScore > whiteScore) {
				gameState = GameState.BLACK_WON;
				
				LOGGER.info(String.format("Black won"));
			} else if (whiteScore > blackScore) {
				gameState = GameState.WHITE_WON;
				
				LOGGER.info(String.format("White won"));
			} else {
				gameState = GameState.DRAW;
				
				LOGGER.info(String.format("Draw"));
			}

			legalMoves.clear();

			if (ui != null) {
				ui.update();
			}

			return;
		}

		currentPlayerColor = getOtherPlayerColor(currentPlayerColor);
		refreshLegalMoves();
		if (legalMoves.size() == 0) {
			LOGGER.info(String.format("No legal moves for: %s",
					currentPlayerColor));

			currentPlayerColor = getOtherPlayerColor(currentPlayerColor);
			refreshLegalMoves();
		}

		if (ui != null) {
			ui.update();
		}
	}

	private void initializeBoard() {
		board = new PlayerColor[BOARD_SIZE][];
		for (int row = 0; row < BOARD_SIZE; row++) {
			board[row] = new PlayerColor[BOARD_SIZE];
			for (int col = 0; col < BOARD_SIZE; col++) {
				board[row][col] = null;
			}
		}

		board[3][3] = PlayerColor.WHITE;
		board[4][4] = PlayerColor.WHITE;

		board[3][4] = PlayerColor.BLACK;
		board[4][3] = PlayerColor.BLACK;

		currentPlayerColor = PlayerColor.BLACK;

		refreshScores();
		refreshLegalMoves();

		gameState = GameState.RUNNING;
		
		LOGGER.info("Board initialized");
	}

	private void refreshScores() {
		blackScore = 0;
		whiteScore = 0;

		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (board[row][col] != null) {
					if (board[row][col] == PlayerColor.BLACK) {
						blackScore++;
					} else {
						whiteScore++;
					}
				}
			}
		}
	}

	private void makeFlip(int row, int col) {

		if (board[row][col] != null || row < 0 || row >= BOARD_SIZE || col < 0
				|| col >= BOARD_SIZE) {
			throw new IllegalStateException("Cannot flip - illegal move");
		}

		int steps;
		int i, j;

		for (int xdir = -1; xdir <= 1; xdir++) {
			for (int ydir = -1; ydir <= 1; ydir++) {
				steps = 0;
				do {
					steps++;
					i = row + steps * ydir;
					j = col + steps * xdir;

				} while ((i >= 0)
						&& (i < BOARD_SIZE)
						&& (j >= 0)
						&& (j < BOARD_SIZE)
						&& (board[i][j] == getOtherPlayerColor(currentPlayerColor)));

				if ((i >= 0) && (i < BOARD_SIZE) && (j >= 0)
						&& (j < BOARD_SIZE) && (steps > 1)
						&& board[i][j] == getCurrentPlayerColor()) {

					for (int k = 1; k < steps; k++)
						board[row + ydir * k][col + xdir * k] = currentPlayerColor;
				}
			}
		}

		board[row][col] = getCurrentPlayerColor();
	}

	private void refreshLegalMoves() {
		legalMoves.clear();

		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (isMoveLegal(row, col)) {
					legalMoves.add(new Move(row, col));
				}
			}
		}
	}

	private PlayerColor getOtherPlayerColor(PlayerColor color) {
		return color == PlayerColor.WHITE ? PlayerColor.BLACK
				: PlayerColor.WHITE;
	}

}
