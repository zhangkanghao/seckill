package com.zkh.dao;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.zkh.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;


/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * 添加spring-test，junit方便测试的依赖
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit  spring的配置
@ContextConfiguration(locations = {"classpath:spring/spring-dao.xml"})
class SeckillDaoTest {
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
    }

    @Test
    public void reduceNumber() throws Exception {
    }


}