import java.lang.reflect.Array;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Random rnd=new Random();

        int[] rndArr=rnd.ints(10,-100,101).toArray();


        
        int maxItr=1000;
        int iteration=0;
        int[] newSol;
        while (iteration<maxItr){

            newSol=Movement(rndArr).clone();

            if(Fitness(newSol)< Fitness(rndArr)){

               rndArr= newSol.clone();

            }
            System.out.println("Iteration: "+iteration+" Fitness:"+Fitness(rndArr));
            iteration++;
        }
    }
    
    static int Fitness(int[] arr){
        int result=0;

        for (int i = 0; i < arr.length; i++) {
            result+=(arr[i] * arr[i]);
        }
        return result;
    }
    
    static int[] Movement(int[] arr){

        int[] newArr=arr.clone();

        int rndPlus= new Random().nextInt(6)-5;
        int rndIndex=new Random().nextInt(arr.length);
        newArr[rndIndex]+=rndPlus;
        if(newArr[rndIndex]>100){
            newArr[rndIndex]=100;
        } else if (newArr[rndIndex]<-100) {
            newArr[rndIndex]=-100;
        }
        return newArr;
    }
    
}