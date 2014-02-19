package ai.strategy;

import model.Board;
import model.PlayerColor;
import ai.IStrategy;

public class NearEdgesStrategy implements IStrategy {

	@Override
	public double rankBoard(Board board, PlayerColor playerColor) {
		int edgesMy = 0;
		int edgesOpponent = 0;

		for (int i = 1; i < Board.BOARD_SIZE - 1; i++) {
			for (int j = 1; j < Board.BOARD_SIZE; j += Board.BOARD_SIZE - 3) {
				if (board.getBoardTable()[i][j] == playerColor) {
					edgesMy++;
				} else if (board.getBoardTable()[i][j] != null) {
					edgesOpponent++;
				}
			}
		}

		for (int j = 2; j < Board.BOARD_SIZE - 1; j++) {
			for (int i = 1; i < Board.BOARD_SIZE; i += Board.BOARD_SIZE - 3) {
				if (board.getBoardTable()[i][j] == playerColor) {
					edgesMy++;
				} else if (board.getBoardTable()[i][j] != null) {
					edgesOpponent++;
				}
			}
		}

		int edgesTaken = edgesMy + edgesOpponent;

		double fraction = (edgesTaken != 0) ? (double) edgesMy / (edgesTaken)
				: 0.5;

		return fraction * 2.0 - 1.0;
	}
}
