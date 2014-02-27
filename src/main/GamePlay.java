package main;

import model.EvolutionaryPlayer;
import model.Game;
import model.GameState;
import model.HumanPlayer;
import model.PlayerColor;
import model.RandomPlayer;
import ui.UI;

public class GamePlay {

	public static void main(String[] args) throws InterruptedException {

		int blackWon = 0;
		int whiteWon = 0;
		int draws = 0;

		Game game = new Game();

		final UI ui = new UI(game.getBoard());
		game.getBoard().setUI(ui);
		ui.start();
		Thread.sleep(300);

		EvolutionaryPlayer evolutionaryPlayer = new EvolutionaryPlayer(
				game.getBoard(), PlayerColor.WHITE, new double[] {
						3.81975506192257, 7.04157555844609, 3.78556201573816,
						5.62900371063094, 2.41216320342031, 0.40763291350491 });
		EvolutionaryPlayer evolutionaryPlayer2 = new EvolutionaryPlayer(
				game.getBoard(), PlayerColor.WHITE,
				new double[] { 4.19427379017672, 5.01445855756605,
						5.02470676207837, 4.29581425367182, 1.82027234844694,
						-0.517164315234025 });
		RandomPlayer randomPlayer = new RandomPlayer(game.getBoard(),
				PlayerColor.WHITE);
		RandomPlayer randomPlayer2 = new RandomPlayer(game.getBoard(),
				PlayerColor.BLACK);
		HumanPlayer humanPlayer = new HumanPlayer(game.getBoard(),
				PlayerColor.WHITE);
		HumanPlayer humanPlayer2 = new HumanPlayer(game.getBoard(),
				PlayerColor.BLACK);

		GameState result = game.run(humanPlayer, humanPlayer2);
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

		System.out.println(String.format("black: %d, white: %d, draws: %s",
				blackWon, whiteWon, draws));

	}
}
