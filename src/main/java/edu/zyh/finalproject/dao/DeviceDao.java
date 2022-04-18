package edu.zyh.finalproject.dao;

import edu.zyh.finalproject.POJO.Device;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Device Dao
 * @author zyhsna
 */
@Mapper
public interface DeviceDao {
    /**
     * Dao层 用户绑定新设备
     * @param userId 用户ID
     * @param deviceUID 设备UID
     * @return 1 成功 其他失败
     */
    @Insert("insert into device(owner_id, hardware_address, device_name, price) value (#{userId},#{deviceUID},#{deviceName},#{devicePrice})")
    int insertNewDevice(int userId, String deviceUID,String deviceName, String devicePrice);

    /**
     * Dao层 查询是否重复绑定
     * @param userId 用户ID
     * @param deviceUID 设备UID
     * @return >0 重复绑定
     */
    @Select("select count(*) from device where owner_id = #{userId} and hardware_address = #{deviceUID}")
    int checkDuplicateDevice(int userId, String deviceUID);

    /**
     * 根据UserId来查询用户绑定的设备
     * @param userId 用户ID
     * @return List
     */
    @Select("select * from device where owner_id = #{userId}")
    List<Device> getDeviceListByUserId(int userId);

    /**
     * 根据设备id获取longitude
     * @param deviceId 设备ID
     * @return double
     */
    @Select("select longitude from device where device_id = #{deviceId}")
    double getDeviceLastLongitude(int deviceId);
    /**
     * 根据设备id获取latitude
     * @param deviceId 设备ID
     * @return double
     */
    @Select("select latitude from device where device_id = #{deviceId}")
    double getDeviceLastLatitude(int deviceId);

    /**
     * 获取设备价格
     * @param deviceId 设备ID
     * @return price
     *
     */
    @Select("select price from device  where device_id = #{deviceId}")
    int getDevicePrice(int deviceId);

    @Select("select device_id from device where hardware_address = #{hardwareAddress}")
    int getDeviceIdByHardwareAddress(String hardwareAddress);

    @Update("update device set drop_flag = 1 where device_id = #{deviceId}")
    int dropDevice(int deviceId);

    /**
     * 根据MAC地址查询该设备是否有遗失
     * @param hardwareAddress MAC地址
     * @return 1 遗失
     */
    @Select("select drop_flag from device where hardware_address = #{hardwareAddress}")
    int getDropFlagByHardwareAddress(String hardwareAddress);

    /**
     * 根据Mac地址查询是否有这个设备
     * @param hardwareAddress mac地址
     * @return 有为1 否则为0
     */
    @Select("select count(*) from device where hardware_address = #{hardwareAddress}")
    int checkExistenceByHardwareAddress(String hardwareAddress);

    /**
     * 根据设备ID获取设备MAC地址
     * @param deviceId 设备ID
     * @return mac address
     */
    @Select("select hardware_address from device where device_id = #{deviceId}")
    String getHardwareAddressByDeviceId(int deviceId);

    /**
     * 找回设备更新状态
     * @param deviceId 设备ID
     */
    @Update("update device set drop_flag = 0 where device_id = #{deviceId}")
    void findDeviceBack(int deviceId);
}
