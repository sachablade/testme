package com.sachablade.batch;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.sachablade.settings.Settings;


public abstract class WorkQueue {
	
	protected static final Logger logger = LogManager.getLogger("");
	
	private final PoolWorker[] threads;
	private final QueueSingleton queue;
	private boolean debugMode;
	private JSONObject settings;

	private int queueSize = 0;
	boolean isAlive = true;
	Date startDate;

	
	int nThreads=10;
	JSONObject data;
	
	public abstract ArrayList<Object> retriveTask(JSONObject input) throws Exception;

	public abstract Object runTask(Object Task) throws Exception;
	
	public abstract void finalizeTask(Object Task) throws Exception;	

	private void readSettings(String[] args) throws Exception {		
		
		Options options = new Options();		
		
		Option properties = new Option("s", "settings", true, "Settings file definition");
		properties.setRequired(true);
        options.addOption(properties);
        
        Option debug = new Option("d", "debug", false, "debug mode");
        debug.setRequired(false);
        options.addOption(debug);

        Option nthreads = new Option("n", "threads", true, "Threads number");
        nthreads.setRequired(false);
        options.addOption(nthreads);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
            return;
        }
        
        debugMode=cmd.hasOption("debug");
        String settingPath=cmd.getOptionValue("settings"); 
        
		if(settings==null){
			//settings=Settings.getSettings(settingPath);
			String  program = settings.getString("program");
			System.out.println("Executing " + program);
			Integer threads=settings.getInt("threads");
			if(threads!=null){
				nThreads=threads;
			}
			
		}
		
		String className=null;
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null) {
			className=enclosingClass.getName();
		} else {
			className=getClass().getName();
		}
		
        /*String inputFilePath = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");

        System.out.println(inputFilePath);
        System.out.println(outputFilePath);*/
		
	};
	
	public WorkQueue(String[] args, String className) throws Exception {		
		
		readSettings(args);		
		queue = QueueSingleton.getInstance();
		queue.addAll(retriveTask(data));		
		queueSize = queue.getTotalSize();
		System.out.println("Se han registrado " + queueSize + " elementos");
		startDate = new Date();

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
							Object myTask=runTask(obj);
							finalizeTask(myTask);
							
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