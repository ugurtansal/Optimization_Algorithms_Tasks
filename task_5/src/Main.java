import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args) {
        int maxIt = 100;
        int dim = 3;
        int np = 20;
        float c1 = 2.0f, c2 = 2.0f;
        int[] area = {3,5,7};
        int maxArea = 400;
        int[] profit = {10,20,30};
        int[][] p = new int[np][dim];
        int[][] pBest = new int[np][dim];
        int[] gBest = new int[dim];
        double gFit = 0;
        double[][] v = new double[np][dim];
        for (int i = 0; i < np; i++) {
            do{
                for (int j = 0; j < dim; j++) {
                    p[i][j] =  (int)Math.ceil(Math.random() *5);
                    pBest[i][j] = p[i][j];
                    v[i][j] = 0;
                }
            }while( (constraint1(p[i], area,maxArea) &&constraint2(p[i]))==false);
        }
        for (int t = 0; t < maxIt; t++) {
            for (int i = 0; i < np; i++) {
                int currentFitness = fitness(p[i], profit);
                int personalBestFitness = fitness(pBest[i],profit);
                if (personalBestFitness < currentFitness && constraint1(p[i], area,maxArea) &&constraint2(p[i]) ) {
                    for(int j=0 ; j <dim;j++)
                        pBest[i][j] = p[i][j];
                }
                if (gFit < currentFitness && constraint1(p[i], area,maxArea) &&constraint2(p[i])) {
                    for(int j=0 ; j <dim;j++)
                        gBest[j] = p[i][j];
                    gFit = currentFitness;
                }
            }
            System.out.println("Iteration " + (t + 1) + ": Best Fitness = " + gFit);
            for (int i = 0; i < np; i++) {
                for (int j = 0; j < dim; j++) {
                    double r1 = Math.random();
                    double r2 = Math.random();
                    v[i][j] = v[i][j]
                            + c1 * r1 * (pBest[i][j] - p[i][j])
                            + c2 * r2 * (gBest[j] - p[i][j]);
                    p[i][j] += (int)Math.ceil(v[i][j]);
                }
            }
        }

        System.out.print("Best Solution: ");
        for (int j = 0; j < dim; j++) {
            System.out.print(gBest[j] + (j < dim - 1 ? ", " : "\n"));
        }
    }

    public static int fitness(int[] x, int[] profit) {
        int sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * profit[i];
        }
        return sum;
    }
    public static boolean constraint1(int[] x, int[] area, int maxArea) {
        int sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * area[i];
        }
        if( sum <=maxArea)
            return true;
        return false;
    }
    public static boolean constraint2(int[] x) {
        int sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        for (int i = 0; i < x.length; i++) {
            if(x[i] > (double)sum/2)
                return false;
        }
        return true;
    }


}



