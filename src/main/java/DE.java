import java.util.Random;

public class DE {

    public static final int DIMENSION_2D = 2;
    public static final int NUM_GENERATIONS = 1000;
    public static final double MUTATION_RATE = 0.4;
    public static final double MUTATION_MODIFIER = 0.4;
    Function function;
    int dimension;
    int populationSize;
    private final double[][] population;
    private double[] bestPosition;
    private double bestValue;
    private final Random random = new Random();

    public DE(int populationSize, int dimension, Function function) {
        this.function = function;
        this.dimension = dimension;
        this.populationSize = populationSize;
        population = new double[populationSize][dimension]; //a[0][0] - x   a[0][1] - y   a[0][2] - z
        bestPosition = new double[dimension];
        bestValue = Double.POSITIVE_INFINITY;
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

        bestPosition = population[0].clone();
        for (int i = 1; i < populationSize; i++) {
            if(dimension == DIMENSION_2D){
                if (population[i][1] < bestValue) {
                    bestPosition = population[i].clone();
                    bestValue = population[i][1];
                }
            }
            else{
                if (population[i][2] < bestValue) {
                    bestPosition = population[i].clone();
                    bestValue = population[i][2];
                }
            }
        }
    }

    public double[] optimize() {
        for(int k = 0; k < NUM_GENERATIONS; k++){
            for (int i = k; i < populationSize; i++) {
                int r1, r2, r3;
                do {
                    r1 = random.nextInt(populationSize);
                } while (r1 == i);
                do {
                    r2 = random.nextInt(populationSize);
                } while (r2 == i || r2 == r1);
                do {
                    r3 = random.nextInt(populationSize);
                } while (r3 == i || r3 == r1 || r3 == r2);

                double[] trial = new double[dimension];
                for (int j = 0; j < dimension-1; j++) {
                    if (random.nextDouble() < MUTATION_RATE) {
                        trial[j] = bestPosition[j] + MUTATION_MODIFIER * (population[r1][j] - population[r2][j]);
                    } else {
                        trial[j] = population[i][j];
                    }
                }

                trial = przedzial(trial);

                if(dimension == DIMENSION_2D){
                    trial[1] = evaluate(trial[0]);
                    if (trial[1] < population[i][1]) {
                        population[i] = trial;
                        if (trial[1] < bestValue) {
                            bestPosition = trial;
                            bestValue = trial[1];
                        }
                    }
                }
                else{
                    trial[2] = evaluate(trial[0], trial[1]);
                    if (trial[2] < population[i][2]) {
                        population[i] = trial;
                        if (trial[2] < bestValue) {
                            bestPosition = trial;
                            bestValue = trial[2];
                        }
                    }
                }
            }
        }

        if(dimension == DIMENSION_2D){
            bestPosition[1] = evaluate(bestPosition[0]);
        }
        else{
            bestPosition[2] = evaluate(bestPosition[0], bestPosition[1]);
        }
        return bestPosition;
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
