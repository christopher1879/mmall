package service;


import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import commons.Const;
import commons.ServerResponse;
import dao.*;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pojo.*;
import utils.bigdecimalutils;
import utils.datetimeutils;
import utils.ftputils;
import utils.propertyutils;
import vo.orderitemvo;
import vo.orderproductvo;
import vo.ordervo;
import vo.shippingvo;

import java.io.File;
import java.math.BigDecimal;
import java.rmi.ServerError;
import java.util.*;

;


@Slf4j

public class oderserviceimp {


   private static AlipayTradeService alipayTradeService;
   static {
       Configs.init("");
       alipayTradeService= new AlipayTradeServiceImpl.ClientBuilder().build();
   }



    @Autowired
    private dao.ordermapper ordermapper;
    @Autowired
    private orderitemmapper orderitemmapper;
    @Autowired
    private payinfomapper payinfomapper;
    @Autowired
    private cartmapper cartmapper;
    @Autowired
    private productmapper productmapper;
    @Autowired
    private  shippingmapper shippingmapper;






    public  ServerResponse createorder (Integer userid,Integer shippingid){
        List<cart> cartList= cartmapper.selectCheckedCartByUserId();
        ServerResponse serverResponse = this.getcartorderitem(userid,cartList){
            if (!serverResponse.issuccess()){
                return serverResponse;
            }
            List<OrderItem> orderitemList = (List<OrderItem>)serverResponse.getData();
            BigDecimal payment = this.getordertotalprice(orderitemList);
            order order = this.assembleorder(userid,shippingid,payment);
           if (order==null){

               return  serverResponse.createByErrorMessage("shenchengdingdancuowu");
            }
            if ( CollectionUtils.isEmpty(orderitemList)) {
                return serverResponse.createByErrorMessage("gouwucheweikong");
            }
            for (OrderItem orderItem : orderitemList){
                orderItem.setOrderNo(order.getOrderNo());
            }
            ordermapper.




        }
    }




    public Long generatorderno(){
        Long  currenttime =System.currentTimeMillis();
        return currenttime+new Random().nextInt(100);
    }






    public ordervo assembleordervo(order order,List<OrderItem> orderitemList){
        ordervo ordervo = new ordervo();
        ordervo.setOrderNo(order.getOrderNo());
        ordervo.setPayment(order.getPayment());
        ordervo.setPaymentType(order.getPaymentType());
        ordervo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        ordervo.setPostage(order.getPostage());
        ordervo.setStatus(order.getStatus());
        ordervo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());
        ordervo.setShippingId(order.getShippingId());
       shipping shipping = shippingmapper.selectByPrimaryKey(order.getShippingId());
       if (shipping!=null){
           ordervo.setReceiverName(shipping.getReceiverName());
           ordervo.setShippingVo(assembleshippingvo(shipping))
       }
        ordervo.setPaymentTime(datetimeutils.dateToStr(order.getPaymentTime()));
        ordervo.setSendTime(datetimeutils.dateToStr(order.getSendTime()));
        ordervo.setEndTime(datetimeutils.dateToStr(order.getEndTime()));
        ordervo.setCreateTime(datetimeutils.dateToStr(order.getCreateTime()));
        ordervo.setCloseTime(datetimeutils.dateToStr(order.getCloseTime()));
     ordervo.setImageHost(propertyutils.getProperty("ftp.server.http.prefix"));
     List<orderitemvo> orderitemvoList = Lists.newArrayList();
   for (OrderItem orderItem:orderitemList){
       orderitemvo orderitemvo = assembleorderitemvo(orderItem);
       orderitemvoList.add(orderitemvo);
   }
   ordervo.setOrderItemVoList(orderitemvoList);
   return  ordervo;
    }





    private List<ordervo> assembleordervolist(List<order> orderList,Integer userid){
        List<ordervo> ordervoList = Lists.newArrayList();
        for (order order:orderList){
           List<OrderItem> orderItemList= Lists.newArrayList();
            if (userid==null){
                orderItemList= orderitemmapper.getByOrderNo(order.getOrderNo());
            }else {
                orderItemList = orderitemmapper.getByOrderNoUserId(order.getOrderNo(),userid);
            }
            ordervo ordervo = assembleordervo(order,orderItemList);
            ordervoList.add(ordervo);
        }
        return ordervoList;
    }




    public orderitemvo assembleorderitemvo(OrderItem orderitem){
        orderitemvo orderitemvo = new orderitemvo();
        orderitemvo.setOrderNo(orderitem.getOrderNo());
        orderitemvo.setProductId(orderitem.getProductId());
        orderitem.setProductImage(orderitem.getProductImage());
        orderitemvo.setCurrentUnitPrice(orderitem.getCurrentUnitPrice());
        orderitemvo.setQuantity(orderitem.getQuantity());
        orderitemvo.setTotalPrice(orderitem.getTotalPrice());
        orderitemvo.setCreateTime(orderitem.getTotalPrice());
        return orderitemvo;

    }



    public shippingvo assembleshippingvo(shipping shipping) {
        shippingvo  shippingvo = new shippingvo();
        shippingvo.setReceiverName(shipping.getReceiverName());
        shippingvo.setReceiverCity(shipping.getReceiverCity());
        shippingvo.setReceiverName(shipping.getReceiverName());

        return  shippingvo;

    }





    public  order assembleorder(Integer userid ,Integer shippingid,BigDecimal payment){

        order order= new order();
        Long orderno = this.generatorderno();
        order.setOrderNo(orderno);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);

        order.setUserId(userid);
        order.setShippingId(shippingid);
        //判断订单有没有创建成功
        int count = ordermapper.insert(order);
        if (count>0){
            return order;
        }
        return null;
    }




    private void reduceproductstock(List<OrderItem> orderitemList){
        for (OrderItem orderitem:orderitemList){
            product product = productmapper.selectByPrimaryKey(orderitem.getProductId());
            product.setStock(product.getStock()-orderitem.getQuantity());
            productmapper.updateByPrimaryKeySelective(product);
        }
    }



    private void cleancart( List<cart> cartList){

        for (cart cart :cartList){
            cartmapper.deletebyprimarykey(cart.getId());
        }
    }




    public ServerResponse<String> cancel(Integer userid,Long orderno){
        order order = ordermapper.selectbyuseridandorderno(userid,orderno);
        if (order==null){
            return ServerResponse.createByErrorMessage("");

        }
        if (order.getStatus()!=Const.OrderStatusEnum.NO_PAY.getCode()){
            order updateorder =  new order();
            updateorder.setId(order.getId());
            updateorder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
            int rowcount = ordermapper.updatebyprimarykeyselective(updateorder);
            if (rowcount>0){
                return ServerResponse.createBySuccess();
            }
            return  ServerResponse.createByError();
        }
        return ServerResponse.createByError();
    }





   private ServerResponse getcartorderitem (Integer userid,List<cart> cartList){
         List<OrderItem> orderItemList= new ArrayList<>();
         if (CollectionUtils.isEmpty(cartList)){
             return ServerResponse.createByErrorMessage("gouwucheweikong");
         }

         for (cart cartitem:cartList){
             OrderItem orderItem = new OrderItem();
             product product = productmapper.selectByPrimaryKey(cartitem.getProductId());
             if (Const.ProductStatusEnum.ON_SALE.getCode()!=product.getStatus()){
                 return ServerResponse.createByErrorMessage("chanpin"+product.getName()+"bushizaixianshoumaizhuangtai");
             }

             if (cartitem.getQuantity()>product.getStock()){
                 return  ServerResponse.createByErrorMessage("chanpin"_+product.getName()+"kucunbuzu");
             }
             orderItem.setUserId(userid);
             orderItem.setProductId(product.getId());
             orderItem.setProductName(product.getName());
             orderItem.setProductImage(product.getMainImage());
             orderItem.setQuantity(cartitem.getQuantity());
             orderItem.setCurrentUnitPrice(product.getPrice());
             orderItem.setTotalPrice(bigdecimalutils.mul(product.getPrice().doubleValue(),cartitem.getQuantity()));
             orderItemList.add(orderItem);
         }
         return ServerResponse.createBySuccess(orderItemList);
    }






    public ServerResponse<String> cancel (Integer userid,long orderno){
        order order=ordermapper.selectbyuseridandorderno(userid,orderno);
        if (order==null){
            return ServerResponse.createByErrorMessage("该用户此订单不存在");
        }
        if (order.getStatus()!=Const.OrderStatusEnum.NO_PAY.getCode()){
            return ServerResponse.createByErrorMessage("已经付款无法取消订单");
        }
        order updateorder= new order();
        updateorder.setId(order.getId());
        updateorder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
        int row = ordermapper.updatebyprimarykeyselective(updateorder);
        if (row>0){
            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByError();
    }




   public ServerResponse getordercartproduct (Integer userid) {
       orderproductvo orderproductvo = new orderproductvo();
       List<cart> cartList = cartmapper.selectCheckedCartByUserId(userid);
       ServerResponse serverResponse = this.getcartorderitem(userid,cartList);
       if(!serverResponse.issuccess()){
           return serverResponse;
       }
       List<OrderItem> orderItemList =(List<OrderItem>)serverResponse.getData();
       List<orderitemvo> orderitemvoList =  Lists.newArrayList();
       BigDecimal payment = new BigDecimal("0");//初始化总价为0
       for (OrderItem orderItem:orderItemList){
           payment= bigdecimalutils.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
           orderitemvoList.add(assembleorderitemvo(orderItem));
       }
       orderproductvo.setProducttotalprice(payment);
       orderproductvo.setOrderitemvoList(orderitemvoList);
       orderproductvo.setImagehost(propertyutils.getProperty(""));
       return serverResponse.createBySuccess();


   }







   private BigDecimal getordertotalprice(List<OrderItem> orderItemList){
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList){
            payment= bigdecimalutils.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return payment;
   }





   public ServerResponse<ordervo> getorderdetail(Integer userid ,Long orderno) {
       order order = ordermapper.selectbyuseridandorderno(userid, orderno);
           if (order != null) {
           List<OrderItem> orderItemList = orderitemmapper.getByOrderNoUserId(orderno, userid);
           ordervo ordervo = assembleordervo(order, orderItemList);
           return ServerResponse.createBySuccess(ordervo);
       }

       return ServerResponse.createByErrorMessage("");

   }











    public ServerResponse<PageInfo> getorderlist(Integer userid,int pagenum,int pagesize){
        PageHelper.startPage(pagenum,pagesize);
        List<order> orderList=ordermapper.selectbyuserid(userid);
        List<ordervo> ordervoList = assembleordervolist(orderList,userid);
        PageInfo pageresult = new PageInfo(orderList);

    }





















//backend
    public ServerResponse<PageInfo> managelist(int pagenum,int pagesize){
        PageHelper.startPage(pagenum, pagesize);
        List<order> orderList = ordermapper.selectallorder();
        List<ordervo> ordervoList= this.assembleordervolist(orderList);
        PageInfo pageresult= new PageInfo();
        pageresult.setList(ordervo);
    }


    public ServerResponse pay(long orderno,Integer userid,String path){
        Map<String,String> resultmap= Maps.newHashMap();
        order order =ordermapper.selectbyuseridandorderno(userid,orderno);
        if (order==null){
            return ServerResponse.createByErrorMessage("yonghumeiyoudingdan");
        }
        resultmap.put("orderno",String.valueOf(order.getOrderNo()));


        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();


        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("happymmall扫码支付,订单号:").append(outTradeNo).toString();


        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();


        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";


        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();


        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";


        ExtendParams extendParams= new ExtendParams();
        extendParams.setSysServiceProviderId("")




        List<goodsdetail> goodsdetailList = new ArrayList<goodsdetail>();
        List<OrderItem> orderitemlist= orderitemmapper.getByOrderNoUserId(orderno,userid);
        for (OrderItem orderItem:orderitemlist){
            goodsdetail goods = goodsdetail.newinstance

        }

        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder().setSubject(subject).setTotalAmount(totalmount).setOutTradeNo(outtradeno).setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(propertyutils.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);


        AlipayF2FPrecreateResult result= AlipayTradeService.tradeprecreate(builder);
        switch ( result.getTradeStatus()){
            case SUCCESS:
                log.info("");
                AlipayTradePrecreateResponse response = result.getResponse();
                dumpresponse(response);

                File folder = new File(path);
                if (!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                String qrpath = String .format(path+"/qr-",response.getOutTradeNo());
                String qrfilename= String.format("",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(),256,qrpath);

                File targetfile = new File(path,qrfilename);
                try {
                    ftputils.
                }catch (){

                }


        }
    }

}






private  void dumpresponse(AlipayResponse response){
        if (response!=null){
            log.info(String.format("code:"),response.getCode(),response.getMsg());
        if (StringUtils.isNotEmpty(response.getSubCode())){
            log.info(String.format("",response.getSubCode(),response.getSubCode()));

        }
        log.info("body"+response.getBody());
        }
}





public  ServerResponse alicallback(Map<String,String> params){
    Long orderno = Long.parseLong(params.get("out_trade_no"));
    String tradeno = params.get("trade_no");
    String tradestatus =params.get("trade_status");
    order order = ordermapper.selectbyorderno(orderno);
    if ( order==null){
        return ServerResponse.createByErrorMessage();
    }
    if ( order.getStatus()>=Const.OrderStatusEnum.PAID.getCode());{
        return ServerResponse.createBySuccess("zhifubaochongfudiaoyong ");
    }
    if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradestatus)) {

        order.setStatus(Const.OrderStatusEnum.PAID.getCode());
        ordermapper.updatebyprimarykeyselective(order);
    }

    payinfo payinfo = new payinfo();
    payinfo .setUserId(order.getUserId());
    payinfo.setOrderNo(order.getOrderNo());
    payinfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
    payinfo.setPlatformStatus(tradestatus);
    payinfo.setPlatformNumber(tradeno);

    payinfomapper.insert(payinfo);
  return ServerResponse.createBySuccess();
    }





    public  ServerResponse queryorderpaystatus (Integer userid,Long orderno){
        order order = ordermapper.selectbyuseridandorderno(userid,orderno);
        if ( order==null){
            return  ServerResponse.createByErrorMessage("yonghumeiyougaidingdan ")
        }
        if (order.getStatus()>=Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();

    }










    //backend

    //有分页的功能  需要传入 page 的参数
      public  ServerResponse<PageInfo> managelist(int pagenum,int pagesize){
        PageHelper .startPage(pagenum, pagesize);
        List<order> orderList= ordermapper.selectallorder();
        List<ordervo> ordervoList= this.assembleordervolist(orderList,null);
        PageInfo pageInfo= new PageInfo(orderList);
        pageInfo.setList(ordervoList);
        return ServerResponse.createBySuccess(pageInfo);
}




public  ServerResponse<ordervo> managedetail(Long orderno){
        order order= ordermapper.selectbyorderno(orderno);
        if (order!=null){
            List<OrderItem> orderItemList= orderitemmapper.getByOrderNo(orderno);
            ordervo ordervo= assembleordervo(order,orderItemList);
            return ServerResponse.createBySuccess(ordervo);
        }

        return  ServerResponse.createByErrorMessage("dingdanbucunzai ");
}


//有分页的实现 需要传入 page 的参数
public ServerResponse<PageInfo> managesearch(Long orderno,int pagenum,int pagesize){
        PageHelper.startPage(pagenum,pagesize);
        order order = ordermapper.selectbyorderno(orderno);
        if (order!=null){
            List<OrderItem> orderItemList= orderitemmapper.getByOrderNo(orderno);
            ordervo ordervo= assembleordervo(order,orderItemList);
            PageInfo pageInfo = new PageInfo(Lists.newArrayList(order));
            pageInfo.setList(Lists.newArrayList(order));
            return ServerResponse.createBySuccess(pageInfo);
        }

        return  ServerResponse.createByErrorMessage("dingdanbucunzai");
}



public ServerResponse<String> managesendgoods(Long orderno){
        order order =ordermapper.selectbyorderno(orderno);
        if (order!=null){
            if (order.getStatus()==Const.OrderStatusEnum.PAID.getCode()){
                order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(new Date());
                ordermapper.updatebyprimarykeyselective(order);
                return ServerResponse.createBySuccess("fahuochenggong");
            }
            return ServerResponse.createByErrorMessage("dingdanbucunzai")
        }

    }







//定时关单的功能

    public void closeorder (int hour){
        Date closedatetime = DateUtils.addHours(new Date(),-hour);
        List <order> orderList= ordermapper.selectorderstatusbycreatetime(Const.OrderStatusEnum.NO_PAY.getCode(),datetimeutils.dateToStr(closedatetime));
        for (order order:orderList){
            List<OrderItem>  orderitemList=orderitemmapper.getByOrderNo(order.getOrderNo());
            for (OrderItem orderItem:orderitemList){
            ///一定要用主键where 条件 防止锁表  同时必须是innodb 引擎
                Integer stock =- productmapper.selectstockbyproductid(orderItem.getProductId());
                if (stock==null){
                    continue;
                }
                product product = new product();
                product.setId(orderItem.getProductId());
                product.setStock(stock+orderItem.getQuantity());
                productmapper.updateByPrimaryKeySelective(product);
            }
            ordermapper.closeorderbyorderid(order.getId());
            log.info("",order.getOrderNo());


        }
    }
















}