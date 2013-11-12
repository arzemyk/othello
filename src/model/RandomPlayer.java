package model;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player {

	private Random random = new Random();
	
	public RandomPlayer(Board board, PlayerColor playerColor) {
		super(board, playerColor);
	}

	@Override
	public Move getNextMove() {
		List<Move> legalMoves = board.getLegalMoves();
		
		return legalMoves.get(random.nextInt(legalMoves.size()));
		
	}

}
