package ai.strategy;

import model.Board;
import model.PlayerColor;
import ai.IStrategy;

public class CornerStrategy implements IStrategy {

	@Override
	public double rankBoard(Board board, PlayerColor playerColor) {

		int cornersMy = 0;
		int cornersOpponent = 0;

		for (int i = 0; i < Board.BOARD_SIZE; i += Board.BOARD_SIZE - 1) {
			for (int j = 0; j < Board.BOARD_SIZE; j += Board.BOARD_SIZE - 1) {
				if (board.getBoardTable()[i][j] == playerColor) {
					cornersMy++;
				} else if (board.getBoardTable()[i][j] != null) {
					cornersOpponent++;
				}
			}
		}
		
		int cornersTaken = cornersMy + cornersOpponent;
		
		double fraction = (cornersTaken != 0) ? (double) cornersMy / (cornersTaken) : 0.5;

		return fraction * 2.0 - 1.0;
	}
}
