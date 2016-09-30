package myLibrary;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayBlockingQueueExample {
	
	public class Producer implements Runnable{	    
	    private BlockingQueue queue;
	    
	    public Producer(BlockingQueue queue) {
	        this.queue = queue;
	    }

	    @Override
	    public void run() {	        
	        // We are adding elements using offer() in order to check if
	        // it actually managed to insert them.
	        for (int i = 0; i < 80; i++) {
	            System.out.println("Trying to add to queue: String " + i +
	                    " and the result was " + queue.offer("String " + i));	            
	            try {  
	                Thread.sleep(500);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	public class Consumer implements Runnable{	    
	    private BlockingQueue queue;	    
	    public Consumer(BlockingQueue queue) {
	    	this.queue = queue;
	    }

	    @Override
	    public void run() {	        
	        // As long as there are empty positions in our array,
	        // we want to check what's going on.
	        while (queue.remainingCapacity() > 0) {
	            try {
					System.out.println("Queue size: " + queue.size() +
					        ", remaining capacity: " + queue.remainingCapacity() + queue.take());
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	            
	            try {
	                Thread.sleep(500);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	public static void main(String[] args) {
		
		ArrayBlockingQueueExample thiss=new ArrayBlockingQueueExample();
		// TODO Auto-generated method stub
		 // Let's create a blocking queue that can hold at most 5 elements.
        BlockingQueue queue = new ArrayBlockingQueue<>(50);
        
        // The two threads will access the same queue, in order
        // to test its blocking capabilities.
        Thread producer = new Thread(thiss.new Producer(queue));
        Thread consumer = new Thread(thiss.new Consumer(queue));
        
        producer.start();
        consumer.start();
	}

}
