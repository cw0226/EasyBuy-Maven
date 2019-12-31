package cn.easybuy.utils;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdqn on 2016/5/10.
 */
public class MemcachedUtils {

    static MemCachedClient client = null;

    static String[] connectUrls = new String[]{"127.0.0.1:11211"};

    static {
        String[] attr = connectUrls;
        client = new MemCachedClient();
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(attr);
        pool.setWeights(new Integer[]{3});
        pool.setInitConn(5);
        pool.setMinConn(5);
        pool.setMaxConn(200);
        pool.setMaxIdle(1000 * 30 * 30);
        pool.setMaintSleep(30);
        pool.setNagle(false);
        pool.setSocketConnectTO(30);
        pool.initialize();
    }

    public static void add(String key, Object object) {
        client.set(key, object);
    }

    public static void del(String key) {
        client.delete(key);
    }

    public static Object get(String key) {
        return client.get(key);
    }

    public static void main(String args[]) {
        List<String> name = new ArrayList<String>();
        name.add("1111");
        name.add("2222");
        name.add("3333");
        name.add("4444");
        name.add("5555");
        name.add("6666");
        add("name", name);
        List<String> test = (List<String>) get("name");
        System.out.print(test);
    }
}
