package model;

import ui.UIThread;


public class Board {
	private GameState[][] board;
	private UIThread ui;
	
	public Board() {
		board = new GameState[8][];
		for (int i = 0; i < 8; i++) {
			board[i] = new GameState[8];
			for (int j = 0; j < 8; j++) {
				board[i][j] = GameState.NONE;
			}
		}
	}

	public void makeMove(final int row, final int col, final GameState color) {
		board[row][col] = color;
	}
	
	public void setUI(UIThread ui) {
		this.ui = ui;
	}
	
	public UIThread getUI() {
		return ui;
	}
}
