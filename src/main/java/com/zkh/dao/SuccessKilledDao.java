package com.zkh.dao;

import com.zkh.entity.SuccessKilled;

public interface SuccessKilledDao {

    /**
     * 插入成功秒杀的明细,可过滤重复
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(long seckillId, long userPhone);


    /**
     *通过seckillId查询successkilled并携带秒杀产品实体对象
     * @param seckillId
     * @return
     */
    SuccessKilled querySuccessKillWithSeckill(long seckillId);

}
