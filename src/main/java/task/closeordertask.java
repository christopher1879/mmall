package task;


import commons.Const;
import commons.Redissonmanager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pojo.OrderItem;
import pojo.order;
import service.orderservice;
import utils.propertyutils;
import utils.redisshardedpoolutil;
import dao.ordermapper;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Component
@Slf4j
public class closeordertask {


    @Autowired
    private orderservice orderservice;
    @Autowired
    private Redissonmanager redissonmanager;

    @PreDestroy
    public void dellock(){
        redisshardedpoolutil.del(Const.redis_lock.close_order_task_lock);

     }




     @Scheduled(cron ="0/1 ***?")//每分钟执行一次
    public void closeordertaskv1(){

         log.info("");
         int hour = Integer.parseInt(propertyutils.getProperty("clse.order.task.time.hour","2"));

         orderservice
     }





//没有设置过期的时间 死锁的问题存在
     public void closeorder2(){

        log.info("关闭订单定时任务启动");
        Long  locktimeout =Long.parseLong(propertyutils.getProperty(""));
        Long setnxresult =redisshardedpoolutil.setnx(Const.redis_lock.close_order_task_lock,String.valueOf(System.currentTimeMillis()+locktimeout));
         if (setnxresult!=null && setnxresult.intValue()==1){
             closeorder(Const.redis_lock.close_order_task_lock);
         }else {
             log.info("");
    }
}


//加入了 时间戳 预防了死锁的现象 phenomenon

     @Scheduled("")
    public void closeordertask3(){
         log.info("");
         Long locktimeout=Long.parseLong(propertyutils.getProperty("lock.timeout","5000");
         Long setnxresult = redisshardedpoolutil.setnx(Const.redis_lock.close_order_task_lock,String.valueOf(System.currentTimeMillis()+locktimeout));

         if (setnxresult!=null && setnxresult.intValue()==1){
             closeorder(Const.redis_lock.close_order_task_lock);
         }else {
             String lockvaluestr =redisshardedpoolutil.get(Const.redis_lock.close_order_task_lock);
                  if (lockvaluestr!=null&& System.currentTimeMillis()>Long.parseLong(lockvaluestr)){
                      String getsetresult = redisshardedpoolutil.getset(Const.redis_lock.close_order_task_lock,String.valueOf(System.currentTimeMillis()+locktimeout));
                      if (getsetresult==null||(getsetresult!=null&& StringUtils.equals(lockvaluestr,getsetresult))){

                  }
         }

     }
}


//框架的实现方式

    @Scheduled(cron = "")
    public void closrorderv4(){
        RLock lock = redissonmanager.getredission().getLock(Const.redis_lock.close_order_task_lock);
        boolean getlock =false; //设置默认的初始值是false
        try {
            if (getlock=lock.tryLock(0,50, TimeUnit.SECONDS))
            {//将trylock返回的布尔值赋值给getlock  如果是true 的话进入下面的代码块{
                log.info("redisson获得分布式锁:{},threadname:{}" ,Const.redis_lock.close_order_task_lock);
                int hour =Integer.parseInt(propertyutils.getProperty("close.order.task.time.hour","2"));
                orderservice.closeOrder(hour);}
            else {
                log.info("redisson没有获取到分布式锁：{}，threadname:{},",Const.redis_lock.close_order_task_lock);
            }
        }catch (InterruptedException e){
            log.error("redisson分布式锁获取异常",e);
        }finally {
            if (!getlock){
                return;
            }

            lock.unlock();
            log.info("redisson分布式所释放锁");
        }

    }









    public  void  closeorder (String lockname){
      redisshardedpoolutil.expire(lockname,5);
      log.info("",Const.redis_lock.close_order_task_lock,Thread.currentThread().getName());
      int hour = Integer.parseInt(propertyutils.getProperty("close.order.task.time.hour","2"));
      orderservice.closeorder();
      redisshardedpoolutil.del(Const.redis_lock.close_order_task_lock,Thread.currentThread().getName());
      log.info("",Const.redis_lock.close_order_task_lock);
      log.info("==============");


    }


}