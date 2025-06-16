import java.util.*;

public class Main {

    static final int FOOD_NUMBER = 20;
    static final int DIM = 10 * 2; // 10 pano, her biri x ve y → toplam 20 boyut
    static final int LIMIT = 100;
    static final int MAX_ITER = 500;
    static final double LOWER_BOUND = 0;
    static final double UPPER_BOUND = 100;
    static final Random rand = new Random();

    static double[] visibilityScores = new double[10]; // sabit trafik yoğunluğu puanları - // Fixed traffic density scores for each billboard

    static class FoodSource {
        double[] position = new double[DIM];
        double fitness;
        int trial = 0;

        FoodSource() {
            for (int i = 0; i < DIM; i++) {
                position[i] = LOWER_BOUND + rand.nextDouble() * (UPPER_BOUND - LOWER_BOUND);
            }
            fitness = evaluate(position);
        }

        void generateNeighbor(FoodSource[] population) {
            int k;
            do {
                k = rand.nextInt(FOOD_NUMBER);
            } while (population[k] == this);

            int d = rand.nextInt(DIM);
            double phi = rand.nextDouble() * 2 - 1;

            double[] newPos = position.clone();
            newPos[d] = position[d] + phi * (position[d] - population[k].position[d]);
            newPos[d] = Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, newPos[d]));

            double newFit = evaluate(newPos);
            if (newFit > fitness) { // MAKSİMİZASYON - Maximization
                position = newPos;
                fitness = newFit;
                trial = 0;
            } else {
                trial++;
            }
        }
    }

    public static double evaluate(double[] position) {
        double score = 0;
        for (int i = 0; i < 10; i++) {
            score += visibilityScores[i]; // sabit trafik puanları - Add visibility scores
        }

        // Yakınlık cezası
        for (int i = 0; i < 10; i++) {
            double xi = position[2 * i];
            double yi = position[2 * i + 1];
            for (int j = i + 1; j < 10; j++) {
                double xj = position[2 * j];
                double yj = position[2 * j + 1];
                double dist = Math.sqrt(Math.pow(xi - xj, 2) + Math.pow(yi - yj, 2));
                if (dist < 10) {
                    score -= 50.0; // cezalandır - Penalize for being too close
                }
            }
        }
        return score; // maksimize edilecek - Maximize
    }

    public static void main(String[] args) {

        // Görünürlük puanlarını rastgele ata
        // Randomly assign visibility scores for each billboard
        for (int i = 0; i < 10; i++) {
            visibilityScores[i] = 50 + rand.nextDouble() * 50; // Random between 50–100
        }

        FoodSource[] population = new FoodSource[FOOD_NUMBER];
        for (int i = 0; i < FOOD_NUMBER; i++) {
            population[i] = new FoodSource();
        }

        FoodSource best = population[0];

        for (int iter = 0; iter < MAX_ITER; iter++) {
            // 1. Employed
            for (FoodSource food : population) {
                food.generateNeighbor(population);
            }

            // 2. Onlooker
            double totalFit = 0;
            for (FoodSource food : population) {
                totalFit += food.fitness;
            }

            for (int i = 0; i < FOOD_NUMBER; i++) {
                double r = rand.nextDouble() * totalFit;
                double acc = 0;
                for (FoodSource food : population) {
                    acc += food.fitness;
                    if (acc >= r) {
                        food.generateNeighbor(population);
                        break;
                    }
                }
            }

            // 3. Scout
            for (int i = 0; i < FOOD_NUMBER; i++) {
                if (population[i].trial > LIMIT) {
                    population[i] = new FoodSource();
                }
            }

            // En iyi çözümü güncelle - Update the best solution
            for (FoodSource food : population) {
                if (food.fitness > best.fitness) {
                    best = new FoodSource();
                    best.position = food.position.clone();
                    best.fitness = food.fitness;
                }
            }

            System.out.printf("Iter %d: Best fitness = %.2f\n", iter, best.fitness);
        }

        System.out.printf("\nBest positions found:\n");
        for (int i = 0; i < 10; i++) {
            System.out.printf("Pano %d: (x = %.2f, y = %.2f)\n", i + 1, best.position[2 * i], best.position[2 * i + 1]);
        }
        System.out.printf("Toplam görünürlük puanı: %.2f\n", best.fitness);
    }
}
