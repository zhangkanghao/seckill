package com.zkh.dao;

import com.zkh.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    /**
     *减库存
     * @param seckillId
     * @param killedTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killedTime") Date killedTime);


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
    List<Seckill> queryAll(@Param("offset") long offset,@Param("limit") long limit);

    /**
     * 添加记录
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     */
    void addNumber(String name, int number, Date startTime, Date endTime);
}
