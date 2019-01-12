package dao;

import pojo.catagory;

import java.util.List;

public interface catagorymapper {

    int deleteByPrimaryKey(Integer id);

    int insert(catagory record);

    int insertSelective(catagory record);

    catagory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(catagory record);

    int updateByPrimaryKey(catagory record);

    List<catagory> selectCategoryChildrenByParentId(Integer parentId);
}


