package com.zkh.entity;

import java.util.Date;

public class SuccessKilled {

    private long seckillId;

    /**
     * 这里数据库中类型是long，在定义POJO的时候要定义为String类型

     因为如过定义为long的话，它去查询的时候会默认给你转换为科学记数法

     所以就会报如上的错误了。
     */
    private String userPhone;

    private short state;

    private Date createTime;


    //变通
    //有多对一的情况，多条秒杀成功的记录对应一条seckill实体
    private Seckill seckill;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public String  getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                ", seckill=" + seckill +
                '}';
    }
}
