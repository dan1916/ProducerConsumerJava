package producerconsumer;

/*
 * Demos a producer consumer program where a producer is first producing
 anywhere from 0 to 10 random integers in a shared array, waits for the
 consumer. Then the consumer consumes it and again waits for the producer.
 */
public class Demo {

    public static void main(String[] args) {

        //Define the sharred array...
        int[] sharedArray = new int[10];

        //Allocate one producer and one consumer...
        Producer p = new Producer(sharedArray);
        Consumer c = new Consumer(sharedArray);

        //Start them all...
        p.start();
        c.start();
    }

}
