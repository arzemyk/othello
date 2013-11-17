package ai.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import model.EvolutionaryPlayer;
import model.Game;
import model.GameState;
import model.PlayerColor;
import ai.StrategyUtil;

public class Evolution {

	private final static int POPULATION_SIZE = 3;

	private final static int NUMBER_OF_MATCHES = 1;

	private final static int NUMBER_OF_BEST = 2;

	private final static int WIN_POINTS = 3;

	private final static int DRAW_POINTS = 1;

	private final static double MUTATION_POSSIBILITY = 0.3;

	private final static double MUTATION_RANGE = 1.0;

	private Random random = new Random();

	private List<double[]> population = new ArrayList<>(POPULATION_SIZE);

	private List<Integer> populationFitness = new ArrayList<>(POPULATION_SIZE);

	private int iterationNumber = 0;
	
	private final static Logger LOGGER = Logger
			.getLogger(Evolution.class.getName());

	public static void main(String[] args) {
		Evolution evolution = new Evolution();
		evolution.startEvolution();
	}

	public void startEvolution() {

		for (int i = 0; i < POPULATION_SIZE; i++) {
			population.add(chooseRandomIndividual());
		}

		while (true) {
			iterationNumber++;

			playMatch();
			printPopulation();
			selectBest();
			addRandoms();
			mutateIndividuals();
		}
	}

	private void printPopulation() {
		System.out.println("Iteration number: " + iterationNumber);

		for (int i = 0; i < POPULATION_SIZE; i++) {
			for (int w = 0; w < StrategyUtil.strategiesSize; w++) {
				System.out.print(population.get(i)[w] + " ");
			}
			System.out.println(": " + populationFitness.get(i));
		}

		System.out.println();
	}

	private void addRandoms() {
		for (int i = 0; i < POPULATION_SIZE - NUMBER_OF_BEST; i++) {
			population.add(chooseRandomIndividual());
		}
	}

	private void selectBest() {

		List<double[]> newPopulation = new ArrayList<>(POPULATION_SIZE);

		int sumOfFitness = 0;
		for (Integer fitness : populationFitness) {
			sumOfFitness += fitness;
		}

		for (int i = 0; i < NUMBER_OF_BEST; i++) {
			int randomHit = random.nextInt(sumOfFitness);

			for (int index = 0; index < POPULATION_SIZE; index++) {
				randomHit -= populationFitness.get(index);

				if (randomHit < 0) {
					newPopulation.add(population.get(index).clone());
					break;
				}
			}
		}
		
		if (newPopulation.size() != NUMBER_OF_BEST) {
			throw new IllegalStateException("Size of new population doesn't match NUMBER_OF_BEST");
		}

		population = newPopulation;
	}

	private void playMatch() {

		populationFitness.clear();
		for (int i = 0; i < population.size(); i++) {
			populationFitness.add(i, new Integer(0));
		}

		for (int i = 0; i < population.size(); i++) {
			for (int j = 0; j < population.size(); j++) {

				if (i == j) {
					continue;
				}

				for (int m = 0; m < NUMBER_OF_MATCHES; m++) {
					LOGGER.info(String.format("Playing match %d between %d and %d", m, i, j));
					
					Game game = new Game();
					EvolutionaryPlayer blackPlayer = new EvolutionaryPlayer(
							game.getBoard(), PlayerColor.BLACK,
							population.get(i));
					EvolutionaryPlayer whitePlayer = new EvolutionaryPlayer(
							game.getBoard(), PlayerColor.WHITE,
							population.get(j));

					GameState gameState = game.run(blackPlayer, whitePlayer);

					if (gameState == GameState.BLACK_WON) {
						populationFitness.set(i, populationFitness.get(i)
								+ WIN_POINTS);
					} else if (gameState == GameState.WHITE_WON) {
						populationFitness.set(j, populationFitness.get(j)
								+ WIN_POINTS);
					} else if (gameState == GameState.DRAW) {
						populationFitness.set(i, populationFitness.get(i)
								+ DRAW_POINTS);
						populationFitness.set(j, populationFitness.get(j)
								+ DRAW_POINTS);
					}
				}
			}
		}
	}

	private void mutateIndividuals() {
		for (int i = 0; i < POPULATION_SIZE; i++) {

			for (int w = 0; w < StrategyUtil.strategiesSize; w++) {
				if (random.nextDouble() <= MUTATION_POSSIBILITY) {
					double mutation = random.nextDouble() * MUTATION_RANGE
							- MUTATION_RANGE / 2;

					population.get(i)[w] += mutation;
				}

			}
		}
	}

	private double[] chooseRandomIndividual() {

		double individual[] = new double[StrategyUtil.strategiesSize];
		for (int i = 0; i < StrategyUtil.strategiesSize; i++) {
			individual[i] = random.nextDouble() * StrategyUtil.WEIGHT_LIMIT;
		}

		return individual;
	}

}
