package wy.com.write.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wy.com.write.dao.SimilarityDao;
import wy.com.write.entity.SplitEntity;

@Service
public class WorkerDoSplitUpdate extends WorkerDoUpdate<SplitEntity>{
	private SimilarityDao dao;
	
	
	
	
	public WorkerDoSplitUpdate() {
	}


	public WorkerDoSplitUpdate(SimilarityDao dao) {
		 this.dao=dao;
	}


	@Transactional("job_zb")
	@Override
	public SplitEntity handle(SplitEntity obj) {
		 ArrayList<Object[]> params = new ArrayList<Object[]>();
		 HashMap<String,String> map = obj.getMap();
		for (Entry<String, String> entry : map.entrySet()) {
			params.add(new Object[]{obj.getTask_id(),entry.getKey(),entry.getValue(),obj.getJuzi_status()});
		}
		dao.insertSplitResult(params,obj.getJuzi_status(),obj.getTask_id());
		System.out.println("update one test Exception");
		return  obj;
	}
	
}
