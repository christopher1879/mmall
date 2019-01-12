package controller.portal;
import com.sun.deploy.net.cookie.CookieUnavailableException;
import commons.ResponseCode;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;
import sun.swing.StringUIClientPropertyKey;
import  utils.jsonutils;
import  utils.redisshardedpoolutil;
import commons.Const;
import commons.ServerResponse;
import commons.tokencache;
import dao.usermapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.user;
import utils.cookieutils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.rmi.ServerError;
import java.util.UUID;

@Controller

public class usercontroller {
    @Autowired
    private service.uservice uservice;


    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<user> login(String username, String password, HttpSession session, HttpServletResponse response) {

        ServerResponse<user> response1 = uservice.login(username, password);
        if (response1.issuccess()) {
            cookieutils.writeLoginToken(response, session.getId());

         //todo  未解决的问题
            redisshardedpoolutil.setnx(session.getId(), jsonutils.obj2string(response1.getData(), Const.RedisCacheExtime.REDIS_SESSION_EXTIME));
        }
    }


    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<user> logout(HttpServletResponse response, HttpServletRequest request) {
        String logintoken = cookieutils.readLoginToken(request);
        cookieutils.delLoginToken(request, response);
        redisshardedpoolutil.del(logintoken);
        return ServerResponse.createBySuccess();

    }






    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse <String> register(user user){
        return uservice.register(user);
    }






    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody

    public ServerResponse<String> checkvalid(String str, String type) {
        return uservice.checkValid(str, type);


    }




    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<user> getUserInfo(HttpServletRequest request) {
        String logintoken = cookieutils.readLoginToken(request);
        if (StringUtils.isEmpty(logintoken)) {
            return ServerResponse.createByErrorMessage("yonghuweidenglu,wufahuoqudangqianyonghude xinxi");
        }
        String userjsonstr = redisshardedpoolutil.get(logintoken);
        user user = jsonutils.string2obj(userjsonstr, user.class);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("");
    }











    @RequestMapping(value = "",method =RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetpassword(HttpServletRequest request,String passwordold,String passwordnew){
        String logintoken= cookieutils.readLoginToken(request);
        if (StringUtils.isEmpty(logintoken)){
            return  ServerResponse.createByErrorMessage("");
        }

        String userjsonst= redisshardedpoolutil.get(logintoken);
        user user = jsonutils.string2obj(userjsonst,user.class);
        if (user==null){
            return ServerResponse.createByErrorMessage("");
        }
        return uservice.resetPassword(passwordold,passwordnew,user);

    }



    @RequestMapping(value ="" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<user> update_information(HttpServletRequest request,user user){
        String logintoken =cookieutils.readLoginToken(request);
        if (StringUtils.isEmpty(logintoken)){
            return ServerResponse.createByErrorMessage("")
        }
        String userjsonstr= redisshardedpoolutil.get(logintoken);
        user currentuser= jsonutils.string2obj(userjsonstr,user.class)
        if(currentuser==null){
            return ServerResponse.createByErrorMessage("yonghuweidenglu");
        }
        user.setId();
        user.setUsername();
        ServerResponse<user> response= uservice.updateInformation(user);

    }




    public ServerResponse<user> get_information(HttpServletRequest request){
        String logintoken =cookieutils.readLoginToken(request);
        if (StringUtils.isEmpty(logintoken)){
            return ServerResponse.createByErrorMessage("yonghuweidenglu,wufahuoqudangqianyonghudexinxi");
        }
        String userjsonstr= redisshardedpoolutil.get(logintoken);
        user currentuser= jsonutils.string2obj(userjsonstr,user.class);
        if (currentuser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"weideng,luxuyaodenglu")

        }
        return uservice.getInformation(currentuser.getId());
    }
}





