import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static final int MAX_ITR = 100;
    static final int PARTICLE_SIZE = 30;
    static final int SIZE=5;
    static final int DIMENSION = 2;
    static final double c1= 2.0 , c2= 2.0 , w = 0.5;
    static final double MIN = -50.0, MAX = 50.0;
    static Random rnd= new Random();

    static class Particle {
        double position[][]= new double[SIZE][DIMENSION];
        double velocity[][]= new double[SIZE][DIMENSION];
        double bestPosition[][]= new double[SIZE][DIMENSION];
        double bestFitness = Double.MAX_VALUE;

        Particle(){
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < DIMENSION; j++) {
                    position[i][j] = rnd.nextDouble() * (MAX - MIN) + MIN;
                    velocity[i][j] = rnd.nextDouble() * (MAX - MIN) + MIN;
                    bestPosition[i][j] = position[i][j];
                }
            }
            bestFitness = fitness(position);
        }

        void update(double[][] globalBestPosition) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < DIMENSION; j++) {
                    double r1 = rnd.nextDouble();
                    double r2 = rnd.nextDouble();
                    velocity[i][j] = w * velocity[i][j] + c1 * r1 * (bestPosition[i][j] - position[i][j]) + c2 * r2 * (globalBestPosition[i][j] - position[i][j]);
                    position[i][j] += velocity[i][j];
                }
            }
            double currentFitness = fitness(position);
            if (currentFitness < bestFitness) {
                bestFitness = currentFitness;
               bestPosition = deepCopy(position);
            }
        }
    }

    static double fitness(double[][] position) {
        double sum = 0;
        for (int i = 0; i < SIZE-1; i++) {
            for (int j = i+1; j < SIZE; j++) {
                double distance=distance(position[i], position[(j)]);
                sum += distance;
            }
        }
            if (sum<5) {
                sum +=10;
            }
        return sum;
    }

    static double distance(double[] x, double[] y) {
        double sum = 0;
        for (int i = 0; i < DIMENSION; i++) {
           sum += Math.pow(x[i] - y[i], 2);
        }
        return Math.sqrt(sum);
    }
    public static void main(String[] args) {
        Particle[] particles = new Particle[PARTICLE_SIZE];
        for (int i = 0; i < PARTICLE_SIZE; i++) {
            particles[i] = new Particle();
        }

        double[][] globalBestPosition = deepCopy(particles[0].bestPosition);
        double globalBestFitness = particles[0].bestFitness;
        for (int i = 0; i < MAX_ITR; i++) {
            for(Particle p : particles) {
                p.update(globalBestPosition);

                if (p.bestFitness < globalBestFitness) {
                    globalBestFitness = p.bestFitness;
                    globalBestPosition = deepCopy(p.bestPosition);
                }
            }
            System.out.println("Iteration " + (i + 1) + ": Best Fitness = " + globalBestFitness);
        }
    }
    static double[][] deepCopy(double[][] original) {
        double[][] copy = new double[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

}