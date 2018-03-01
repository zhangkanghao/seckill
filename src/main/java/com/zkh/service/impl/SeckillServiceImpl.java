package com.zkh.service.impl;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import com.zkh.dao.SeckillDao;
import com.zkh.dao.SuccessKilledDao;
import com.zkh.dto.Exposer;
import com.zkh.entity.Seckill;
import com.zkh.excepetion.RepeatKillException;
import com.zkh.excepetion.SeckillCloseException;
import com.zkh.excepetion.SeckillException;
import com.zkh.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 *
 */

public class SeckillServiceImpl implements SeckillService{

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    private SeckillDao seckillDao;

    private SuccessKilledDao successKilledDao;

    //md5盐值字符串，用于混淆md5，在用户不知道拼接方式时，加上这串字符串生成md5几乎不能破解，越乱越好
    private final String slat="LFASKHFA*S%*&@!)jlkfsahflksya9das*/das6";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill=seckillDao.queryById(seckillId);
        if(seckill==null){
            return new Exposer(false,seckillId);
        }
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        //系统当前时间
        Date nowTime =new Date();
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,startTime.getTime(),endTime.getTime(),nowTime.getTime());
        }

        String md5=null;getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base=seckillId+"/"+slat;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    public void excuteSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {

    }
}
