package cn.easybuy.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MapperUtils {
    private static MapperUtils mapperUtils;
    public static SqlSessionFactory sqlSessionFactory;

    private MapperUtils(){
        try {
            //获取配置文件
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            //获取工厂对象
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static MapperUtils getSqlSession(){
        if(mapperUtils == null){
            mapperUtils = new MapperUtils();
        }
       return mapperUtils;
    }
}
