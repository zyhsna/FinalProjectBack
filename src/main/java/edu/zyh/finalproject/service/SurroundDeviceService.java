package edu.zyh.finalproject.service;

import edu.zyh.finalproject.POJO.SurroundDevice;

import java.util.List;

public interface SurroundDeviceService {
    /**
     * 用户扫描到新的设备进行更新
     * @param surroundDevice 设备
     * @return 成功1，错误其他  -1该设备未遗失  -2 该设备不在库中
     */
    int updateSurroundDevice(SurroundDevice surroundDevice);

    /**
     * 根据设备ID获取该设备最近被检测信息
     * @param deviceId 设备ID
     * @return List
     */
    List<SurroundDevice> getDropDeviceInfo(int deviceId);
}
