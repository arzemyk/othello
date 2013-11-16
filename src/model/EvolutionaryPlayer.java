package model;

import ai.MinimaxAlgorithm;
import ai.strategy.PiecesNumberStrategy;

public class EvolutionaryPlayer extends Player {

	private final static int LOOK_AHEAD_LIMIT = 5;
	
	private MinimaxAlgorithm minimax = new MinimaxAlgorithm();
	
	public EvolutionaryPlayer(Board board, PlayerColor playerColor) {
		super(board, playerColor);
		
		minimax.addStrategy(new PiecesNumberStrategy(), 1.0);
	}

	@Override
	public Move getNextMove() {
		return minimax.getBestMove(board, playerColor, LOOK_AHEAD_LIMIT);
	}

}
