package wy.com.write.util;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import wy.com.write.entity.SplitEntity;

@Service
public class WorkerDoSplitTask  extends WorkerDoTask<SplitEntity>{
	@Override
	public SplitEntity handle(SplitEntity o){
		HashMap<String, String> similarity = DeteleHtmlUtil.similarity(o.getContent());
		if(similarity ==null || similarity.isEmpty()){
			o.setJuzi_status(-1);
		}else{
			o.setJuzi_status(1);
			o.setMap(similarity);
		}
		return  o ; 
	}
}
