import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Random rnd = new Random();

        double[] rndArr = rnd.doubles(10, -30, 31).toArray();


        int maxItr = 100;
        int iteration = 0;
        double[] newSol;
        final double TEMPERATURE = 100;
        final double coefficient = 0.99;
        double t = TEMPERATURE;

        while (iteration < maxItr || t==0) {
            t *= coefficient;

            newSol=Movement(rndArr);

            if(Fitness(newSol)< Fitness(rndArr)){

                rndArr= newSol.clone();

            }
            else{
                double delta= Fitness(newSol)-Fitness(rndArr);
                double randNumber=new Random().nextDouble(1);
                if(randNumber< Math.exp(-delta/t)){
                    rndArr= newSol.clone();
                }
            }


            System.out.println("Iteration: " + iteration + " Fitness:" + Fitness(rndArr));

            iteration++;
        }

    }
        static double Fitness(double[] arr){
            int result=0;

            for (int i = 0; i < arr.length; i++) {
                result+= Math.pow(arr[i]+0.5,2);
            }
            return result;
        }

        static double[] Movement(double[] arr){

            double[] newArr=arr.clone();

            int rndPlus= new Random().nextInt(6)-5;
            int rndIndex=new Random().nextInt(arr.length);
            newArr[rndIndex]+=rndPlus;
            if(newArr[rndIndex]>30){
                newArr[rndIndex]=30;
            } else if (newArr[rndIndex]<-30) {
                newArr[rndIndex]=-30;
            }
            return newArr;
        }
}