package model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game {
	public static GameState state;
	public static HumanPlayer humanPlayer;
	private ComputerPlayer computerPlayer;
	private final Board board;

	final static Lock lock = new ReentrantLock();
	final static Condition computerMove = lock.newCondition();

	public Game() {
		state = GameState.COMPUTER;
		board = new Board();
		computerPlayer = new ComputerPlayer(board);
		humanPlayer = new HumanPlayer(board);
	}

	public void run() {
		while (true) {
			lock.lock();
			if (state == GameState.COMPUTER) {
				computerPlayer.makeMove();
				state = GameState.HUMAN;
			}
			try {
				computerMove.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lock.unlock();
		}
	}

	public Board getBoard() {
		return board;
	}
}
