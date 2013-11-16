package ai;

import model.Board;
import model.PlayerColor;

public interface IStrategy {

	double rankBoard(Board board, PlayerColor player);
	
}
