package controller.backend;


import com.google.common.collect.Maps;
import commons.Const;
import commons.ResponseCode;
import commons.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pojo.user;
import pojo.product;
import service.fileservice;
import service.uservice;
import service.productservice;
import utils.propertyutils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class productcontroller {
    @Autowired
    private uservice uservice;
   @Autowired
   private productservice productservice;
   @Autowired
   private fileservice fileservice;


    public ServerResponse productsave(HttpSession session, product product){
        user user = (pojo.user)session.getAttribute(Const.CURRENT_USER);
        if ( user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"yonghuweidenglu,qingdengluguanliyuan")
        }
        if (uservice.checkAdminRole(user).issuccess()){
            /*增加产品的业务逻辑*/

        }else{
            return ServerResponse.createByErrorMessage("wuqianxiancaozuo")
        }
    }


    public  ServerResponse productsearch(){

    }

    public  ServerResponse getdetail(){

    }


    public ServerResponse getlist(){

    }

    public ServerResponse upload(){

    }


    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextimgupload(HttpSession session, @RequestParam(value = "upload_file",required =false)MultipartFile file, HttpServletRequest request, HttpServletResponse response){

        Map resultmap = Maps.newHashMap();
        user user = (pojo.user)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            resultmap.put("success", false);
            resultmap.put("msg","shangchuangshibai");
            return  resultmap;
        }

        if (uservice.checkAdminRole(user).issuccess()) {
            String path =request.getSession().getServletContext().getRealPath("upload");
            String targetfilename =fileservice.upload
            String url = propertyutils.getProperty("ftp.server.http.prefix")+targetfilename;
            resultmap.put("success",true);
            resultmap.put("msg","上传成功");
            resultmap.put("file_path",url);
            response.addHeader("access-control-allow-headers","x-file-name");
            return  resultmap;
        } else {
        }

    }
}
