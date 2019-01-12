package service;

import commons.Const;
import commons.ServerResponse;
import dao.usermapper;
import jdk.nashorn.internal.ir.IfNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;
import pojo.user;
import utils.MD5utils;
import utils.redisshardedpoolutil;

import javax.management.remote.JMXServerErrorException;
import java.rmi.ServerError;
import java.util.UUID;

@Service("uservice")
public class userviceiml implements uservice {


    @Override
    public ServerResponse<user> login(String username, String password) {
        int resultcount = usermapper.checkusername(username);
        if (resultcount==0){
            return ServerResponse.createByErrorMessage("yonghumingbucunzai");

        }
        String md5password= MD5utils.md5encodeutf8(password);
        user user= usermapper.selectlogin(username,md5password);
        if (user==null){
            return ServerResponse.createByErrorMessage("mimacuowu");

        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("dengluchenggong",user);

    }


    @Override
    public ServerResponse<String> register(user user) {
      ServerResponse validresponse=this.checkValid(user.getUsername(), Const.USERNAME);
      if (!validresponse.issuccess()){
          return  validresponse;
      }
      validresponse=this.checkValid(user.getEmail(),Const.EMAIL);
      if (!validresponse.issuccess()){
          return validresponse;
      }
      user.setRole(Const.Role.ROLE_CUSTOMER);
      user.setPassword(MD5utils.md5encodeutf8(user.getPassword()));
      int resultcount= usermapper.insert(user);
      if (resultcount==0){
          return ServerResponse.createByErrorMessage("zhuceshibai");
      }
      return ServerResponse.createBySuccessMessage("zhucechengzhu")
    }


    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type))  {
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultcount = usermapper.checkusername(str);
                if (resultcount > 0) {
                    return ServerResponse.createByErrorMessage("yonghumingcunzai");

                }
            }
                if (Const.EMAIL.equals(type)) {
                    int resultcount = usermapper.checkemail(str);
                    if (resultcount > 0) {
                        return ServerResponse.createByErrorMessage("youxiangcunzai");
                    }
                } else {
                    return ServerResponse.createByErrorMessage("canshucuowu");
                }
                return ServerResponse.createBySuccess("jiaoyanchenggong");
            }
            return ServerResponse.createBySuccessMessage("jiaoyanchenggong");
        }






    @Override
    public ServerResponse selectQuestion(String username) {
        ServerResponse validresponse= this.checkValid(username,Const.USERNAME);
        if (validresponse.issuccess()){
            return ServerResponse.createByErrorMessage("yonghubucunzai")

        }
        String question =usermapper.selectquestionbyusername(username){
            return ServerResponse.createBySuccess(question)
        }


    }






    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {

        int resultcount =usermapper.checkanswer(username,question,answer);
        if (resultcount>0){
            String forgettoken = UUID.randomUUID().toString();
            redisshardedpoolutil.setnx(Const.TOKEN_PREFIX+username,forgettoken,60*60*12);
            return ServerResponse.createBySuccess(forgettoken);
        }
        return ServerResponse.createByErrorMessage("wentidedaancuowu");


            }








    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("canshucuowu,tokenxuyaochuandi");
        }
        ServerResponse validresponse=this.checkValid(username,Const.USERNAME);
        if (validresponse.issuccess()){
            return ServerResponse.createByErrorMessage("yonghubucunzai");
        }
        String token= redisshardedpoolutil.get(Const.TOKEN_PREFIX+username);
        if (org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("tokenwuxiaohuozheguoqi");
        }
        if (org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5password =MD5utils.md5encodeutf8(passwordNew);
            int rowcount =usermapper.updatepasswordbyusername(username,md5password);
            if (rowcount>0){
                return ServerResponse.createBySuccess("xiugaimimachengong");
            }else {
                return ServerResponse.createByErrorMessage("tokencuowu,qingchongxinhuoquchongzhimimade token");
            }

        }
        return ServerResponse.createByErrorMessage("xiugaimimashibai");
    }






    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, user user) {


        int resultcount = usermapper.checkpassword(MD5utils.md5encodeutf8(passwordOld),user.getId());
        if (resultcount==0){
            return  ServerResponse.createByErrorMessage("jiumimacuowu");
        }
        user.setPassword(MD5utils.md5encodeutf8(passwordNew));
        int updatecount= usermapper.updatebyprimarykeyselective(user);
        if (updatecount>0){
            return ServerResponse.createBySuccessMessage("mimagengxinchenggong");
        }
        return ServerResponse.createByErrorMessage("mimagengxinshibai");
    }






    @Override
    public ServerResponse<user> updateInformation(user user) {
        int resultcount = usermapper.checkemailbyuserid(user.getEmail(), user.getId());
        if (resultcount > 0) {
            return ServerResponse.createByErrorMessage("email yicunzai");
        }
        user updateuser = new user();
        updateuser.setId(user.getId());
        updateuser.setEmail(user.getEmail());
        updateuser.setPhone(user.getPhone());
        updateuser.setAnswer(user.getAnswer());

        int updatecount = usermapper.updatebyprimarykeyselective(updateuser);
        if (updatecount > 0) {
            return ServerResponse.createBySuccess("gengxinxinxinchenggong", updateuser);
        }
        return ServerResponse.createByErrorMessage("gengxingerenxinxishibai");
    }








    @Override
    public ServerResponse<user> getInformation(Integer userId) {
       user user =usermapper.selectbyprimarykey(userId){
           if (user==null){
               return ServerResponse.createByErrorMessage("zhaobudaodangqianyonghu");
           }
           user.setPassword(StringUtils.EMPTY);
           return ServerResponse.createBySuccess();
        }
    }







    @Override
    public ServerResponse checkAdminRole(user user) {
        if (user!=null&& user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return  ServerResponse.createBySuccess();
        }
        return  ServerResponse.createByError();
    }


}
