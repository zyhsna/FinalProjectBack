package edu.zyh.finalproject.dao;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用于对用户的点数进行修改
 * @author zyhsna
 */
@Mapper
public interface PointDao {

    /**
     * 根据用户ID进行相应的点数增长
     * @param userId 用户ID
     * @param point 要增长的点数多少
     * @return 1 成功
     */
    @Update("update user set point = point + #{point} where user_id = #{userId}")
    int increasePointByUserId(int userId, double point);

    /**
     * 根据用户ID来减少相应点数
     * @param userId 用户ID
     * @param point 点数
     * @return 1 成功
     */
    @Update("update user set point = point - #{point} where user_id = #{userId}")
    int decreasePointByUserId(int userId, double point);

    /**
     * 查询用户的点数多少
     * @param userId 用户ID
     * @return 点数
     */
    @Select("select point from user where user_id = #{userId}")
    double getPointByUserId(int userId);


}
