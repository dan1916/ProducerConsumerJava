/*
 * Demos a producer consumer program where a producer is first producing
 anywhere from 0 to 10 random integers in a shared array, waits for the
 consumer. Then the consumer consumes it and again waits for the producer.
 */
package producerconsumer;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static producerconsumer.Producer.howManyNumbers;

/**
 * Consumer is responsible for waiting till the producer produces and then
 * consume it.
 *
 * @author Dhavalkumar Suthar
 */
public class Consumer extends Thread {

    //This will hold the shared array...
    private int[] sharedArray;

    //Initializes a new instance of consumer thread.
    public Consumer(int[] sharedArray) {
        this.sharedArray = sharedArray;
    }

    //Starts the consumer job.
    public void run() {
        
        //We'll keep on repeating 2 times...
        int times = 0;
        while (times < 2) {
            
            //Enter the synchronized on the lock obj of producer.
            synchronized (Producer.syncLockObj) {
                try {
                    //First wait for the producer to notify us how many numbers we're going to get.             
                    Producer.syncLockObj.wait();

                    //Then read how many numbers producer has generated...
                    int howManyNubs = Producer.howManyNumbers;
                    
                    //Notify the producer that he can start producing...
                    Producer.syncLockObj.notify();

                    //Wait till all numbers are generated...
                    Producer.syncLockObj.wait();

                    //And now print all numbers...
                    for (int i = 0; i < Producer.howManyNumbers; i++) {
                        System.out.println("Consumer Got :" + sharedArray[i]);
                    }

                    //Notify the producer that we're done reading...
                    Producer.syncLockObj.notify();

                } catch (InterruptedException ex) {
                    Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            times++;
        }
        
         System.out.println("Consumer Done !");
    }
}
