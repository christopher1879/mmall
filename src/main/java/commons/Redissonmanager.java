package commons;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;
import utils.propertyutils;

import javax.annotation.PostConstruct;


@Component
@Slf4j
public class Redissonmanager {
      private Config config = new Config();
       private  Redisson redisson = null;//初始化redisson   redisson 是声明为私有的  所以需要开发出去一个方法getredisson方法



      public Redisson getredission(){

        return  redisson;
    }

    private  static  String  redis1ip = propertyutils.getProperty("redis1.ip");
    private  static Integer redis1port = Integer.parseInt("redis1.port" );
    private  static String redis2ip =propertyutils.getProperty("redis2ip");
    private  static  Integer  redis2port =Integer.parseInt("redis2port");


    @PostConstruct
    private void  init(){
        try {
            config.useSingleServer()//单服务 .setaddress()//


            //使用redisson 的单服务 这是一个链式调用  setaddress的方法传入的是一个string 类型的adress  这里可以新建立一个address 使用stringbuider 的方法
            config.useSingleServer().setAddress(new StringBuilder().append(redis1ip).append(":").append(redis1port).toString());
            redisson = (Redisson) Redisson.create(config);//给redisson 赋值 assign
            log.info("初始化redisson 结束");
        }catch (Exception e){
            log.error("redisson init erro" ,e);

        }
    }

}
