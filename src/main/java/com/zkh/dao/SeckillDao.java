package com.zkh.dao;

import com.zkh.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    /**
     *减库存
     * @param seckillId
     * @param killedTime
     * @return
     */
    int reduceNumber(long seckillId, Date killedTime);


    /**
     * 查询，通过秒杀Id去查询该Id商品库存
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 通过偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(long offset, long limit);

    /**
     * 添加记录
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     */
    void addNumber(String name, int number, Date startTime, Date endTime);
}
