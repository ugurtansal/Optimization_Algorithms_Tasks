import java.util.Random;



         class ContinuousGWO_Schwefel {

            static int dim = 5;
            static int n_agents = 20;
            static int max_iter = 100;
            static double lowerBound = -500;
            static double upperBound = 500;

            static double[][] positions = new double[n_agents][dim];
            static double[] Alpha_pos = new double[dim];
            static double[] Beta_pos = new double[dim];
            static double[] Delta_pos = new double[dim];
            static double Alpha_score = Double.MAX_VALUE;
            static double Beta_score = Double.MAX_VALUE;
            static double Delta_score = Double.MAX_VALUE;

            static Random rand = new Random();

            public static void main(String[] args) {
                initializeAgents();

                for (int t = 0; t < max_iter; t++) {
                    for (int i = 0; i < n_agents; i++) {
                        double fitness = schwefel(positions[i]);// call schwefel
                        if (fitness < Alpha_score) {
                            Alpha_score = fitness;
                            Alpha_pos = positions[i].clone();
                        } else if (fitness< Beta_score) {
                            Beta_score = fitness;
                            Beta_pos = positions[i].clone();
                        } else if (fitness < Delta_score) {
                            Delta_score = fitness;
                            Delta_pos = positions[i].clone();
                        }
                    }

                    double a = 2.0 - ((double) t * (2.0 / max_iter));

                    for (int i = 0; i < n_agents; i++) {
                        for (int j = 0; j < dim; j++) {
                            //calculate new x for Alpha

                            double r1 = rand.nextDouble();
                            double A1 = 2 * a * r1 - a;

                            double r2 = rand.nextDouble();
                            double C1 = 2 * r2;


                            double D_alpha = Math.abs(C1 * Alpha_pos[j] - positions[i][j]);
                            double X1 = Alpha_pos[j] - A1 * D_alpha;


                            //calculate new x for Beta
                            double r3 = rand.nextDouble();
                            double A2 = 2 * a * r3 - a;


                            double r4 = rand.nextDouble();
                            double C2 = 2 * r4;


                            double D_beta = Math.abs(C2 * Beta_pos[j] - positions[i][j]);
                            double X2 = Beta_pos[j] - A2 * D_beta;

                            //calculate new x for Delta
                            double r5 = rand.nextDouble();
                            double A3 = 2 * a * r5 - a;


                            double r6 = rand.nextDouble();
                            double C3 = 2 * r6;


                            double D_delta = Math.abs(C3 * Delta_pos[j] - positions[i][j]);
                            double X3 = Delta_pos[j] - A3 * D_delta;

                            positions[i][j] = (X1 + X2 + X3) / 3.0;

                            // check bounds
                            if (positions[i][j] < lowerBound) {
                                positions[i][j] = lowerBound;
                            } else if (positions[i][j] > upperBound) {
                                positions[i][j] = upperBound;
                            }
                        }
                    }

                    System.out.printf("Iter %d/%d - Best Fitness: %.6f%n", t + 1, max_iter, Alpha_score);
                }

                System.out.println("Best solution:");
                for (int i = 0; i < dim; i++) {
                    System.out.printf("x[%d] = %.6f ", i, Alpha_pos[i]);
                }
                System.out.printf("%nMinimum value: %.6f%n", Alpha_score);
            }

            static void initializeAgents() {
                for (int i = 0; i < n_agents; i++) {
                    for (int j = 0; j < dim; j++) {
                        positions[i][j] = rand.nextDouble() * (upperBound - lowerBound) + lowerBound;
                    }
                }

            }

            static double schwefel(double[] x) {

                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += x[i] * Math.sin(Math.sqrt(Math.abs(x[i])));
                }
                return 418.9829 * x.length - sum;
            }
        }
