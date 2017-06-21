package wy.com.write.util;

import java.util.Queue;

import org.springframework.stereotype.Service;

@Service
public abstract class Worker<E> implements Runnable {

	
	public void setUpdateQueue(Queue<E> updateQueue) {
	}

	public void setWorkQueue(Queue<E> workQueue) {
		
	}
	
}
