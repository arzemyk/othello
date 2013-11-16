package ai.strategy;

import model.Board;
import model.PlayerColor;
import ai.IStrategy;

public class PiecesNumberStrategy implements IStrategy {

	@Override
	public double rankBoard(Board board, PlayerColor playerColor) {

		int playerPieces = playerColor == PlayerColor.BLACK ? board.getBlackScores() : board.getWhiteScores();

		double fraction = (double)playerPieces/board.getPiecesNumber();
		
		return fraction * 2.0 - 1.0;
	}
}
