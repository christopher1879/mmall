
package controller.backend;
import utils.jsonutils;
import utils.redisshardedpoolutil;
import commons.Const;
import commons.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.user;
import service.uservice;
import utils.cookieutils;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class usercontroller {

    @Autowired
    private uservice uservice;


    @RequestMapping("/")
    @ResponseBody
    public ServerResponse<user> login(String username,String password, HttpSession session, HttpServletResponse response)
    {
        ServerResponse<user> response1 = uservice.login(username,password);
        if (response1.issuccess()){
            user user= response1.getData();
            if (user.getRole()== Const.Role.ROLE_ADMIN){
                cookieutils.writeLoginToken(response,session.getId());
                redisshardedpoolutil.setnx(session.getId(),jsonutils.obj2string(response1.getData(),Const.rediscacheextime.redis_session));
                return response1;
            }else {
                return ServerResponse.createByErrorMessage("bushiguanliyuan,wufadenglu");
            }
        }
        return response1;
    }

}
