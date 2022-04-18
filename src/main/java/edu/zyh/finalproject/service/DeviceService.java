package edu.zyh.finalproject.service;

import edu.zyh.finalproject.POJO.Device;

import java.util.List;
import java.util.Map;

/**
 * @author zyhsna
 */
public interface DeviceService {
    /**
     * 用户绑定设备
     * @param userId 用户ID
     * @param hardwareAddress 蓝牙MAC地址
     * @param deviceName 设备命名
     * @param devicePrice 设备价格
     * @return -1 重复绑定  -2 注册失败  >0 成功
     */
    int insertNewDevice(int userId, String hardwareAddress,String deviceName,String devicePrice);

    /**
     * 根据用户ID查询绑定过得设备
     * @param userId 用户ID
     * @return List
     */
    List<Device> getDeviceListByUserId(int userId);

    /**
     * 获取设备最后一次位置用于导航
     * @param deviceId 设备ID
     * @return Map
     */
    List<Double> getDeviceLastLocation(int deviceId);

    /**
     * 用户设置设备遗失
     * @param deviceId 设备ID
     * @return 1 修改成功
     */
    int dropDevice(int deviceId);
}
