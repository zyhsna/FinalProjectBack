package edu.zyh.finalproject.service;

import java.util.Map;

/**
 * @author zyhsna
 */
public interface MapService {

    /**
     * 更新设备定位相关
     *
     * @param userId    用户ID
     * @param deviceId  设备ID
     * @param longitude 经度
     * @param latitude  纬度
     * @return 1 更新成功 其他失败
     */
    int updateLocation(int userId, String deviceId, double longitude, double latitude);

    /**
     * 根据用户ID和设备ID获取最后位置
     * @param userId 用户ID
     * @param deviceId 设备ID，是android系统生成的
     * @return Map< String, Double > 经纬度
     */
    Map<String, Double> getLastLocation(int userId, String deviceId);

    /**
     * 用户完成任务之后更新距离
     * @param userId 用户ID
     * @param taskId 任务ID
     * @param distance 距离
     * @return 1 成功
     */
    int updateDistance(int userId, int taskId);
}
