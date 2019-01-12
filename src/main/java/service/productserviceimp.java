package service;

import com.alipay.demo.trade.model.hb.Product;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import commons.Const;
import commons.ResponseCode;
import commons.ServerResponse;
import dao.catagorymapper;
import dao.productmapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.catagory;
import pojo.product;
import utils.propertyutils;
import vo.productdetailvo;
import vo.productlistvo;

import java.util.ArrayList;
import java.util.List;

@Service()
public class productserviceimp implements productservice {

    @Autowired
    private  productmapper productmapper;
    @Autowired
    private catagorymapper catagorymapper;
    @Autowired
    private catagoryservice catagoryservice;


    public ServerResponse savaorupdateproduct(product product){
        if (product!=null){
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] subimagearray= product.getSubImages().split(",");
                if (subimagearray.length>0){
                    product.setMainImage(subimagearray[0]);
                }
            }
            if ( product.getId()!=null){
              int rowcount=  productmapper.updateByPrimaryKey(product);
              if (rowcount>0){
                  return ServerResponse.createBySuccess("gengxinchapinchenggong ")
              }  return ServerResponse.createBySuccess("gengxinchanpinshibai")
            } else {
                int rowcount = productmapper.insert(product);
                if (rowcount>0){
                    return ServerResponse.createBySuccess("xinzengchanpinchengong");
                }
                return ServerResponse.createBySuccess("xinzengshibai");
            }

        }
        return ServerResponse.createByErrorMessage("xinzenghuogengxinchanpincanshubuzhenque");
    }





    public ServerResponse<String> setsalestatus(Integer productid,Integer status){

        if ( productid==null || status==null){
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc())
        }
        product product = new product();
        product.setStatus(productid);
        product.setId(status);
        int rowcount = productmapper.updateByPrimaryKeySelective(product);
        if (rowcount>0){
            return ServerResponse.createBySuccess("xiugaishangpingxiaoshouzhuangtaichenggong");
        }
        return ServerResponse.createByErrorMessage("xiugaishangpinxiaoshouzhuangtaishibai");
    }





    public ServerResponse<productdetailvo>  manageproductdetail(Integer productid){
        if ( productid==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        product product = productmapper.selectByPrimaryKey(productid);
        if (product==null){
            return  ServerResponse.createByErrorMessage("chanpinyijingxiajiahuozheshanchu");
        }
        productdetailvo productdetailvo = assembleProductDetailVo(product);

        return ServerResponse.createBySuccess(productdetailvo);

    }




    public ServerResponse<PageInfo> getproductlist(int pagenum,int pagesize){
        PageHelper.startPage(pagenum, pagesize);
        List<product> productList =productmapper.selectlist();
        List<productlistvo> productlistvoList = Lists.newArrayList();

        for (product productitem:productList){

            productlistvo productlistvo =assembleproductlistvo(productitem);
            productlistvoList.add(productlistvo);
        }
        PageInfo pageresult = new PageInfo(productList);
        pageresult.setList(productlistvoList);
        return ServerResponse.createBySuccess(pageresult);

    }









    private productdetailvo assembleProductDetailVo(product product){
       productdetailvo  productDetailVo = new productdetailvo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }





  private productlistvo assembleproductlistvo(product product){
        productlistvo productListVo = new productlistvo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(propertyutils.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }






    public  ServerResponse<PageInfo>  searchproduct(String productname,Integer productid,int pagenum,int pagesize){

        PageHelper.startPage(pagenum,pagesize);
        if (StringUtils.isNotBlank(productname)){
            productname= new StringBuilder().append("%").append(productname).append("%").toString();
        }
        List<product> productList = productmapper.selectbynameandproductid(productname,productid);
        List<productlistvo> productlistvoList = Lists.newArrayList();
        for (product productitem:productList){
            productlistvo productlistvo = assembleproductlistvo(productitem);
            productlistvoList.add(productlistvo);

        }
        PageInfo pageresult = new PageInfo(productList);
        pageresult.setList(productlistvoList);
        return ServerResponse.createBySuccess(pageresult);

    }






    public  ServerResponse<productdetailvo> getproductdetail (Integer productid){
        if ( productid==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }

        product product = productmapper.selectByPrimaryKey(productid);
        if ( product==null){
            return ServerResponse.createByErrorMessage("chanpinyijingxiajiahuozheshanchu");
        }
        if (product.getStatus()!= Const.ProductStatusEnum.ON_SALE.getCode()){
            return  ServerResponse.createByErrorMessage("chanpinyijingxiajiahuozheshanchu");
        }
        productdetailvo productdetailvo = assembleproductdetailvo(product);
        return ServerResponse.createBySuccess(productdetailvo);
        }

    }




    public ServerResponse<PageInfo> getproductbykeywordcatagory(String keyword,Integer catagorid,int pagesize, int pagenum,String orderby){
        if ( StringUtils.isBlank(keyword)&&catagorid==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        List<Integer> catagoryidlist = new ArrayList<Integer>();
        if (catagorid!=null){
            catagory catagory = catagorymapper.selectByPrimaryKey(catagoryid);
            if ( catagory==null&& StringUtils.isBlank(keyword)){
                PageHelper.startPage(pagenum,pagesize);
                List<productlistvo > productlistvoList =Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productlistvoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            catagoryidlist = catagoryservice.selectCategoryAndChildrenById(catagory.getId().getdata());

        }
        if (StringUtils.isNotBlank(keyword)){
            keyword= new StringBuilder().append("%").append(keyword).append("%").toString();
            PageHelper .startPage(pagenum,pagesize){
                if (StringUtils.isNotBlank(orderby)){
                    if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderby)){
                        String[] orderbyarray= orderby.split("_");
                        PageHelper.orderBy(orderbyarray[0]+""+orderbyarray[1]);
                    }
                }
                List<product> productList = productmapper.selectbynameandcatagoryids(StringUtils.isBlank(keyword))?null:keyword,catagoryidlist.size);
                List<productlistvo> productlistvoList= Lists.newArrayList();
                for ( product product:productList){
                    productlistvo productlistvo = assembleproductlistvo(product);
                    productlistvoList.add(productlistvo);

                }

                PageInfo pageInfo = new PageInfo(productList);
                pageInfo.setList(productlistvoList);
                return  ServerResponse.createBySuccess(pageInfo);
            }



    }

}
