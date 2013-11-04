package main;

import model.Game;
import ui.UIThread;

public class GamePlay {
	
	public static void main(String[] args) throws InterruptedException {
		Game game = new Game();
		
		final UIThread ui = new UIThread();
		ui.start();
		ui.setBoard(game.getBoard());
		
		Thread.sleep(3000);
		
		game.getBoard().setUI(ui);
		game.run();
	}
}
