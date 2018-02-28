package com.zkh.dao;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.zkh.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.security.SecureClassLoader;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * 添加spring-test，junit方便测试的依赖
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit  spring的配置
@ContextConfiguration(locations = {"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void queryById() throws Exception {
        long id=1000;
        Seckill seckill=seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception {
        /**
         * Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
         *  List<Seckill> queryAll(long offset, long limit);
         *  java没有保存形参的记录  queryAll(long offset, long limit)--->queryAll(arg0, arg1)
         *  用mybatis提供的@param注解来帮助mybatis记录参数的含义。写在SeckillDao的接口参数表上
         */
        List<Seckill> seckillList=seckillDao.queryAll(0,100);
        for (Seckill seckill:seckillList){
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        /**
         * 同样这里的两个参数也需要用mybatis  @param的注解来记录参数含义，以便mybatis识别
         */
        Date killTime=new Date();
        int updateCount=seckillDao.reduceNumber(1000L,killTime);
        System.out.println(updateCount);
    }


}