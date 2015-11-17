package producerconsumer;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Producer is responsible for producing random amount of random numbers
 * and wait till the consume consumes them all and then repeat it.
 *
 * @author Dhavalkumar Suthar
 */
public class Producer extends Thread {

    //This will hold the shared array...
    int[] sharedArray;

    //Initializes a new instance of Producer thread.
    public Producer(int[] sharedArray) {
        this.sharedArray = sharedArray;
    }

    //Our sync object will be used while entering the synchronized code block..
    public static Object syncLockObj = new Object();
    
    //This will be read by the consumer, letting him know how many numbers we generated.
    public static int howManyNumbers;

    //Starts the producer job.
    public void run() {
        
        //We'll keep on repeating 2 times...
        int times = 0;
        while (times < 2) {
            
            //Init a random int generator.
            Random rand = new Random();

            //First let the consumer know how many numbers we're going to generate.
            synchronized (syncLockObj) {
                howManyNumbers = rand.nextInt(11);
                Producer.syncLockObj.notify(); //Notify waiting consumer...

                //And then we wait till we get a signal from him...
                try {
                    Producer.syncLockObj.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            //Finally, we have to produce random numbers and add to array..
            try {
                synchronized (syncLockObj) {
                    for (int i = 0; i < howManyNumbers; i++) {
                        sharedArray[i] = rand.nextInt(101);
                        System.out.println("Producer Put :" + sharedArray[i]);
                    }

                    //Signal the consumer that we're done.
                    Producer.syncLockObj.notify();
                    
                    //And wait till the consumer has read them all...
                    Producer.syncLockObj.wait();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            times++;
        }
        
        System.out.println("Producer Done !");
    }
}
