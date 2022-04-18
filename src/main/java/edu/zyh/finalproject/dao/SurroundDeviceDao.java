package edu.zyh.finalproject.dao;

import edu.zyh.finalproject.POJO.SurroundDevice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SurroundDeviceDao {
    /**
     * 更新相关数据
     * @param surroundDevice surroundDevice
     * @return 1 更新成功
     */
    @Update("update surround_device set scan_time = #{surroundDevice.scanTime},longitude=#{surroundDevice.longitude}," +
            "latitude=#{surroundDevice.latitude} where hardware_address = #{hardwareAddress} and scan_user_id = #{scanUserId}")
    int updateSurroundDevice(@Param("surroundDevice") SurroundDevice surroundDevice);

    /**
     * 根据设备Mac地址查询最近的丢失信息
     * @param hardwareAddress mac 地址
     *
     * @return list
     */
    @Select("select * from surround_device where hardware_address = #{hardwareAddress}")
    List<SurroundDevice> getDropDeviceInfo(String hardwareAddress);

    /**
     * 查询是否有这条记录
     * @param hardwareAddress mac地址
     * @param scanUserId 发现用户的ID
     * @return 1 存在
     */
    @Select("select count(*) from surround_device where hardware_address = #{hardwareAddress} and scan_user_id = #{scanUserId}")
    int checkExistence(String hardwareAddress, int scanUserId);

    /**
     * 新增周围设备
     * @param surroundDevice surroundDevice
     * @return 1 插入成功
     */

    @Insert("insert into surround_device(hardware_address,scan_time,longitude,latitude,scan_user_id) value (" +
            "#{surroundDevice.hardwareAddress},#{surroundDevice.scanTime},#{surroundDevice.longitude}," +
            "#{surroundDevice.latitude},#{surroundDevice.scanUserId}" +
            ")")
    int insertNewSurroundDevice(SurroundDevice surroundDevice);
}
