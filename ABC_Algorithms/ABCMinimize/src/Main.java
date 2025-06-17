import java.util.*;

public class Main {
    static final int FOOD_NUMBER = 20;   // Arı kolonisi büyüklüğü
    static final int SENSOR_COUNT = 5;   // Sensör sayısı
    static final int DIMENSION = 2;      // x, y
    static final int LIMIT = 50;         // Sınır sayısı (çözüm sabit kalırsa terk edilir)
    static final int MAX_ITER = 100;
    static final double LOWER_BOUND = 0.0;
    static final double UPPER_BOUND = 100.0;
    static final Random rnd = new Random();

    static class FoodSource {
        double[][] position = new double[SENSOR_COUNT][DIMENSION];
        double fitness;
        int trial = 0;

        FoodSource() {
            for (int i = 0; i < SENSOR_COUNT; i++) {
                for (int j = 0; j < DIMENSION; j++) {
                    position[i][j] = LOWER_BOUND + rnd.nextDouble() * (UPPER_BOUND - LOWER_BOUND);
                }
            }
            fitness = evaluate();
        }

        FoodSource(FoodSource other) {
            for (int i = 0; i < SENSOR_COUNT; i++) {
                System.arraycopy(other.position[i], 0, this.position[i], 0, DIMENSION);
            }
            this.fitness = other.fitness;
            this.trial = other.trial;
        }

        double evaluate() {
            double sum = 0;
            for (int i = 0; i < SENSOR_COUNT - 1; i++) {
                for (int j = i + 1; j < SENSOR_COUNT; j++) {
                    double dx = position[i][0] - position[j][0];
                    double dy = position[i][1] - position[j][1];
                    double distSq = dx * dx + dy * dy;
                    if (distSq < 1e-6) distSq = 1e-6; // Çakışma cezası
                    sum += 1.0 / distSq;
                }
            }
            return sum;
        }

        void mutate(FoodSource neighbor) {
            int sensorIdx = rnd.nextInt(SENSOR_COUNT);
            int dim = rnd.nextInt(DIMENSION);

            double phi = rnd.nextDouble() * 2 - 1; // [-1,1]
            double newValue = this.position[sensorIdx][dim] +
                    phi * (this.position[sensorIdx][dim] - neighbor.position[sensorIdx][dim]);

            newValue = Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, newValue));

            double old = this.position[sensorIdx][dim];
            this.position[sensorIdx][dim] = newValue;
            double newFitness = evaluate();

            if (newFitness < this.fitness) {
                this.fitness = newFitness;
                this.trial = 0;
            } else {
                this.position[sensorIdx][dim] = old;
                this.trial++;
            }
        }
    }

    public static void main(String[] args) {
        FoodSource[] foods = new FoodSource[FOOD_NUMBER];
        for (int i = 0; i < FOOD_NUMBER; i++) {
            foods[i] = new FoodSource();
        }

        FoodSource best = new FoodSource(foods[0]);

        for (int iter = 0; iter < MAX_ITER; iter++) {
            // Employed Bees
            for (int i = 0; i < FOOD_NUMBER; i++) {
                int k;
                do {
                    k = rnd.nextInt(FOOD_NUMBER);
                } while (k == i);

                foods[i].mutate(foods[k]);
            }

            // Onlooker Bees
            double[] adjustedFitness = new double[FOOD_NUMBER];
            double totalAdjusted = 0;

            for (int i = 0; i < FOOD_NUMBER; i++) {
                adjustedFitness[i] = 1.0 / (1.0 + foods[i].fitness); // küçük fitness daha büyük şansa sahip olur
                totalAdjusted += adjustedFitness[i];
            }

            for (int i = 0; i < FOOD_NUMBER; i++) {
                double r = rnd.nextDouble() * totalAdjusted;
                double acc = 0;

                for (int j = 0; j < FOOD_NUMBER; j++) {
                    acc += adjustedFitness[j];
                    if (acc >= r) {
                        int k;
                        do {
                            k = rnd.nextInt(FOOD_NUMBER);
                        } while (k == j);

                        foods[j].mutate(foods[k]); // yeni komşu oluştur
                        break;
                    }
                }
            }

            // Scout Bees
            for (int i = 0; i < FOOD_NUMBER; i++) {
                if (foods[i].trial > LIMIT) {
                    foods[i] = new FoodSource();
                }
            }

            // Update best
            for (FoodSource f : foods) {
                if (f.fitness < best.fitness) {
                    best = new FoodSource(f);
                }
            }

            System.out.printf("Iteration %d: Best Fitness = %.5f%n", iter + 1, best.fitness);
        }

        System.out.println("\n En iyi sensör yerleşimi:");
        for (int i = 0; i < SENSOR_COUNT; i++) {
            System.out.printf("Sensor %d: (%.2f, %.2f)%n", i + 1, best.position[i][0], best.position[i][1]);
        }
    }
}
