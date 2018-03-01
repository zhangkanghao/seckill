package com.zkh.service.impl;

import com.zkh.dao.SeckillDao;
import com.zkh.dao.SuccessKilledDao;
import com.zkh.dto.Exposer;
import com.zkh.dto.SeckillExecution;
import com.zkh.entity.Seckill;
import com.zkh.entity.SuccessKilled;
import com.zkh.enums.SeckillStatEnum;
import com.zkh.excepetion.RepeatKillException;
import com.zkh.excepetion.SeckillCloseException;
import com.zkh.excepetion.SeckillException;
import com.zkh.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import sun.awt.windows.ThemeReader;

import java.util.Date;
import java.util.List;

/**
 *@component:代表所有组件，不知道是service还是dao还是controller的时候用这个
 * @service：servicer，@dao，@controller
 */
@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    //从spring容器中获取该dao实例并注入到service中
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    //md5盐值字符串，用于混淆md5，在用户不知道拼接方式时，加上这串字符串生成md5几乎不能破解，越乱越好
    private final String slat="LFASKHFA*S%*&@!)jlkfsa hflksya9das*/das6";

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

        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base=seckillId+"/"+slat;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


    /**
     * 尽量不要使用一次配置永久使用的配置方法
     * 使用注解控制事务方法的优点：
     * 1.开发团队达成一致约定，明确标注事务 方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作（非数据库操作）或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如：只有一条修改操作，只读操作不需要事务控制。通常并发操作及多条修改需要事务控制
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    @Transactional
    public SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null|| !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑:减库存，记录秒杀行为
        Date nowTime=new Date();
        try {
            int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
            if(updateCount<=0){
                //没有更新到记录
                throw new SeckillCloseException("seckill is closed");
            }else {
                //减库存成功
                int insertCount=successKilledDao.insertSuccessKilled(seckillId,userPhone);
                //唯一：seckillId+userPhone
                if(insertCount<=0){
                    throw new RepeatKillException("seckill repeated ");
                }else {
                    //秒杀成功
                    SuccessKilled successKilled=successKilledDao.querySuccessKillWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            //所有编译期异常转化为运行期异常，spring会自动rollback
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }


    }
}
