package com.sachablade.example;

import java.io.IOException;

import com.sachablade.batch.JobScheduler;

public class JobSchedulerExample extends JobScheduler {
	static String path="/home/juanma/MyWorkspace/myLibrary/src/com/sachablade/settings/exampleSettings.json";
	
	public JobSchedulerExample(int Threads) throws ClassNotFoundException, NoSuchMethodException, IOException {
		super(Threads,path);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException {
		new JobSchedulerExample (10);

	}

}
