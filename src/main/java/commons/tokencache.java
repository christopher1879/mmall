package commons;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;





public class tokencache {
private static Logger logger =LoggerFactory.getLogger(tokencache.class);


//LRU算法
public static LoadingCache<String,String> localcache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {



    @Override

    public String load(String s) throws Exception {
        return "null";


    }


    public static   void setkey(String key, String value){
        localcache.put(key,value);
    }



    public static String getkey(String key){
        String value= null;
        try {
            value= localcache.get(key);
            if ("null".equals(value))
                return null;

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}

}
