package com.zkh.service;

import com.zkh.dto.Exposer;
import com.zkh.dto.SeckillExecution;
import com.zkh.entity.Seckill;
import com.zkh.excepetion.RepeatKillException;
import com.zkh.excepetion.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;


/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;


    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills=seckillService.getSeckillList();
        logger.info("list={}",seckills);
    }

    @Test
    public void getById() throws Exception {
        long id=1000;
        Seckill seckill=seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testSeckillService() throws Exception {
        long id=1000;
        long phone=12345678921L;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()) {
            logger.info("exposer={}",exposer);
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.excuteSeckill(id, phone, md5);
                logger.info("seckillExcution={}", seckillExecution);
            } catch (RepeatKillException e) {
                logger.error("exposer={}",exposer);
            } catch (SeckillCloseException e) {
                logger.error("exposer={}",exposer );
            }
        }else {
            logger.warn("exposer={}",exposer);
        }
    }

}