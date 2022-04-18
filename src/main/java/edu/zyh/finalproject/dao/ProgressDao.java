package edu.zyh.finalproject.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 管理用户等级Dao
 * @author zyhsna
 */
@Mapper
public interface ProgressDao {

    /**
     * 根据用户ID增加经验值进度
     * @param userId 用户ID
     * @return 1
     */
    @Update("update user set progress = progress + #{progressIncreasement} where user_id = #{userId}")
    int increaseProgressByUserId(int userId, double progressIncreasement);

    /**
     * 根据用户ID来查询用户的progress
     * @param userId yonghuID
     * @return double
     */
    @Select("select progress from user where user_id = #{userId}")
    double getProgressByUserId(int userId);

    /**
     * 设置用户的等级
     * @param userId 用户ID
     * @param userLevel 用户等级
     * @return 1 设置成功
     */
    @Update("update user set user_level = #{userLevel} where user_id = #{userId}")
    int setUserLevel(int userId, int userLevel);
}
