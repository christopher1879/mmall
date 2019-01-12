package controller.backend;
import com.github.pagehelper.Page;
import commons.Const;
import commons.ResponseCode;
import commons.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.user;
import service.orderservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import utils.cookieutils;
import utils.jsonutils;
import utils.redisshardedpoolutil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/order/")
@Slf4j
public class ordercontroller {




    private  static LoggerFactory logger =new  LoggerFactory();
    @Autowired
    private  orderservice orderservice;


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<Page>orderlist(HttpServletRequest request, @RequestParam(value = "pagenum",defaultValue = "")int pagemnum,@RequestParam(value = "pagesize",defaultValue = "10") int pagesize){


    }



    @RequestMapping()
    @ResponseBody
    public  ServerResponse create (HttpServletRequest request,Integer shippinid){

        String logintoken = cookieutils.readLoginToken(request);
        if(StringUtils.isEmpty(logintoken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userjsonstr= redisshardedpoolutil.get(logintoken);
        user user = jsonutils.string2obj(userjsonstr, pojo.user.class);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return orderservice.cancel(user.getId(),orderno);


    }

    @RequestMapping("cancel.do")
    @ResponseBody
    public  ServerResponse cancel (HttpServletRequest request,Long orderno){
        String logintoken =cookieutils.readLoginToken(request);
        if (StringUtils.isEmpty(logintoken)){
            return ServerResponse.createByErrorMessage("")
        }
        String userjsonstr =redisshardedpoolutil.get(logintoken);
        user user = jsonutils.string2obj(userjsonstr, pojo.user.class);




    }






    @ResponseBody
    @RequestMapping("detail.do")
public  ServerResponse detail(HttpServletRequest request,long orderno){
        String logintoken= cookieutils.readLoginToken(request);
        if (StringUtils.isEmpty(logintoken)){

        }
    }



    @ResponseBody
    @RequestMapping("/")
    public ServerResponse pay(HttpSession session,Long orderno,HttpServletRequest request) {


        user user = (pojo.user) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = session.getServletContext().getRealPath("upload");
        return orderservice.pay(orderno, user.getId(), path);


    }



    @RequestMapping("")
    @ResponseBody
     public  Object alipaycallback(HttpServletRequest request){
    Map<String,> params = request.getParameterMap();
    for (Iterator iterator = requestparams.keySet().iterator();iterator.hasNext()){
        String name = (String)iterator.next();
        String[] values =(String[])requestparams.get(name);
        String valuestr = "";//声明一个字符串
        for (int i = 0; i <values.length ; i++) {

            valuestr= (i==values.length-1)?valuestr+values[i]:valuestr+values[i]+",";

        }
        params.put();

    }

    params.remove("sign_type");


}











}
