package service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import commons.ServerResponse;
import dao.shippingmapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.shipping;

import java.util.List;
import java.util.Map;

@Service("shippingservice")
public class shippingserviceimp {

    @Autowired
    private shippingmapper  shippingmapper;


    public ServerResponse add (Integer userid, shipping shipping){
        shipping.setUserId(userid);
        int rowcount= shippingmapper.insert(shipping);
        if ( rowcount> 0 ){
            Map result = Maps.newHashMap();
            result.put("",shipping.getId());
            return ServerResponse.createBySuccess("xinjiandizhichengdong",result);

        }
        return ServerResponse.createByErrorMessage("xinjiandizhishibai");
    }



    public  ServerResponse<String> del( Integer  userid,Integer shippingid){
        int resultcount = shippingmapper.deleteByShippingIdUserId(userid,shippingid);
        if (resultcount> 0){
            return ServerResponse.createBySuccess("shanchuchenggong ");

        }
        return  ServerResponse .createByErrorMessage("shanchushibai");
    }

    public  ServerResponse<shipping> select(Integer userid,Integer shippingid){
        shipping shipping = shippingmapper.selectByShippingIdUserId(userid, shippingid);
        if (shipping==null){
            return  ServerResponse.createByErrorMessage("wufachaxundaogaidizhi");

        }
        return ServerResponse.createBySuccess("gengxindizhichenggong",shipping);

    }


    public  ServerResponse<PageInfo> list( Integer userid,int pagenum,int pagesize){
        PageHelper.startPage(pagenum,pagesize);
        List<shipping>shippingList = shippingmapper.selectByUserId(userid);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }

}
