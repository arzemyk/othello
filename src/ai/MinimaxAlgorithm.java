package ai;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Board;
import model.Move;
import model.PlayerColor;

public class MinimaxAlgorithm {

	private final static Logger LOGGER = Logger
			.getLogger(MinimaxAlgorithm.class.getName());
	
	class StrategyInfo {
		public StrategyInfo(IStrategy strategy, double weight) {
			this.strategy = strategy;
			this.weight = weight;
		}
		
		public IStrategy strategy;

		public double weight;
	}
	
	class BestMove {
		public double rank;
		
		public Move move;
	}

	private List<StrategyInfo> strategies = new LinkedList<>();

	public MinimaxAlgorithm() {
		LOGGER.setLevel(Level.OFF);
	}
	
	public void addStrategy(IStrategy strategy, double weight) {
		strategies.add(new StrategyInfo(strategy, weight));
	}

	public void clearStrategies() {
		strategies.clear();
	}
	
	public Move getBestMove(Board board, PlayerColor initialPlayer, int lookAhead) {
		if (board.getLegalMoves().size() == 0) {
			return null;
		}
		
		Move bestMove = null;
		double bestRank = Integer.MIN_VALUE;
		double rank;
		
		for (Move legalMove : board.getLegalMoves()) {
			Board newBoard = board.clone();
			newBoard.makeMove(legalMove);

			rank = rankNode(newBoard, lookAhead, bestRank, Integer.MAX_VALUE,
					initialPlayer);

			if (rank > bestRank) {
				bestRank = rank;
				bestMove = legalMove;
			}
		}
		
		LOGGER.info("Returning best move");
		return bestMove;
	}

	public double rankNode(Board board, int lookAhead, double alpha,
			double beta, PlayerColor initialPlayer) {

		LOGGER.info(String.format("lookAhead: %d, alpha: %f , beta: %f", lookAhead, alpha, beta	));
		
		switch (board.getGameState()) {

		case DRAW:
			return 0;

		case WHITE_WON:
			if (initialPlayer == PlayerColor.WHITE) {
				return Double.MAX_VALUE;
			} else {
				return Double.MIN_VALUE;
			}

		case BLACK_WON:
			if (initialPlayer == PlayerColor.BLACK) {
				return Double.MAX_VALUE;
			} else {
				return Double.MIN_VALUE;
			}

		case RUNNING:
			break;
		}

		double rank = 0;

		if (lookAhead == 0) {
			for (StrategyInfo strategyInfo : strategies) {
				rank += strategyInfo.weight
						* strategyInfo.strategy.rankBoard(board,
								board.getCurrentPlayerColor());
			}

			return rank;
		}

		for (Move legalMove : board.getLegalMoves()) {
			Board newBoard = board.clone();
			newBoard.setUI(null);
			newBoard.makeMove(legalMove);			

			rank = rankNode(newBoard, lookAhead - 1, alpha, beta,
					initialPlayer);

			if (isMaximizing(board, initialPlayer)) {
				if (rank > alpha) {
					alpha = rank;
				}
			} else {
				if (rank < beta) {
					beta = rank;
				}
			}

			if (alpha >= beta) {
				return rank;
			}
		}

		if (isMaximizing(board, initialPlayer)) {
			return alpha;
		} else {
			return beta;
		}
	}

	private boolean isMaximizing(Board board, PlayerColor initialPlayer) {
		return board.getCurrentPlayerColor() == initialPlayer;
	}
	
}