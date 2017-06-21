package wy.com.write.util;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface GetTask<T> {
    List<T> getTask(int begin,int step);
}
