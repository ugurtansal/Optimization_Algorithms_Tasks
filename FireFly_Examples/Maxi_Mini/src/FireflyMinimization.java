import java.util.Random;

public class FireflyMinimization {
    static final int FIREFLY_COUNT = 30;
    static final int DIMENSION = 2;
    static final int MAX_GEN = 100;
    static final double LOWER_BOUND = 0;
    static final double UPPER_BOUND = 100;
    static final double BETA0 = 1.0;
    static final double GAMMA = 0.01;
    static final double ALPHA = 0.2;
    static final double EPSILON = 1e-6;
    static Random rand = new Random();

    static class Firefly {
        double[] position = new double[DIMENSION];
        double fitness;

        Firefly() {
            for (int i = 0; i < DIMENSION; i++) {
                position[i] = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * rand.nextDouble();
            }
            fitness = evaluate(position);
        }

        static double evaluate(double[] pos) {
            double centerX = 50, centerY = 50;
            double distToCenter = Math.sqrt(Math.pow(pos[0] - centerX, 2) + Math.pow(pos[1] - centerY, 2));
            return 1.0 / (distToCenter + EPSILON); // Şehir merkezine yakınsa daha fazla gürültü
        }

        double distanceTo(Firefly other) {
            double sum = 0;
            for (int i = 0; i < DIMENSION; i++) {
                sum += Math.pow(this.position[i] - other.position[i], 2);
            }
            return Math.sqrt(sum);
        }

        void moveTowards(Firefly better) {
            double r = this.distanceTo(better);
            double beta = BETA0 * Math.exp(-GAMMA * r * r);

            for (int i = 0; i < DIMENSION; i++) {
                double randStep = ALPHA * (rand.nextDouble() - 0.5) * (UPPER_BOUND - LOWER_BOUND);
                position[i] += beta * (better.position[i] - position[i]) + randStep;

                // Sınırlar içinde kal
                position[i] = Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, position[i]));
            }
            fitness = evaluate(position);
        }
    }

    public static void main(String[] args) {
        Firefly[] swarm = new Firefly[FIREFLY_COUNT];
        for (int i = 0; i < FIREFLY_COUNT; i++) {
            swarm[i] = new Firefly();
        }

        for (int gen = 0; gen < MAX_GEN; gen++) {
            for (int i = 0; i < FIREFLY_COUNT; i++) {
                for (int j = 0; j < FIREFLY_COUNT; j++) {
                    if (swarm[j].fitness < swarm[i].fitness) { // çünkü minimizasyon!
                        swarm[i].moveTowards(swarm[j]);
                    }
                }
            }

            Firefly best = swarm[0];
            for (Firefly f : swarm) {
                if (f.fitness < best.fitness) {
                    best = f;
                }
            }

            System.out.printf("Generation %d: Best Noise = %.6f at (%.2f, %.2f)%n", gen + 1, best.fitness, best.position[0], best.position[1]);
        }
    }
}
