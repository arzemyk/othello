package ai;

import ai.strategy.MobilityStrategy;
import ai.strategy.PiecesNumberStrategy;

public class StrategyUtil {

	public final static IStrategy piecesNumberStrategy = new PiecesNumberStrategy();
	
	public final static IStrategy mobilityStrategy = new MobilityStrategy();
	
	public final static IStrategy strategies[] = {piecesNumberStrategy, mobilityStrategy};
	
	public final static int strategiesSize = strategies.length;
	
	public final static double WEIGHT_LIMIT = 10.0;

	public static void printStrategyWeigths(double weights[]) {
		if (weights.length != strategiesSize) {
			throw new IllegalArgumentException("Number of given weigths doesn't match strategies number");
		}
		
		for (int i = 0; i < strategiesSize; i++){
			System.out.println(String.format("%s: %f", strategies[i].getClass().getName(), weights[i]));
		}
	}
	
}
