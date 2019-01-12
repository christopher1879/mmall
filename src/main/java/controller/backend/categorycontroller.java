package controller.backend;

import commons.Const;
import commons.ResponseCode;
import commons.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pojo.user;
import service.uservice;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class categorycontroller {

    @Autowired
    private uservice uservice;

    public ServerResponse addcategory(HttpSession session, String categoryname, @RequestParam(value = "parentid" ,defaultValue = )){
        user user= (pojo.user)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createBySuccess("gengxinggerenxinxichenggong " );
            if (user==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"yonghuweidenglu");

            }
            if (uservice.checkAdminRole(user).issuccess()){
                return  
            }else{
                return ServerResponse.createByErrorMessage("wuquancaozuo.xuguanliyuandenglu");
            }

    }





}
