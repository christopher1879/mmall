package dao;
import org.apache.ibatis.annotations.Param;
import pojo.order;

import java.util.List;

public interface ordermapper {
  int  deletebyprimarykey(Integer id);
  int insert(order record);
  int insertselective(order record);
  order selectbyprimarykey(Integer id);
  order selectbyuseridandorderno(@Param("userid")Integer userid ,@Param("orderno")long orderno);
  List<order> selectbyuserid(Integer userid);
  List<order>  selectallorder();
  order selectbyorderno(long orderno);
  int updatebyprimarykey(order record);
  int updatebyprimarykeyselective(order record);



  //二期新增的定时关闭订单的任务
  List<order> selectorderstatusbycreatetime(@Param("ststus") Integer status,@Param("date") String date);
  int closeorderbyorderid(Integer id);
}
