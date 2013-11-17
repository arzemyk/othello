package ai.strategy;

import model.Board;
import model.PlayerColor;
import ai.IStrategy;

public class MobilityStrategy implements IStrategy {

	@Override
	public double rankBoard(Board board, PlayerColor player) {
		
		int playerLegalMoves = board.getLegalMoves().size();
		int opponentLegalMoves = 0;
		
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			for (int col = 0; col < Board.BOARD_SIZE; col++) {				
				if (board.isMoveLegal(row, col, Board.getOtherPlayerColor(player))) {
					opponentLegalMoves++;
				}
			}
		}
		
		return ((double)playerLegalMoves / (playerLegalMoves + opponentLegalMoves)) * 2.0 - 1.0;
	}

}
