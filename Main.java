 package wy.com.write.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import wy.com.write.dao.SimilarityDao;
import wy.com.write.entity.SplitEntity;

@Service("SimilarityMain")
public class  Main {
	
	
	@Autowired
	private SimilarityDao dao;
	
	/*private String getSql;//获取执行任务的sql
	
	private String updateSql;//上报任务的sql
	
	private int getNum;//每次获取多少任务做
	
	private int updateNum;//多少个任务一次上报
	
	private int doTaskCountWorker;//做任务线程数
	
	private int updateTaskCountWorker;//上报任务的线程数
*/	
	/**
	 * 
	 * @param doTaskCountWorker 做任务线程数
	 * @param getSql 获取操作任务的sql
	 * @param getDBUrl 获取任务的数据库地址
	 * @param getNum  每次获取多少任务做
	 * @param updateTaskCountWorker 上报任务的线程数
	 * @param updateSql  上报任务的sql
	 * @param updateDBUrl 上报的数据库地址
	 * @param updateNum 多少个任务一次上报
	 */
	public  Main(int doTaskCountWorker,String getSql,String getDBUrl,int getNum,int updateTaskCountWorker,String updateSql,String updateDBUrl,int updateNum){
		
	}
	
	public Main(){
		/*try {   
            //文件生成路径   
            PrintStream ps=new PrintStream("D:\\Thread.txt");   
            System.setOut(ps);   
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
        }  */
	}
	public void main(){
		Master<SplitEntity> master = new Master<SplitEntity>(new WorkerDoSplitTask(), 5);//固定5个Worker，并制定Worker
		Master<SplitEntity> update = new Master<SplitEntity>(new WorkerDoSplitUpdate(dao), 2);//固定5个Worker，并制定Worker
		doTask(master,update);
	}
	
	public   void doTask(Master<SplitEntity> master,Master<SplitEntity> update){
		int begin =0;
		int step =500;
		while(true){
			//从数据库取出数据
			List<SplitEntity> task = dao.GetSpiltTask(begin, step);
			begin+=step;
			//
			if(task ==null || task.isEmpty()){
				System.out.println("get Task  list  is null");
				System.out.println("the get task is null ,application is off");
				break;
			}else{
				master.submit(task);
			}
			master.execute();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	/*		if(master.isComplete()){
				master.shutDown();
			}*/
			int taskSize=task.size();
		
	    	for (int i = 0 ; i<taskSize;i++) {
	    		SplitEntity poll = master.poll();
			    if(poll ==null ){
			    	update.execute();
			    }else{
			    	update.submit(poll);
			    }
			}
		    update.execute();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
/*			if(update.isComplete()){
				update.shutDown();
			}
*/			System.out.println("master do size:"+master.workQueue.size());
			System.out.println("master utask size:"+master.updateQueue.size());
			System.out.println("update work size:"+update.workQueue.size());
		}
		if(update.isComplete()){
			update.shutDown();
		}
		if(master.isComplete()){
			master.shutDown();
		}
	}
	
	


	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:root-context.xml");
		final Main main =(Main)context.getBean("SimilarityMain");
		main.main();
	}
}
