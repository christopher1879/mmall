package dao;
import org.apache.ibatis.annotations.Param;
import pojo.shipping;

import java.util.List;

public interface shippingmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(shipping record);

    int insertSelective(shipping record);

    shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(shipping record);

    int updateByPrimaryKey(shipping record);

    int deleteByShippingIdUserId(@Param("userId")Integer userId,@Param("shippingId") Integer shippingId);

    int updateByShipping(shipping record);

    shipping selectByShippingIdUserId(@Param("userId")Integer userId, @Param("shippingId") Integer shippingId);

    List<shipping> selectByUserId(@Param("userId")Integer userId);


}
