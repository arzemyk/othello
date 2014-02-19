package ai.strategy;

import model.Board;
import model.PlayerColor;
import ai.IStrategy;

public class NearCornerStrategy implements IStrategy {

	@Override
	public double rankBoard(Board board, PlayerColor playerColor) {
		int nearCornersMy = 0;
		int nearCornersOpponent = 0;

		for (int i = 1; i < Board.BOARD_SIZE; i += Board.BOARD_SIZE - 3) {
			for (int j = 0; j < Board.BOARD_SIZE; j += Board.BOARD_SIZE - 1) {
				if (board.getBoardTable()[i][j] == playerColor) {
					nearCornersMy++;
				} else if (board.getBoardTable()[i][j] != null) {
					nearCornersOpponent++;
				}
			}
		}

		for (int j = 1; j < Board.BOARD_SIZE; j += Board.BOARD_SIZE - 3) {
			for (int i = 0; i < Board.BOARD_SIZE; i += Board.BOARD_SIZE - 1) {
				if (board.getBoardTable()[i][j] == playerColor) {
					nearCornersMy++;
				} else if (board.getBoardTable()[i][j] != null) {
					nearCornersOpponent++;
				}
			}
		}

		int nearCornersTaken = nearCornersMy + nearCornersOpponent;

		double fraction = (nearCornersTaken != 0) ? (double) nearCornersMy
				/ (nearCornersTaken) : 0.5;

		return fraction * 2.0 - 1.0;
	}
}
