package main;

import model.EvolutionaryPlayer;
import model.Game;
import model.GameState;
import model.PlayerColor;
import model.RandomPlayer;

public class GamePlay {

	private final static int NUMBER_OF_GAMES = 10;

	public static void main(String[] args) throws InterruptedException {

		int blackWon = 0;
		int whiteWon = 0;
		int draws = 0;
		
		for (int i = 0; i < NUMBER_OF_GAMES; i++) {
			Game game = new Game();

			// final UI ui = new UI(game.getBoard());
			// game.getBoard().setUI(ui);

			EvolutionaryPlayer evolutionaryPlayer = new EvolutionaryPlayer(
					game.getBoard(), PlayerColor.BLACK, new double[] {1.0, 2.0});
			EvolutionaryPlayer evolutionaryPlayer2 = new EvolutionaryPlayer(
					game.getBoard(), PlayerColor.WHITE, new double[] {1.0, 2.0});
			RandomPlayer randomPlayer = new RandomPlayer(game.getBoard(),
					PlayerColor.WHITE);
			RandomPlayer randomPlayer2 = new RandomPlayer(game.getBoard(),
					PlayerColor.BLACK);

			
			
			GameState result = game.run(randomPlayer2, evolutionaryPlayer);
			switch (result) {
			case BLACK_WON:
				blackWon++;
				break;
			case WHITE_WON:
				whiteWon++;
				break;
			case DRAW:
				draws++;
				break;
			default:
				break;
			}
			
			System.out.println(String.format("%d: %s", i, result.toString()));
		}

		System.out.println(String.format(
				"games: %d, black: %d, white: %d, draws: %s", NUMBER_OF_GAMES,
				blackWon, whiteWon, draws));

	}
}
