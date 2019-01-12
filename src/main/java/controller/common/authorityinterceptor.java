package controller.common;

import com.google.common.collect.Maps;
import commons.Const;
import commons.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pojo.user;
import utils.cookieutils;
import utils.jsonutils;
import utils.redisshardedpoolutil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class authorityinterceptor  implements HandlerInterceptor {


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         log.info("");

         HandlerMethod handlerMethod =(HandlerMethod) handler;
         String methodname=handlerMethod.getMethod().getName();
         String classname =handlerMethod.getBean().getClass().getSimpleName();
       //解析参数  具体的参数的key 和value 是什么
         StringBuffer requestparambuffer=new StringBuffer();
         Map parammap= request.getParameterMap();



         //map 单独的遍历出values 的值    map 有一个.values的方法
         /*
        Collection c =parammap.values();
        Iterator iterator = c.iterator();
        while (iterator.hasNext()){
            Object value= iterator.next();
        }
         */


         //map 的遍历的方式 使用iterator方式遍历  第一种是entryset 效率比较高   第二种是遍历keyset 遍历了两次效率低
        //1
        Iterator it= parammap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry=(Map.Entry)it.next();
            String mapkey= (String)entry.getKey();
            String mapvalue= StringUtils.EMPTY;
            Object obj=entry.getValue();
           //request 这个参数的map 里面的value 返回的是一个string[]
            if (obj instanceof String[]){
                String [] strings= (String[])obj;
                mapvalue = Arrays.toString(strings);
            }
            requestparambuffer.append(mapkey).append("=").append(mapvalue);

        }

        //2
      Iterator iterator = parammap.keySet().iterator();
        while (iterator.hasNext()){
            Object key= iterator.next();
            Object value=parammap.get(key);
        }





        //stringutils的用法 对比的传入的两个参数references
        if ( StringUtils.equals(classname,"usermanagecontroller")&& StringUtils.equals(methodname,"login")){

         log.info("",classname,methodname,requestparambuffer.toString());
         user user= null;
         String logintoken= cookieutils.readLoginToken(request);
         if (StringUtils.isNotBlank(logintoken)){
             String userjsonstr= redisshardedpoolutil.get(logintoken);
             user = jsonutils.string2obj(userjsonstr,user.class);
         }
            if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
                response.reset();
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;chrset=utf-8");
                PrintWriter out = response.getWriter();
                if (user==null){
                    if (StringUtils.equals(classname,"productmanagercontroller")&& StringUtils.equals(methodname,"richtextimgupload")){
                        Map resultmap= Maps.newHashMap();
                        resultmap.put("success",false);
                        resultmap.put("msg","qingdengluguanliyuan");
                    }else {
                        out.print(jsonutils.obj2string(ServerResponse.createByErrorMessage("lanjieqilanjie,yonghuweidneglu")));
                    }
                }else {

                    if (StringUtils.equals(classname,"productmanagecontroller")&&StringUtils.equals(methodname,"richtextimgupload")){
                        Map resultmap= Maps.newHashMap();
                        resultmap.put("success",false);
                        resultmap.put("msg","wuquanxinacaozuo");
                    }else {
                        out.print(jsonutils.obj2string(ServerResponse.createByErrorMessage("")));
                    }
                }
                out.flush();
                out.close();
                return false;
            }

            return true;

        }
      return true;
    }


}
