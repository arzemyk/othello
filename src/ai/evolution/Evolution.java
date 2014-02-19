package ai.evolution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import ai.StrategyUtil;

public class Evolution {

	private final static int POPULATION_SIZE = 15;

	public final static int NUMBER_OF_MATCHES = 3;

	public final static int MAX_ITERATION_COUNT = 60;

	private final static int NUMBER_OF_BEST = 13;

	public final static int WIN_POINTS = 3;

	public final static int DRAW_POINTS = 1;

	private final static double MUTATION_POSSIBILITY = 0.3;

	private final static double CROSSING_POSSIBILITY = 0.5;

	private final static double MUTATION_RANGE = 1.0;

	private final static int THREADS = 8;

	private Random random = new Random();

	private List<double[]> population = new ArrayList<>(POPULATION_SIZE);

	private List<double[]> newPopulation = new ArrayList<>(POPULATION_SIZE);

	private List<Integer> populationFitness = new ArrayList<>(POPULATION_SIZE);

	private Map<Integer, double[]> bestInIteration = new HashMap<Integer, double[]>();

	private int iterationNumber = 0;

	private ExecutorService executor = Executors.newFixedThreadPool(THREADS);

	private List<Future<Map<Integer, Integer>>> future = new ArrayList<Future<Map<Integer, Integer>>>();

	private final static Logger LOGGER = Logger.getLogger(Evolution.class
			.getName());

	public static void main(String[] args) throws IOException {
		Evolution evolution = new Evolution();
		evolution.startEvolution();
		evolution.shutdown();
	}

	public void startEvolution() throws IOException {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population.add(chooseRandomIndividual());
		}

		while (true) {
			iterationNumber++;
			if (iterationNumber > MAX_ITERATION_COUNT) {
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd_HH:mm:ss");
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						"results/result_"
								+ df.format(Calendar.getInstance().getTime())
								+ ".txt"));
				bw.append("Population size: " + POPULATION_SIZE + ";");
				bw.newLine();
				bw.append("Number of best: " + NUMBER_OF_BEST + ";");
				bw.newLine();
				bw.append("Iteration count: " + MAX_ITERATION_COUNT + ";");
				bw.newLine();
				bw.append("Number of matches: " + NUMBER_OF_MATCHES
						+ ", Win points: " + WIN_POINTS + ", Draw points: "
						+ DRAW_POINTS + ";");
				bw.newLine();
				bw.append("Crossing possibility: " + CROSSING_POSSIBILITY + ";");
				bw.newLine();
				bw.append("Mutation posibility: " + MUTATION_POSSIBILITY  + ", Mutation range: " + MUTATION_RANGE + ";");
				bw.newLine();
				bw.newLine();
				
				for (Integer i : bestInIteration.keySet()) {
					for (double w : bestInIteration.get(i)) {
						bw.append(w + ";");
					}
					bw.newLine();
				}
				bw.close();
				return;
			}
			future.clear();
			long begin = System.currentTimeMillis();
			playMatch();
			printPopulation();
			selectBest();
			crossingOver();
			addRandoms();
			mutateIndividuals();
			System.out.println((double)(System.currentTimeMillis() - begin)/60000);
			population = newPopulation;
		}
	}

	public void shutdown() {
		executor.shutdown();
	}

	private void printPopulation() {
		System.out.println("Iteration number: " + iterationNumber);

		int maxFitness = 0;
		int index = 0;
		for (int i = 0; i < POPULATION_SIZE; i++) {
			for (int w = 0; w < StrategyUtil.strategiesSize; w++) {
				System.out.print(population.get(i)[w] + " ");
			}
			if (maxFitness < populationFitness.get(i)) {
				index = i;
				maxFitness = populationFitness.get(i);
			}
			System.out.println(": " + populationFitness.get(i));
		}

		bestInIteration.put(iterationNumber, population.get(index));

		System.out.println();
	}

	private void addRandoms() {
		for (int i = 0; i < POPULATION_SIZE - NUMBER_OF_BEST; i++) {
			newPopulation.add(chooseRandomIndividual());
		}
	}

	private int selectIndividual(int sumOfFitness) {
		int randomHit = random.nextInt(sumOfFitness);

		for (int index = 0; index < POPULATION_SIZE; index++) {
			randomHit -= populationFitness.get(index);

			if (randomHit < 0) {
				return index;
			}
		}

		return 0;
	}

	private void selectBest() {

		newPopulation = new ArrayList<>(POPULATION_SIZE);

		int sumOfFitness = 0;
		for (Integer fitness : populationFitness) {
			sumOfFitness += fitness;
		}

		for (int i = 0; i < NUMBER_OF_BEST; i++) {
			newPopulation.add(population.get(selectIndividual(sumOfFitness))
					.clone());
		}

		if (newPopulation.size() != NUMBER_OF_BEST) {
			throw new IllegalStateException(
					"Size of new population doesn't match NUMBER_OF_BEST");
		}
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

				Callable<Map<Integer, Integer>> worker = new EvolutionThread(i,
						j, population.get(i), population.get(j));
				Future<Map<Integer, Integer>> submit = executor.submit(worker);
				future.add(submit);
			}
		}

		for (Future<Map<Integer, Integer>> f : future) {
			try {
				Map<Integer, Integer> scores = f.get();
				for (Integer key : scores.keySet()) {
					populationFitness.set(key, populationFitness.get(key)
							+ scores.get(key));
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private void mutateIndividuals() {
		for (int i = 0; i < POPULATION_SIZE; i++) {

			for (int w = 0; w < StrategyUtil.strategiesSize; w++) {
				if (random.nextDouble() <= MUTATION_POSSIBILITY) {
					double mutation = random.nextDouble() * MUTATION_RANGE
							- MUTATION_RANGE / 2;

					newPopulation.get(i)[w] += mutation;
				}

			}
		}
	}

	private void crossingOver() {
		int sumOfFitness = 0;
		for (Integer fitness : populationFitness) {
			sumOfFitness += fitness;
		}

		for (int i = 0; i < POPULATION_SIZE - NUMBER_OF_BEST; i++) {
			if (random.nextDouble() <= CROSSING_POSSIBILITY) {
				int parent1Index = selectIndividual(sumOfFitness);
				int parent2Index = selectIndividual(sumOfFitness);

				if (parent1Index == parent2Index) {
					parent2Index += 1;
					parent2Index %= POPULATION_SIZE;
				}

				double individual[] = new double[StrategyUtil.strategiesSize];
				for (int j = 0; j < StrategyUtil.strategiesSize; j++) {
					individual[j] = (population.get(parent1Index)[j] + population
							.get(parent2Index)[j]) / 2;
				}

				newPopulation.add(individual);
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
