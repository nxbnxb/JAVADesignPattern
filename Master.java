package wy.com.write.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

@Service
public class Master<E> {
	
	@SuppressWarnings("unused")
	private Master() {
	}

	//开启的线程数
	protected int countWorker;
	
	//worker进程队列  线程池
	protected ExecutorService exe = Executors.newCachedThreadPool();
	
	//任务队列
	protected Queue<E> workQueue =new ConcurrentLinkedQueue<E>();
	
//	//子任务处理队列
//	protected Map<String, Object> resultMap = new ConcurrentHashMap<String,Object>();
	
	//完成任务后的队列 ||上报队列
	protected Queue<E> updateQueue =new ConcurrentLinkedQueue<E>();
	
	//用于存储worker；
	protected List<Worker<E>> workerList =new ArrayList<Worker<E>>();
	
	//是否所有的子任务都结束了
	public boolean isComplete(){
		exe.isShutdown();//启动一次顺序关闭，执行以前提交的任务，但不接受新任务。 使 isTerminated() 生效效果待测试//TODO 
		return exe.isTerminated();
	}
	
	
	//master 的构造器 需要一个Worker进程逻辑 和 需要的Worker的进程数量
	public Master(Worker<E> worker,int countWorker){
		worker.setWorkQueue(workQueue);
		worker.setUpdateQueue(updateQueue);
		for (int i = 0; i < countWorker; i++) {
			workerList.add(worker);
		}
		this.countWorker=countWorker;
	}
	
	
	//提交一个任务
	public void submit (E obj){
		workQueue.add(obj);
	}
	
	
	//提交一个任务
	public  void submit (Collection<? extends E> c){
		workQueue.addAll(c);
	}
	
	/**
	 *   获取并移除此队列的头
	 * @return
	 * 2017年6月9日
	 */
	public E poll(){
		return updateQueue.poll();
	}  
	
	
	//返回子任务集合
	public  Queue<E> getResultQueue(){
		return updateQueue;
	}
	
//	CountDownLatch cdl = new CountDownLatch(threadMap.size());
			
	
	//开始运行所有的worker进程，进行处理
	public void execute(){
		for (Worker<E> work : workerList) {
			exe.execute(work);
		}
	}
	
	/**
	 * 关闭线程池
	 * 2017年6月9日
	 */
	public void shutDown(){
		exe.shutdown();
		System.out.println("the ThreadPool is shutdown");
	}
	
}
