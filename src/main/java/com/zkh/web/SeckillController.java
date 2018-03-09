package com.zkh.web;


import com.zkh.dto.Exposer;
import com.zkh.dto.SeckillExecution;
import com.zkh.dto.SeckillResult;
import com.zkh.entity.Seckill;
import com.zkh.enums.SeckillStatEnum;
import com.zkh.excepetion.RepeatKillException;
import com.zkh.excepetion.SeckillCloseException;
import com.zkh.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller //@service  @component
@RequestMapping("/seckill")//模块       url：/模块/资源/{id}/细分
public class SeckillController {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        //获取列表页
        List<Seckill> list=seckillService.getSeckillList();
        model.addAttribute("list",list);
        //lsit.jsp+model=ModelAndView
        return "list";//  /WEB-INF/jsp/+    list   +.jsp
    }


    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId==null){
            return "redirect:/seckill/list";
        }
        Seckill seckill=seckillService.getById(seckillId);
        if(seckill==null){
            return "redirect:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }


    //ajax接口，返回json
    @RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody/*告诉它这是一个json返回类型,springmvc会把它封装成json类型*/
    public SeckillResult<Exposer> exposer(Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<Exposer>(true,exposer);
        } catch (Exception e) {
            e.printStackTrace();
            result=new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> excute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  /*required=false表示即使cookie中没有killphone也不要提交错误给springmvc,我们在代码中解决,不加的话没有killphone直接报错*/
                                                  @CookieValue(value = "killPhone",required = false) Long userPhone){
        if(userPhone==null){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution seckillExecution=seckillService.excuteSeckill(seckillId,userPhone,md5);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        } catch (RepeatKillException e) {
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false,execution);
        }catch (SeckillCloseException e) {
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(false,execution);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false,execution);
        }
    }

    @RequestMapping(value ="/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now=new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }
}
