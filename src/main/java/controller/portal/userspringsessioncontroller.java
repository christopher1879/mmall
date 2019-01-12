package controller.portal;


import commons.Const;
import commons.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pojo.user;
import service.uservice;

import javax.servlet.http.HttpSession;

@Controller
public class userspringsessioncontroller {

    @Autowired
    private uservice uservice;




    //store up session


    public ServerResponse<user> login(String username, String password, HttpSession session){
        ServerResponse<user> response =uservice.login(username, password);
        if (response.issuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }



    //remove session
    public ServerResponse<> logout(){

    }




    public ServerResponse<> getuserinfo(){


    }


}
