package dao;
import org.apache.ibatis.annotations.Param;
import pojo.cart;

import java.util.List;

public interface cartmapper {

    int deletebyprimarykey(Integer id);
    int inset(cart record);
    int insertselective(cart record);
    int updatebyprimarykey(cart record);
    cart slectbyprimarykey(Integer id);

    cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId")Integer productId);

    List<cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdList);

    int updateByPrimaryKeySelective(cart record);
    int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId")Integer productId,@Param("checked") Integer checked);

    int selectCartProductCount(@Param("userId") Integer userId);


    List<cart> selectCheckedCartByUserId(Integer userId);




}
