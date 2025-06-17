import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static final int MAX_ITR=100;
    static final int PARTICLE_SIZE=30;
    static final double W=0.5;
    static final double C1=2.0;
    static final double C2=2;
    static final double V_MAX=5.0;
    static final int LOWER_BOUND=-50;
    static final int UPPER_BOUND=50;
    static final int DIMENSIONS=5;

    static Random rnd=new Random();

    static class Particle{
        double[] position=new double[DIMENSIONS];
        double[] velocity=new double[DIMENSIONS];
        double[] bestPosition=new double[DIMENSIONS];
        double fitness;

        Particle(){
            for (int i = 0; i < DIMENSIONS; i++) {
                position[i]= rnd.nextDouble() * (UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
                velocity[i]= rnd.nextDouble() * 2 -1;
                bestPosition[i]= position[i];
            }
            fitness=fitnessFunction(position);

        }

         void update(double[] gBest){
             for (int i = 0; i < DIMENSIONS; i++) {
                     velocity[i]=W*velocity[i]+ C1*rnd.nextDouble() * (bestPosition[i] - position[i])+
                             C2*rnd.nextDouble()*(gBest[i]-position[i]);

                     position[i]+=velocity[i];

             }

             double fit = fitnessFunction(position);
             if (fit > fitness) {
                 fitness = fit;
                 bestPosition = position.clone();
             }
        }

    }


    static double fitnessFunction(double[] x) {
        double profit = 0;
        profit += 20 * x[0] - 0.2 * x[0] * x[0];
        profit += 15 * x[1] - 0.1 * x[1] * x[1];
        profit += 10 * x[2] - 0.05 * x[2] * x[2];
        profit += 18 * x[3] - 0.15 * x[3] * x[3];
        profit += 12 * x[4] - 0.1 * x[4] * x[4];
        return profit;
    }


    public static void main(String[] args) {
        Particle[] swarm=new Particle[PARTICLE_SIZE];
        for (int i = 0; i < swarm.length; i++) {
            swarm[i]=new Particle();
        }
        double[] gBest=swarm[0].bestPosition.clone();
        double gBestFitness=swarm[0].fitness;

        for (int i = 0; i < MAX_ITR; i++) {
            for(Particle p:swarm){
                p.update(gBest);

                if(p.fitness> gBestFitness){
                    gBest=p.bestPosition.clone();
                    gBestFitness=p.fitness;
                }
            }
            System.out.println("Iteration " + (i + 1) + ": Best Fitness = " + gBestFitness);
        }

    }
}