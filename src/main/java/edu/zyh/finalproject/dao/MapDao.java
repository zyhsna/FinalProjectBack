package edu.zyh.finalproject.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 地理位置的相关Dao层
 * @author zyhsna
 */
@Mapper
public interface MapDao {

    /**
     * 更新设备定位相关
     *
     * @param userId    用户ID
     * @param deviceId  设备ID
     * @param longitude 经度
     * @param latitude  纬度
     * @return 1 成功 其他失败
     */
    @Update("update device set latitude = #{latitude},longitude = #{longitude} where owner_id = #{userId} and hardware_address = #{deviceId}")
    int updateLocation(int userId, String deviceId, double longitude, double latitude);

    /**
     * 根据用户Id和设备ID获取经度
     * @param userId 用户ID
     * @param deviceId 设备ID
     * @return 经度
     */
    @Select("select longitude from device where owner_id = #{userId} and device_uid = #{deviceId}")
    double getLongitude(int userId, String deviceId);

    /**
     * 根据用户Id和设备ID获取纬度
     * @param userId 用户ID
     * @param deviceId 设备ID
     * @return 纬度
     */
    @Select("select latitude from device where owner_id = #{userId} and device_uid = #{deviceId}")
    double getLatitude(int userId, String deviceId);

    @Update("update bill_receiver set distance = #{distance} where receiver_id = #{userId} and task_id = #{taskId}")
    int updateDistance(int userId, int taskId, double distance);
}
