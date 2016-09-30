package com.sachablade.batch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.sachablade.settings.Settings;

public class JobScheduler {
	
	private final QueueSingleton queue;
	private PoolWorker[] threads;
	private Settings settings;
	
	private int nThreads;
	private boolean isAlive;

	public JobScheduler(int Threads, String pathSettings) throws IOException, ClassNotFoundException, NoSuchMethodException {		
		queue=QueueSingleton.getInstance();
		nThreads=Threads;
		isAlive = true;	
		
		/*Settings validation*/
		settings = new Settings(pathSettings);
		try {
			startJobs ();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void startJobs () throws InterruptedException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		TaskObjectExecutor refTaskObjectExecutor=settings.getTask(0);
		refTaskObjectExecutor.Execute();
		threads = new PoolWorker[nThreads];
		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
		/* Queda a la espera de que finalicen todos los hilos */
		while (isAlive) {
			Thread.sleep(1000);			
			for (int i = 0; i < nThreads; i++) {
				if (threads[i].isAlive()) {
					isAlive = true;
					break;
				}
				isAlive = false;
			}
		}
		System.exit(0);
	}
	
	private class PoolWorker extends Thread {		
		public void run() {
			while (!queue.isEmpty()) {
				synchronized (queue) {
					if (!queue.isEmpty()) {
						Object obj = queue.poll();						
						try {
							
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					queue.notifyAll();
				}
			}
		}
	}

}
