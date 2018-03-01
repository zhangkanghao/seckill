package com.zkh.service;

import com.zkh.dto.Exposer;
import com.zkh.entity.Seckill;
import com.zkh.excepetion.RepeatKillException;
import com.zkh.excepetion.SeckillCloseException;
import com.zkh.excepetion.SeckillException;

import java.util.List;

/**
 * 业务接口：站在使用者角度设计接口
 * 1.方法定义粒度明确：比如秒杀，要有执行秒杀的接口和参数，而不是关注怎么减库存等
 * 2.参数：越简洁越好
 * 3.返回类型：return的类型要友好，不要返回一些Map类型的，要简单比如ENtity      允许异常
 */

public interface SeckillService {

    /**
     * 查询所有秒杀商品列表
     * @return
     */
    List<Seckill>  getSeckillList();


    /**
     * 查询单个秒杀商品
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址，
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 验证与内部md5是否一致，
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    void excuteSeckill(long seckillId, long userPhone,String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
}
