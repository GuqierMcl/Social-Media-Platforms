package com.thirteen.smp.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * MyBatis工具类
 * @author 顾
 * @version 1.0
 * @since 1.0
 */
public class SqlSessionUtil {

    private SqlSessionUtil(){

    }

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ThreadLocal<SqlSession> local = new ThreadLocal<>();

    /**
     * 获取会话对象
     * @return 会话对象
     */
    public static SqlSession openSession(){
        SqlSession sqlSession = local.get();
        if(sqlSession == null){
            sqlSession = sqlSessionFactory.openSession();
            local.set(sqlSession);// 绑定对象到当前线程
        }
        return sqlSession;
    }

    /**
     * 关闭SqlSession对象
     * @param sqlSession
     */
    public static void close(SqlSession sqlSession){
        if (sqlSession != null) {
            sqlSession.close();
            local.remove();
        }
    }
}
