package ai.evolution;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import model.EvolutionaryPlayer;
import model.Game;
import model.GameState;
import model.PlayerColor;

public class EvolutionThread implements Callable<Map<Integer, Integer>> {
	private final static Logger LOGGER = Logger
			.getLogger(EvolutionThread.class.getName());

	private final int player1Number;
	private final int player2Number;
	private final double[] player1;
	private final double[] player2;
	
	
	public EvolutionThread(int player1Number, int player2Number, double[] player1, double[] player2) {
		this.player1Number = player1Number;
		this.player2Number = player2Number;
		this.player1 = player1;
		this.player2 = player2;
	}
	
	@Override
	public Map<Integer, Integer> call() throws Exception {
		Map<Integer, Integer> scores = new HashMap<>();
		scores.put(player1Number, 0);
		scores.put(player2Number, 0);
		
		for (int m = 0; m < Evolution.NUMBER_OF_MATCHES; m++) {
			//LOGGER.info(String.format("Playing match %d between %d and %d", m, player1Number, player2Number));
			
			Game game = new Game();
			EvolutionaryPlayer blackPlayer = new EvolutionaryPlayer(
					game.getBoard(), PlayerColor.BLACK,
					player1);
			EvolutionaryPlayer whitePlayer = new EvolutionaryPlayer(
					game.getBoard(), PlayerColor.WHITE,
					player2);

			GameState gameState = game.run(blackPlayer, whitePlayer);

			if (gameState == GameState.BLACK_WON) {
				scores.put(player1Number, scores.get(player1Number) + Evolution.WIN_POINTS);
			} else if (gameState == GameState.WHITE_WON) {
				scores.put(player2Number, scores.get(player2Number) + Evolution.WIN_POINTS);
			} else if (gameState == GameState.DRAW) {
				scores.put(player1Number, scores.get(player1Number) + Evolution.DRAW_POINTS);
				scores.put(player2Number, scores.get(player2Number) + Evolution.DRAW_POINTS);
			}
		}
		
		return scores;
	}
}
