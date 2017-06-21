package wy.com.write.util;

import java.util.Queue;

import org.springframework.stereotype.Service;

@Service
public class WorkerDoTask<E> extends Worker<E>{

	//任务队列，用于取得子任务
	protected Queue<E> workQueue;
	
	//完成任务后的队列 ||上报队列
	protected Queue<E> updateQueue ;

	public void setWorkQueue(Queue<E> workQueue){
		this.workQueue=workQueue;
	}
	
	public void setUpdateQueue(Queue<E> updateQueue ) {
		this.updateQueue = updateQueue;
	}

	//子任务处理逻辑，在子类中实现具体逻辑
	public E handle(E obj){
		return obj;
	}


	@Override
	public void run() {
		long count  =1000l;
			while(true){
				E poll = workQueue.poll();
				if(poll ==null){
					try {
						Thread.sleep(count+=1000);
						System.out.println("worker: workQueue is null ,stop "+count+"times(millisecond)");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				}else{
					count=1000;
					//处理子任务
					E re =handle(poll);
					//将处理结果写入结果集
					updateQueue.add(re);
					System.out.println("worker: updateQueue is success");
				}
		  }
	}
}
