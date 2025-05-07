import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class GeneticRosenbrock {
    static final int d = 5;
    static final int p = 20;
    static final int c_number = (int)p/2;
    static final int T = 100;
    static final double cRate = 0.8;
    static final double mRate = 0.2;
    static final double l = -5;
    static final double u = 5;
    static final double mRange = 3;
    static Random rand = new Random();

    static class Individual {
        double[] genes = new double[d];
        double fitness;
        Individual() {
            for (int i = 0; i < d; i++) {
                genes[i] = l + (u - l) * rand.nextDouble();
            }
            fitness();
        }
        void fitness() {
            fitness = 0.0;
            for (int i = 0; i < d - 1; i++) {
                double xi = genes[i];
                double xi1 = genes[i + 1];
                fitness += 100 * Math.pow(xi1 - xi * xi, 2) + Math.pow(1 - xi, 2);
            }
        }

    }

    static Individual crossover(Individual parent1, Individual parent2) {
        Individual child = new Individual();
        for (int i = 0; i < d; i++) {
            child.genes[i] = rand.nextBoolean() ? parent1.genes[i] : parent2.genes[i];
        }
        return child;
    }

    static void mutate(Individual ind) {
        int idx = rand.nextInt(d);
        double delta = -mRange + 2 * mRange * rand.nextDouble();
        ind.genes[idx] += delta;
        if (ind.genes[idx] < l) ind.genes[idx] = l;
        if (ind.genes[idx] > u) ind.genes[idx] = u;
        ind.fitness();
    }

    public static void main(String[] args) {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < p; i++) {
            population.add(new Individual());
        }
        for (int generation = 0; generation < T; generation++) {
            List<Individual> children = new ArrayList<>();
            while(children.size() < c_number) {
                if (rand.nextDouble() < cRate) {
                    Individual parent1 = population.get(rand.nextInt(population.size()));
                    Individual parent2 = population.get(rand.nextInt(population.size()));
                    Individual child;
                    child = crossover(parent1, parent2);
                    if (rand.nextDouble() < mRate) {
                        mutate(child);
                    }
                    children.add(child);
                }
            }
            population.addAll(children);
            List<Individual> newPopulation = new ArrayList<>();
            while (newPopulation.size() < p) {
                Individual a = population.get(rand.nextInt(population.size()));
                Individual b = population.get(rand.nextInt(population.size()));
                if(a.fitness < b.fitness)
                {
                    newPopulation.add(a);
                    population.remove(a);
                }
                else
                {
                    newPopulation.add(b);
                    population.remove(b);
                }
            }
            population = newPopulation;
            System.out.printf("Generation %d: Best fitness = %.6f%n", generation, population.get(0).fitness);
        }
        System.out.println("Final Population:");
        for (int i = 0; i < population.size(); i++) {
            Individual ind = population.get(i);
            System.out.printf("Individual %2d: Genes = %s, Fitness = %.6f%n",
                    i + 1, Arrays.toString(ind.genes), ind.fitness);
        }
    }
}