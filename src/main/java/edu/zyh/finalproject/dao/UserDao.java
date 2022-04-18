package edu.zyh.finalproject.dao;

import edu.zyh.finalproject.POJO.RegisterInfo;
import edu.zyh.finalproject.POJO.User;
import org.apache.ibatis.annotations.*;

/**
 * user的Dao层文件
 * @author zyhsna
 */
@Mapper
public interface UserDao {

    /**
     *根据手机号查询是否有用户存在
     * @param telephone 查询的手机号
     * @return 注册结果
     */
    @Select("select count(*) from user where telephone = #{telephone}")
    int findUserByTel(String telephone);

    /**
     * 用户进行登录操作
     * @param telephone 手机号
     * @param password  密码
     * @return User对象
     */
    @Select("select * from user where telephone=#{telephone} and password = #{password}")
    User userLogin(String telephone, String password);

    /**
     * Dao层 用户注册
     * @param user 用户注册相关信息
     * @return 注册结果，如果成功返回1
     */
    @Insert("insert into user(user_name, gender, telephone, user_level, password, user_role, point) value" +
            " (#{user.userName},#{user.gender},#{user.telephone},#{user.userLevel},#{user.password},#{user.userRole}, 0)")
    @Options(keyColumn = "user_id", keyProperty = "userId")
    int register(@Param("user") RegisterInfo user);


    /**
     * Dao层 用户修改密码

     * @param newPassword 新密码
     * @param userId 用户ID
     * @return 1 修改成功
     */
    @Update("update user set password = #{newPassword} where user_id = #{userId} ")
    int changePassword(String newPassword, int userId);

    /**
     * Dao层 根据用户id来查询用户是否存在
     * @param userId  用户id
     * @return 用户个数，1代表存在，0代表不存在
     */
    @Select("select count(*) from user where user_id = #{userId}")
    int findUserByUserId(int userId);

    /**
     *根据userId来查询用户信息
     * @param userId 用户id
     * @return 除了用户密码外的其他属性封装
     */
    @Select("select user_id ,user_name, telephone, gender, user_level,user_role, progress,point from user where user_id = #{userId}")
    User getPersonInfo(int userId);



}
