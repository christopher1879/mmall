package service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import commons.ServerResponse;
import dao.catagorymapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pojo.catagory;

import java.util.List;
import java.util.Set;

@Service("catagoryservice")

public class catagoryserviceimp implements catagoryservice {
    @Autowired
    private catagorymapper catagorymapper;

    private Logger logger =LoggerFactory.getLogger(catagoryserviceimp.class);


    public ServerResponse addcatagory (String catagoryname,Integer parentid){
        if (parentid==null || StringUtils.isBlank(catagoryname)){
            return ServerResponse.createByErrorMessage("zengtianpinleicanshucuowu")
        }
        catagory catagory =new catagory();
        catagory.setName(catagoryname);
        catagory.setParentId(parentid);
        catagory.setStatus(true);
        int rowcount = catagorymapper.insert(catagory);
        if (rowcount>0){
            return ServerResponse.createBySuccess("shangpintainjiachenggong ");

        }
        return ServerResponse.createByErrorMessage("tianjialeipinshibai");
    }




public ServerResponse updatecatagorybyname(Integer catagoryid,String catagoryname){

if (catagoryid==null||StringUtils.isBlank(catagoryname)){
    return  ServerResponse.createByErrorMessage("gengxinpinleicuowu ");
}
catagory catagory = new catagory();
catagory.setId(catagoryid);
catagory.setName(catagoryname);

int rowcount = catagorymapper.updateByPrimaryKeySelective(catagory);
if (rowcount>0){
    return ServerResponse.createBySuccess("gengxinpinleimignzichenggong");
}
return ServerResponse.createByErrorMessage("");

}









    public  ServerResponse<List<catagory>> getchildrenparallelcatagory(Integer catagoryid){

        List<catagory> catagoryList= catagorymapper.selectCategoryChildrenByParentId(catagoryid);
        if (CollectionUtils.isEmpty(catagoryList)){
            logger.info("weizhaodaodangqianzifenlei ");
        }
        return ServerResponse.createBySuccess(catagoryList);
    }






    private Set<catagory>  findchildcatagory(Set<catagory> catagorySet,Integer catagoryid){
        catagory catagory = catagorymapper.selectByPrimaryKey(catagoryid);
        if (catagory!=null){
            catagorySet.add(catagory);
        }
        List<catagory> catagoryList =catagorymapper.selectCategoryChildrenByParentId(catagoryid);
      for (catagory catagoryitem :catagoryList){
          findchildcatagory(catagorySet, catagoryitem.getId());

      }
     return catagorySet;
    }





/*
* 递归查询本节点的id 孩子节点的id
* */
    public ServerResponse selectcatagoryandchildrenbyid(Integer catagoryid){
        Set<catagory> catagorySet = Sets.newHashSet();
        findchildcatagory(catagorySet,catagoryid);

        List<Integer> catagoryidlist= Lists.newArrayList();
        if (catagoryid!=null){
            for (catagory catagoryitem :catagorySet){
                catagoryidlist.add(catagoryitem.getId());

            }
            return ServerResponse.createBySuccess(catagoryidlist);
        }

        return
    }










}
