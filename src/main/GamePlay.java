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
					game.getBoard(), PlayerColor.BLACK);
			RandomPlayer randomPlayer = new RandomPlayer(game.getBoard(),
					PlayerColor.WHITE);

			GameState result = game.run(evolutionaryPlayer, randomPlayer);
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
