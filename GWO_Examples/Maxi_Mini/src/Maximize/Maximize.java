package Maximize;
import java.util.*;


public class Maximize{
    static final int TURBINE_COUNT = 4;
    static final int DIMENSION = 2;
    static final int POPULATION_SIZE = 30;
    static final int MAX_ITR = 100;
    static final int MIN_DISTANCE = 10;
    static final double LOWER_BOUND = 0;
    static final double UPPER_BOUND = 100;
    static Random rnd = new Random();

    static class Turbine {
        double[][] position = new double[TURBINE_COUNT][DIMENSION];
        double fitness;

        Turbine() {
            for (int i = 0; i < TURBINE_COUNT; i++) {
                for (int j = 0; j < DIMENSION; j++) {
                    position[i][j] = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * rnd.nextDouble();
                }
            }
            fitness = calculateFitness();
        }

        Turbine(Turbine other) {
            for (int i = 0; i < TURBINE_COUNT; i++) {
                for (int j = 0; j < DIMENSION; j++) {
                    this.position[i][j] = other.position[i][j];
                }
            }
            this.fitness = other.fitness;
        }

        double calculateFitness() {
            double total = 0;
            for (int i = 0; i < TURBINE_COUNT - 1; i++) {
                for (int j = i + 1; j < TURBINE_COUNT; j++) {
                    double d = distance(position[i], position[j]);
                    if (d < MIN_DISTANCE) {
                        total -= 1000 * (MIN_DISTANCE - d); // ceza
                    } else {
                        total += Math.log(d);
                    }
                }
            }
            return total;
        }

        double distance(double[] p1, double[] p2) {
            return Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2));
        }
    }

    public static void main(String[] args) {
        Turbine[] population = new Turbine[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new Turbine();
        }

        Turbine alpha = new Turbine(population[0]);
        Turbine beta = new Turbine(population[1]);
        Turbine delta = new Turbine(population[2]);

        for (int iter = 0; iter < MAX_ITR; iter++) {
            double a = 2.0 - iter * (2.0 / MAX_ITR);

            // En iyi 3 kurtu güncelle
            for (int i = 0; i < POPULATION_SIZE; i++) {
                Turbine wolf = population[i];
                if (wolf.fitness > alpha.fitness) {
                    delta = new Turbine(beta);
                    beta = new Turbine(alpha);
                    alpha = new Turbine(wolf);
                } else if (wolf.fitness > beta.fitness && !wolf.equals(alpha)) {
                    delta = new Turbine(beta);
                    beta = new Turbine(wolf);
                } else if (wolf.fitness > delta.fitness && !wolf.equals(alpha) && !wolf.equals(beta)) {
                    delta = new Turbine(wolf);
                }
            }

            for (int i = 0; i < POPULATION_SIZE; i++) {
                Turbine wolf = population[i];

                for (int j = 0; j < TURBINE_COUNT; j++) {
                    for (int d = 0; d < DIMENSION; d++) {
                        double A1 = 2 * a * rnd.nextDouble() - a;
                        double C1 = 2 * rnd.nextDouble();
                        double D_alpha = Math.abs(C1 * alpha.position[j][d] - wolf.position[j][d]);
                        double X1 = alpha.position[j][d] - A1 * D_alpha;

                        double A2 = 2 * a * rnd.nextDouble() - a;
                        double C2 = 2 * rnd.nextDouble();
                        double D_beta = Math.abs(C2 * beta.position[j][d] - wolf.position[j][d]);
                        double X2 = beta.position[j][d] - A2 * D_beta;

                        double A3 = 2 * a * rnd.nextDouble() - a;
                        double C3 = 2 * rnd.nextDouble();
                        double D_delta = Math.abs(C3 * delta.position[j][d] - wolf.position[j][d]);
                        double X3 = delta.position[j][d] - A3 * D_delta;

                        wolf.position[j][d] = (X1 + X2 + X3) / 3.0;

                        // Sınır kontrolü
                        wolf.position[j][d] = Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, wolf.position[j][d]));
                    }
                }

                // Yeni fitness değeri hesapla
                wolf.fitness = wolf.calculateFitness();
            }

            System.out.printf("Iteration %d: Best Fitness = %.5f%n", iter + 1, alpha.fitness);
        }

        System.out.println("\n Final Best Positions:");
        for (int i = 0; i < TURBINE_COUNT; i++) {
            System.out.printf("Turbine %d: (%.2f, %.2f)%n", i + 1, alpha.position[i][0], alpha.position[i][1]);
        }
        System.out.printf("Total Fitness: %.5f%n", alpha.fitness);
    }
}
