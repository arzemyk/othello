package model;

import ui.UIThread;

public class Board {
	private GameState[][] board;
	private UIThread ui;

	public int humanScore = 2;
	public int computerScore = 2;
	
	public Board() {
		board = new GameState[8][];
		for (int i = 0; i < 8; i++) {
			board[i] = new GameState[8];
			for (int j = 0; j < 8; j++) {
				board[i][j] = GameState.NONE;
				if ((i == 3 || i == 4) && (j == 3 || j == 4)) {
					board[i][j] = ((i == j) ? GameState.HUMAN
							: GameState.COMPUTER);
				}
			}
		}
	}

	public void makeMove(final int row, final int col, final GameState color) {
		isLegalMove(row, col, color, true);
		board[row][col] = color;
		
		countScores();
	}

	public boolean isLegalMove(int row, int col, GameState color,
			boolean makeFlip) {
		boolean legal = false;
		int steps = 0;
		int i, j;

		if (board[row][col] == GameState.NONE) {
			for (int xdir = -1; xdir <= 1; xdir++) {
				for (int ydir = -1; ydir <= 1; ydir++) {
					steps = 0;
					do {
						steps++;
						i = row + steps * ydir;
						j = col + steps * xdir;

					} while ((i >= 0) && (i < 8) && (j >= 0) && (j < 8)
							&& (board[i][j] == getOtherColor(color)));

					if ((i >= 0) && (i < 8) && (j >= 0) && (j < 8) && (steps > 1)
							&& board[i][j] == color) {
						legal = true;
						if (makeFlip) {
							for (int k = 1; k < steps; k++)
								board[row + ydir * k][col + xdir * k] = color;
						}
					}
				}
			}
		}

		return legal;
	}

	public GameState getOtherColor(GameState color) {
		return (color == GameState.COMPUTER) ? GameState.HUMAN
				: GameState.COMPUTER;
	}

	public void setUI(UIThread ui) {
		this.ui = ui;
	}

	public UIThread getUI() {
		return ui;
	}

	public GameState[][] getBoardTable() {
		return board;
	}
	
	public void countScores() {
		computerScore = 0;
		humanScore = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == GameState.COMPUTER) {
					computerScore++;
				} else if (board[i][j] == GameState.HUMAN) {
					humanScore++;
				}
			}
		}	
	}
}
