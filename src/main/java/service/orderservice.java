package service;
import com.github.pagehelper.PageInfo;
import commons.ServerResponse;
import vo.ordervo;

import java.util.Map;

public interface orderservice {

 ServerResponse pay(Long orderNo, Integer userId, String path);
 ServerResponse aliCallback(Map<String,String> params);
 ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
 ServerResponse createOrder(Integer userId,Integer shippingId);
 ServerResponse<String> cancel(Integer userId,Long orderNo);
 ServerResponse getOrderCartProduct(Integer userId);
 ServerResponse<ordervo> getOrderDetail(Integer userId, Long orderNo);
 ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);



 //backend
 ServerResponse<PageInfo> manageList(int pageNum,int pageSize);
 ServerResponse<ordervo> manageDetail(Long orderNo);
 ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);
 ServerResponse<String> manageSendGoods(Long orderNo);

 //hour个小时以内未付款的订单，进行关闭
 void closeOrder(int hour);





}
