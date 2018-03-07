package com.zkh.dao;

import com.zkh.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    /**
     * 第一次 insetCount=1;
     * 第二次 insetCount=0;
     * 在运行schema.sql建数据表的时候 PRIMARY KEY (seckill_id,user_phone),联合主键，保证了id与电话号合成的唯一性
     * @throws Exception
     */
    @Test
    public void insertSuccessKilled() throws Exception {
        long id=1003L;
        long userPhone=15628309710L;
        int insertCount=successKilledDao.insertSuccessKilled(id,userPhone);
        System.out.println(insertCount);
    }

    @Test
    public void querySuccessKillWithSeckill() throws Exception {
        long id=1003L;
        long userPhone=15628309710L;
        SuccessKilled successKilled=successKilledDao.querySuccessKillWithSeckill(id,userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}