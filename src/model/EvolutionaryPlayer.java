package model;

import ai.MinimaxAlgorithm;

public class EvolutionaryPlayer extends Player {

	private final static int LOOK_AHEAD_LIMIT = 5;
	
	private MinimaxAlgorithm minimax = new MinimaxAlgorithm();
	
	public EvolutionaryPlayer(Board board, PlayerColor playerColor) {
		super(board, playerColor);
	}
	
	public void setStrategyWeights(double weights[]) {
		minimax.setStrategyWeights(weights);
	}

	@Override
	public Move getNextMove() {
		return minimax.getBestMove(board, playerColor, LOOK_AHEAD_LIMIT);
	}

}
