package model;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ui.UI;
import ai.strategy.PiecesNumberStrategy;

public class Board {
	private final static Logger LOGGER = Logger
			.getLogger(Board.class.getName());

	public static final int BOARD_SIZE = 8;
	private PlayerColor[][] boardTable;
	private UI ui;

	private int blackScore;
	private int whiteScore;

	private PlayerColor currentPlayerColor;
	private GameState gameState;

	private List<Move> legalMoves = new LinkedList<>();

	public Board() {
		LOGGER.setLevel(Level.OFF);
		initializeBoard();
	}
	
	public Board(Board board) {		
		
		boardTable = new PlayerColor[BOARD_SIZE][];
		for (int col = 0; col < BOARD_SIZE; col++) {
			boardTable[col] = board.getBoardTable()[col].clone();
		}
		
		currentPlayerColor = board.getCurrentPlayerColor();
		gameState = board.getGameState();
		legalMoves.addAll(board.getLegalMoves());
		ui = board.getUI();
		
		blackScore = board.getBlackScores();
		whiteScore = board.getWhiteScores();
	}
	
	public Board clone() {
		return new Board(this);
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

		LOGGER.info(String.format("PiecesNumber for %s: %f",
				currentPlayerColor,
				new PiecesNumberStrategy().rankBoard(this, PlayerColor.BLACK)));

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
		return boardTable;
	}

	public int getBlackScores() {
		return blackScore;
	}

	public int getWhiteScores() {
		return whiteScore;
	}
	
	public int getPiecesNumber() {
		return blackScore + whiteScore;
	}

	public GameState getGameState() {
		return gameState;
	}

	private boolean isMoveLegal(int row, int col) {
		boolean legal = false;
		int steps;
		int i, j;

		if (boardTable[row][col] != null || row < 0 || row >= BOARD_SIZE || col < 0
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
						&& (boardTable[i][j] == getOtherPlayerColor(currentPlayerColor)));

				if ((i >= 0) && (i < BOARD_SIZE) && (j >= 0)
						&& (j < BOARD_SIZE) && (steps > 1)
						&& boardTable[i][j] == currentPlayerColor) {
					legal = true;
				}
			}
		}

		return legal;
	}
	
	private void finishGame() {
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
	}

	private void refreshGameState() {
		refreshScores();

		if (blackScore + whiteScore == BOARD_SIZE * BOARD_SIZE) {
			finishGame();			
			return;
		}

		currentPlayerColor = getOtherPlayerColor(currentPlayerColor);
		refreshLegalMoves();
		if (legalMoves.size() == 0) {
			LOGGER.info(String.format("No legal moves for: %s",
					currentPlayerColor));

			currentPlayerColor = getOtherPlayerColor(currentPlayerColor);
			refreshLegalMoves();
			
			if (legalMoves.size() == 0) {
				LOGGER.info(String.format("No legal moves for: %s. Game ends.",
						currentPlayerColor));

				finishGame();
				return;
			}
		}

		if (ui != null) {
			ui.update();
		}
	}

	private void initializeBoard() {
		boardTable = new PlayerColor[BOARD_SIZE][];
		for (int row = 0; row < BOARD_SIZE; row++) {
			boardTable[row] = new PlayerColor[BOARD_SIZE];
			for (int col = 0; col < BOARD_SIZE; col++) {
				boardTable[row][col] = null;
			}
		}

		boardTable[3][3] = PlayerColor.WHITE;
		boardTable[4][4] = PlayerColor.WHITE;

		boardTable[3][4] = PlayerColor.BLACK;
		boardTable[4][3] = PlayerColor.BLACK;

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
				if (boardTable[row][col] != null) {
					if (boardTable[row][col] == PlayerColor.BLACK) {
						blackScore++;
					} else {
						whiteScore++;
					}
				}
			}
		}
	}

	private void makeFlip(int row, int col) {

		if (boardTable[row][col] != null || row < 0 || row >= BOARD_SIZE || col < 0
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
						&& (boardTable[i][j] == getOtherPlayerColor(currentPlayerColor)));

				if ((i >= 0) && (i < BOARD_SIZE) && (j >= 0)
						&& (j < BOARD_SIZE) && (steps > 1)
						&& boardTable[i][j] == getCurrentPlayerColor()) {

					for (int k = 1; k < steps; k++)
						boardTable[row + ydir * k][col + xdir * k] = currentPlayerColor;
				}
			}
		}

		boardTable[row][col] = getCurrentPlayerColor();
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
