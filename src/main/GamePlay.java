package main;

import model.Game;
import ui.UI;

public class GamePlay {
	
	public static void main(String[] args) throws InterruptedException {
		Game game = new Game();
		
		final UI ui = new UI(game.getBoard());
		ui.start();
		
		Thread.sleep(500);
		
		game.getBoard().setUI(ui);
		game.run();
	}
}
