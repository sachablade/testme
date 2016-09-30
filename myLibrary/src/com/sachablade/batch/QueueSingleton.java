package com.sachablade.batch;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class QueueSingleton {
	
	static Queue<Object> queue = new LinkedList<Object>();	
	
	private static QueueSingleton queueInstance = null;	
	
	public static QueueSingleton getInstance() { 
		if (queueInstance == null) {
			queueInstance = new QueueSingleton();
		}
		return queueInstance;
	} 
	public Queue<Object> get() {
		return queue;
	}
 
	// Inserts the specified element into this queue if it is possible to do so
	// immediately without violating capacity restrictions
	public void add(Object value) {
		synchronized (queue) {
			queue.add(value);
		}
	}
 
	public void addAll(List listValues) {
		for (Object temp : listValues) {
			queue.add(temp);
		}
	}
	// Removes a single instance of the specified element from this collection
	public void remove(Object value) {
		synchronized (queue) {
			queue.remove(value);
		}
	}
 
	// Retrieves and removes the head of this queue, or returns null if this
	// queue is empty.
	public Object poll() {
		Object data = queue.poll();
		return data;
	}
 
	// Returns true if this collection contains no elements
	public boolean isEmpty() {
		return queue.isEmpty();
	}
 
	// Returns the number of elements in this collection. If this collection
	// contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE
	public int getTotalSize() {
		return queue.size();
	}
}
