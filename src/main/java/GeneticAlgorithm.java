import java.util.Random;

public class GeneticAlgorithm {

    public static final int DIMENSION_2D = 2;
    public static final double CROSSOVER_RATE = 0.5;
    public static final int TOURNAMENT_SIZE = 15;
    public static final double CROSSOVER_VALUE = 0.5;
    public static final double MUTATION_RATE = 0.2;
    public static final int NUM_GENERATIONS = 1000;
    private Function function;
    private int populationSize;
    private int dimension;
    private double[][] population;
    private double[] fittest;
    private Random random;

    public GeneticAlgorithm(int populationSize, int dimension, Function function) {
        this.dimension = dimension;
        this.function = function;
        this.populationSize = populationSize;
        population = new double[populationSize][dimension];
        this.random = new Random();
    }

    public void initialize() {
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < dimension-1; j++) {
                population[i][j] = random.nextDouble() * (function.getMin() - function.getMax()) + function.getMax();
            }
            if(dimension == DIMENSION_2D){
                population[i][1] = evaluate(population[i][0]);
            }
            else{
                population[i][2] = evaluate(population[i][0], population[i][1]);
            }
        }
        fittest = population[0];
    }

    public double[] optimize() {
        for(int k = 0; k< NUM_GENERATIONS; k++){

            double[][] parents = new double[populationSize][dimension];
            for (int i = 0; i < populationSize; i++) {
                parents[i] = selection();
            }

            double[][] children = new double[populationSize][dimension];
            for (int i = 0; i < populationSize / 2; i++) {
                int parent1Index = 2 * i;
                int parent2Index = 2 * i + 1;
                if(dimension == DIMENSION_2D){
                    if (random.nextDouble() < CROSSOVER_RATE) {
                        children[parent1Index][0] = CROSSOVER_VALUE * parents[parent1Index][0] + (1-CROSSOVER_VALUE)*parents[parent2Index][0];
                        children[parent2Index][0] = CROSSOVER_VALUE * parents[parent2Index][0] + (1-CROSSOVER_VALUE)*parents[parent1Index][0];
                    } else {
                        children[parent1Index] = parents[parent1Index];
                        children[parent2Index] = parents[parent2Index];
                    }
                }
                else{
                    if (random.nextDouble() < CROSSOVER_RATE) {
                        children[parent1Index][0] = CROSSOVER_VALUE * parents[parent1Index][0] + (1-CROSSOVER_VALUE)*parents[parent2Index][0];
                        children[parent1Index][1] = CROSSOVER_VALUE * parents[parent1Index][1] + (1-CROSSOVER_VALUE)*parents[parent2Index][1];
                        children[parent2Index][0] = CROSSOVER_VALUE * parents[parent2Index][0] + (1-CROSSOVER_VALUE)*parents[parent1Index][0];
                        children[parent2Index][1] = CROSSOVER_VALUE * parents[parent2Index][1] + (1-CROSSOVER_VALUE)*parents[parent1Index][1];
                    } else {
                        children[parent1Index] = parents[parent1Index];
                        children[parent2Index] = parents[parent2Index];
                    }
                }
                children[parent1Index] = mutate(children[parent1Index]);
                children[parent1Index] = przedzial(children[parent1Index]);
                children[parent2Index] = mutate(children[parent2Index]);
                children[parent2Index] = przedzial(children[parent2Index]);
            }
            population = children;

            evaluateAll();
        }
        findFittest();
        return fittest;
    }

    private void evaluateAll() {
        for (int i = 0; i < populationSize; i++) {
            if(dimension == DIMENSION_2D){
                population[i][1] = evaluate(population[i][0]);
            }
            else{
                population[i][2] = evaluate(population[i][0], population[i][1]);
            }
        }
    }

    public double[] selection(){
        double[][] tournamentIndividuals = new double[TOURNAMENT_SIZE][dimension];
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int randomIndex = random.nextInt(populationSize);
            tournamentIndividuals[i] = population[randomIndex];
        }

        double[] bestIndividual = new double[dimension];
        for (double[] individual : tournamentIndividuals) {
            if(dimension == DIMENSION_2D){
                double fitness = evaluate(individual[0]);
                if (fitness < evaluate(bestIndividual[0])) {
                    bestIndividual = individual;
                }
            }
            else{
                double fitness = evaluate(individual[0],individual[1]);
                if (fitness < evaluate(bestIndividual[0], bestIndividual[1])) {
                    bestIndividual = individual;
                }
            }
        }
        return bestIndividual;
    }
    public void findFittest(){
        for(int i=0; i<populationSize; i++){
            if(dimension == DIMENSION_2D){
                if(population[i][1]<fittest[1]){
                    fittest = population[i];
                }
            }
            else{
                if(population[i][2]<fittest[2]){
                    fittest = population[i];
                }
            }
        }
    }

    public double[] mutate(double[] individual){
        double[] mutant = new double[dimension];
        if(random.nextDouble() < MUTATION_RATE){
            int sign = 0;
            if(dimension == DIMENSION_2D){
                if(random.nextDouble() >= 0.5)
                    sign = -1;
                else
                    sign = 1;
                mutant[0] = individual[0] + ((random.nextDouble()/100)*sign);
            }
            else{
                if(random.nextDouble() >= 0.5)
                    sign = -1;
                else
                    sign = 1;
                mutant[0] = individual[0] + ((random.nextDouble()/100)*sign);
                if(random.nextDouble() >= 0.5)
                    sign = -1;
                else
                    sign = 1;
                mutant[1] = individual[1] + ((random.nextDouble()/100)*sign);
            }
        }
        return mutant;
    }

    public double[] przedzial(double[] individual){
        if(dimension == DIMENSION_2D){
            if(individual[0] > function.getMax()){
                individual[0] = function.getMax();
            }
            else if(individual[0] < function.getMin()){
                individual[0] = function.getMin();
            }
        }
        else{
            if(individual[0] > function.getMax()){
                individual[0] = function.getMax();
            }
            else if(individual[0] < function.getMin()){
                individual[0] = function.getMin();
            }

            if(individual[1] > function.getMax()){
                individual[1] = function.getMax();
            }
            else if(individual[1] < function.getMin()){
                individual[1] = function.getMin();
            }
        }

        return individual;
    }

    private double evaluate(double x) {
        return function.eval(x);
    }

    private double evaluate(double x, double y) {
        return function.eval(x,y);
    }
}