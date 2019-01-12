package dao;

import org.apache.ibatis.annotations.Param;
import pojo.product;

import java.util.List;

public interface productmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(product record);

    int insertSelective(product record);

    product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(product record);

    int updateByPrimaryKey(product record);

    List<product> selectlist();
    List<product> selectbynameandproductid(@Param("productname") String productname,@Param("productid") Integer productid);
    List<product> selectbynameandcatagoryids(@Param("productname") String productname,@Param("catagoryidlist") List<Integer> catagoryidlist);
   //这个地方必须使用integer   considered that 有些商品被删除的情况
    Integer selectstockbyproductid(Integer id);




}
