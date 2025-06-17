import java.util.*;

public class Maximization {
    static final int PANEL_COUNT = 5;
    static final int DIMENSION = 2; // x, y koordinatları
    static final int POP_SIZE = 20;
    static final int MAX_ITER = 200;
    static final double MIN_DIST = 10.0;
    static final double LOWER_BOUND = 0;
    static final double UPPER_BOUND = 100;
    static final double BETA0 = 2.0;
    static final double GAMMA = 0.1;
    static final double ALPHA = 0.7;

    static Random rand = new Random();

    static class Firefly {
        double[][] position = new double[PANEL_COUNT][DIMENSION];
        double brightness;

        Firefly() {
            for (int i = 0; i < PANEL_COUNT; i++) {
                for (int j = 0; j < DIMENSION; j++) {
                    position[i][j] = LOWER_BOUND + rand.nextDouble() * (UPPER_BOUND - LOWER_BOUND);
                }
            }
            brightness = evaluate();
        }

        double evaluate() {
            double energy = 0;
            for (int i = 0; i < PANEL_COUNT; i++) {
                energy += sunlight(position[i]);
            }
            for (int i = 0; i < PANEL_COUNT - 1; i++) {
                for (int j = i + 1; j < PANEL_COUNT; j++) {
                    double dist = distance(position[i], position[j]);
                    if (dist < MIN_DIST) energy -= 1000 * (MIN_DIST - dist); // ceza
                }
            }
            return energy;
        }

        double sunlight(double[] pos) {
            return 100 * Math.sin(pos[0] * Math.PI / 100) + 50 * Math.cos(pos[1] * Math.PI / 100);
        }

        double distance(double[] a, double[] b) {
            double sum = 0;
            for (int i = 0; i < DIMENSION; i++) {
                sum += (a[i] - b[i]) * (a[i] - b[i]);
            }
            return Math.sqrt(sum);
        }
    }

    public static void main(String[] args) {
        Firefly[] swarm = new Firefly[POP_SIZE];
        for (int i = 0; i < POP_SIZE; i++) swarm[i] = new Firefly();

        for (int iter = 0; iter < MAX_ITER; iter++) {
            for (int i = 0; i < POP_SIZE; i++) {
                for (int j = 0; j < POP_SIZE; j++) {
                    if (swarm[j].brightness > swarm[i].brightness) {
                        double r = 0;
                        for (int k = 0; k < PANEL_COUNT; k++) {
                            r += Math.pow(swarm[i].distance(swarm[i].position[k], swarm[j].position[k]), 2);
                        }
                        r = Math.sqrt(r);
                        double beta = BETA0 * Math.exp(-GAMMA * r * r);

                        for (int p = 0; p < PANEL_COUNT; p++) {
                            for (int d = 0; d < DIMENSION; d++) {
                                swarm[i].position[p][d] += beta * (swarm[j].position[p][d] - swarm[i].position[p][d])
                                        + ALPHA * (rand.nextDouble() - 0.5);
                                swarm[i].position[p][d] = Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, swarm[i].position[p][d]));
                            }
                        }
                        swarm[i].brightness = swarm[i].evaluate();
                    }
                }
            }

            double best = Arrays.stream(swarm).mapToDouble(f -> f.brightness).max().orElse(0);
            System.out.println("Iter " + iter + " Best Energy = " + best);
        }
    }
}
