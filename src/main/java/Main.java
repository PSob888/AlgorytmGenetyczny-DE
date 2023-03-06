import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.Scanner;

public class Main {

    public static final int POPULATION_SIZE = 100;
    public static final int DIMENSION_MODIFIER = 2;
    public static final int WYBOR_GENETYCZNY = 0;
    public static final int DIMENSION_2D = 0;

    public static void main(String[] args){
        //(1-x)^2+100(y-x^2)^2
        Scanner scanner = new Scanner(System.in);
        System.out.println("0 - 2D, 1 - 3D");
        int dimension = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Podaj funkcje: ");
        String func = scanner.nextLine();

        System.out.println("Gore przedzialu: ");
        int max = scanner.nextInt();
        System.out.println("Dol przedzialu: ");
        int min = scanner.nextInt();

        Function function = new Function(func, min, max);

        System.out.println("0 - Genetyczny, 1 - DE");
        int wybor = scanner.nextInt();

        if(wybor == WYBOR_GENETYCZNY){
            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(POPULATION_SIZE,dimension + DIMENSION_MODIFIER, function);
            geneticAlgorithm.initialize();
            double[] wynik = geneticAlgorithm.optimize();
            printWynik(dimension, wynik, function);
        }
        else{
            DE de = new DE(POPULATION_SIZE, dimension + DIMENSION_MODIFIER, function);
            de.initialize();
            double[] wynik = de.optimize();
            printWynik(dimension, wynik, function);
        }
    }

    public static void printWynik(int dimension, double[] wynik, Function function){
        if(dimension == DIMENSION_2D)
        {
            System.out.println("f(" +  wynik[0] + ") = " + wynik[1]);
            chartMaker(wynik, function);
        }
        else{;
            System.out.println("f(" +  wynik[0] + ", " + wynik[1] + ") = " + wynik[2]);
        }
    }

    public static void chartMaker(double[] wynik, Function function){
        int amount = (int) (function.getMax() - function.getMin());
        double[] xData = new double[amount];
        double[] yData = new double[amount];
        for(int i = 0; i <= amount-1; i++){
            xData[i] = function.getMin() + i;
            yData[i] = function.eval(xData[i]);
        }

        double[] xPoint = new double[2];
        xPoint[0] = wynik[0];
        xPoint[1] = wynik[0];
        double[] yPoint = new double[2];
        yPoint[0] = wynik[1];
        yPoint[1] = wynik[1];
        XYChart chart = new XYChart(500, 400);
        chart.setTitle("Wykres funkcji");
        chart.setXAxisTitle("X");
        chart.setXAxisTitle("Y");
        chart.addSeries(function.getFun(), xData, yData);
        chart.addSeries("punkt", xPoint, yPoint);

        new SwingWrapper(chart).displayChart();
    }
}
