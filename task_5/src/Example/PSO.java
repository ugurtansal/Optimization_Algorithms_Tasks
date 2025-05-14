package Example;

import java.util.Random;

public class PSO {

    class Rosenberg {
        public static int count = 0;
        public static double evaluate(double[] x) {
            count++;
            double sum = 0.0;
            for (int i = 0; i < x.length - 1; i++) {
                sum += 100 * Math.pow(x[i + 1] - Math.pow(x[i], 2), 2) + Math.pow(1 - x[i], 2);
            }
            return sum;
        }
    }

    public class ParticleSwarmOptimization {
        public static void main(String[] args) {

            int iterationsMax = 100;
            int dimension = 5;
            int sizeOfSwarm = 10;


            double maxInertia = 0.9;
            double minInertia = 0.4;

            double cognitiveFactor = 2.0;
            double socialFactor = 2.0;


            double[][] positions = new double[sizeOfSwarm][dimension];
            double[][] velocities = new double[sizeOfSwarm][dimension];
            double[][] personalBests = new double[sizeOfSwarm][dimension];


            double[] gBestPosition = new double[dimension];
            double gBestFitness = Double.MAX_VALUE;

            Random random = new Random();

            // Initial positions and velocities
            for (int i = 0; i < sizeOfSwarm; i++) {
                for (int j = 0; j < dimension; j++) {
                    positions[i][j] = random.nextInt(-5, 5);
                    velocities[i][j] = 0.0;
                    personalBests[i][j] = positions[i][j];
                }
            }


            for (int iter = 0; iter < iterationsMax; iter++) {
                double inertiaWeight = maxInertia - ((maxInertia - minInertia) * iter / iterationsMax);


                for (int i = 0; i < sizeOfSwarm; i++) {
                    double currentFitness = Rosenberg.evaluate(positions[i]);
                    double personalBestFitness = Rosenberg.evaluate(personalBests[i]);

                    if (currentFitness < personalBestFitness) {
                       // System.arraycopy(positions[i], 0, personalBests[i], 0, dimension);

                        for(int j = 0; j < dimension; j++) {
                            personalBests[i][j] = positions[i][j];
                        }
                    }


                    if (currentFitness < gBestFitness) {
                        //System.arraycopy(positions[i], 0, gBestPosition, 0, dimension);

                        for(int j = 0; j < dimension; j++) {
                            gBestPosition[j] = positions[i][j];
                        }
                        gBestFitness = currentFitness;
                    }
                }

                for (int i = 0; i < sizeOfSwarm; i++) {
                    for (int j = 0; j < dimension; j++) {
                        double r1 = random.nextDouble();
                        double r2 = random.nextDouble();

                        velocities[i][j] =
                                inertiaWeight * velocities[i][j]
                                        + cognitiveFactor * r1 * (personalBests[i][j] - positions[i][j])
                                        + socialFactor * r2 * (gBestPosition[j] - positions[i][j]);

                        positions[i][j] += velocities[i][j];
                    }
                }


                System.out.println("Iteration " + iter + " - Best Fitness: " + gBestFitness);
            }


            System.out.println("Total Fitness Evaluations: " + Rosenberg.count);
            System.out.println("Final Best Fitness: " + gBestFitness);
            System.out.print("Best Solution Found: ");
            for (double val : gBestPosition) {
                System.out.print(val + ", ");
            }
        }
    }}
