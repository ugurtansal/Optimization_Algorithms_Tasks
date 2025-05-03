import java.util.Arrays;
import java.util.Random;

public class Main {
    public static final double[][] coordinates = {
            {16.47, 96.10},
            {16.47, 94.44},
            {20.09, 92.54},
            {22.39, 93.37},
            {25.23, 97.24},
            {22.00, 96.05},
            {20.47, 97.02},
            {17.20, 96.29},
            {16.30, 97.38},
            {14.05, 98.12},
            {16.53, 97.38},
            {21.52, 95.59},
            {19.41, 97.13},
            {20.09, 94.55}
    };

    public static int[] movement(int[] arr)
    {
        int[]temp = arr.clone();
        Random random = new Random();
        int index = random.nextInt(arr.length);
        int index2;
        do{
            index2 = random.nextInt(arr.length);
        }while (index == index2);

        int tempIndex = temp[index];
        temp[index] = temp[index2];
        temp[index2] = tempIndex;

        return temp;
    }

    public static double  fitness(int[] arr)
    {
        double total = 0;
        for(int i=0; i<arr.length-2; i++)
        {
            int index=arr[i];
            int nextIndex=arr[i+1];
            total += distance(coordinates[index-1][0], coordinates[nextIndex-1][0], coordinates[index-1][1], coordinates[nextIndex-1][1]);
        }
        total+=distance(coordinates[arr.length-1][0], coordinates[0][0], coordinates[arr.length-1][1], coordinates[0][1]);
        return total;
    }

    public static double distance(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static void main(String[] args) {
        int[] currentSolution = {10,11,14,6,4,1,2,13,12,7,3,5,9,8};
        double currentFitness = fitness(currentSolution);
        double temperature = 100;
        double colling = 0.99;
        Random rand = new Random();
        int iteration = 1000;
        int i = 0;
        while ( i< iteration) {
            int[] newSolution = movement(currentSolution);
            double newFitness = fitness(newSolution);

            if (newFitness < currentFitness || rand.nextDouble() < Math.exp((currentFitness - newFitness) / temperature)) {
                currentSolution = newSolution;
                currentFitness = newFitness;
            }
            temperature *= colling;
            System.out.println("Iteration: " + i+", fitness: "+  currentFitness);
            i++;
        }
        System.out.println("Best solution: " + Arrays.toString(currentSolution));
        System.out.println("Best fitness: " + currentFitness);
    }
}