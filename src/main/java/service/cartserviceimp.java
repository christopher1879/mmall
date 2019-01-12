package service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import commons.Const;
import commons.ResponseCode;
import commons.ServerResponse;
import dao.cartmapper;
import dao.productmapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pojo.cart;
import pojo.product;
import vo.cartproductvo;
import vo.cartvo;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;


@Slf4j
@Controller
public class cartserviceimp implements cartservice {
    @Autowired
    private cartmapper cartmapper;
    @Autowired
    private productmapper productmapper;


    public ServerResponse<cart> add (Integer userid,Integer productid,Integer count)
    {
        if ( productid==null||count==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
      cart cart = cartmapper.selectCartByUserIdProductId(userid,productid);
      if (cart ==null){
          cart cartitem= new cart();
          cartitem.setQuantity(count);
          cartitem.setChecked(Const.Cart.CHECKED);
          cartitem.setProductId(productid);
          cartitem.setUserId(userid);
         cartmapper.inset(cartitem);
      }else {
          count=cart.getQuantity()+count;
          cart.setQuantity(count);
          cartmapper.updateByPrimaryKeySelective(cart);
      }
    return this.list(userid);
    }



    public ServerResponse<cartvo> deleteproduct(Integer userid,String productids){
        List<String>  productlist = Splitter.on(",").splitToList(productids);
        if (CollectionUtils.isEmpty(productlist)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartmapper.deleteByUserIdProductIds(userid,productlist);
        return  this.list(userid);
    }



    public ServerResponse<cartvo> update(Integer userId,Integer productId,Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cart cart = cartmapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setquantity(count);
        }
        cartmapper.updateByPrimaryKey(cart);
        return this.list(userId);

    }



    public  ServerResponse<cartvo> list(Integer userid){
        cartvo cartvo =this.getcartvolist(userid);
        return  ServerResponse.createBySuccess(cartvo);
    }


    public ServerResponse<cartvo> selectOrUnSelect (Integer userId,Integer productId,Integer checked){
        cartmapper.checkedOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);
    }

    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartmapper.selectCartProductCount(userId));
    }



    private cartvo getcartvolimit(Integer userid){
        cartvo cartvo = new cartvo();
        List<cart> cartList = cartmapper.selectCartByUserId(userid);
        List<cartproductvo>cartproductvoList = Lists.newArrayList();
        BigDecimal carttotalprice = new BigDecimal("0");
        if ( CollectionUtils.isNotEmpty(cartList)){
            for (cart cartitem:cartList){
                cartproductvo cartproductvo = new cartproductvo();
                cartproductvo.setId(cartitem.getId());
                cartproductvo.setUserid(userid);
                cartproductvo.setProductid(cartitem.getProductId());

                product product = productmapper.selectByPrimaryKey(cartitem.getProductId());
                if (product!=null){
                   cartproductvo.setproductmainimage(product.getMainImage());
                    cartproductvo.setProductName(product.getName());
                    cartproductvo.setProductSubtitle(product.getSubtitle());
                    cartproductvo.setProductStatus(product.getStatus());
                    cartproductvo.setProductPrice(product.getPrice());
                    cartproductvo.setproductstock(product.getStock());
                    //判断库存
                    int buylimitcount=0;
                    if (product.getStock()>=cartitem.getQuantity()){
                        buylimitcount=cartitem.getQuantity();
                        cartproductvo.setLimitquantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                        buylimitcount=product.getStock();
                        cartproductvo.setLimitquantity(Const.Cart.LIMIT_NUM_FAIL);
                        cart  cartforquantity= new cart();
                        cartforquantity.setId(cartitem.getId());
                        cartforquantity.setQuantity(buylimitcount);
                        cartmapper.updateByPrimaryKeySelective(cartforquantity);
                    }
                    cartproductvo.
                }

            }

        }
    }




}
